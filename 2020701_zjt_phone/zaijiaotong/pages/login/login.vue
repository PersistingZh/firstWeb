<template>
	<view class="login_center">
		<LunaUpgrade style="z-index: 99" ref="showtip" :url="url" :notes="notes"></LunaUpgrade>
		<view class="topBox">
			<view>
				<image class="logo" src="../../static/logo.png" mode="aspectFill"> </image>
				<image class="logoLabel" src="../../static/txt_zaijiaotong.png" mode="aspectFill"></image>
				<text>司法矫正人员核查管理系统</text>
				<image class="person" src="../../static/icon_head.png" mode="aspectFill"></image>
			</view>
		</view>
		<view class="content">
			<view class="phone">
				<image src="@/static/icon_shoujihao.png" mode="aspectFill"></image>
				<input type="number" v-model="inputAccount" @blur="isMobileCheck" maxlength="11" placeholder="请输入手机号" />
			</view>
			<view class="msgCode">
				<image src="@/static/icon_yanzhengma.png" mode="aspectFill"></image>
				<input type="number" v-model="inputPassword" @blur="isEmptyCheck" maxlength="6" placeholder="请输入验证码" />
				<button type="default" size="mini" :style="{ 'background-color': this.countDown ? '#c5c5c5' : '#148af0' }" @click="getMessage">
					{{ msgText }}
				</button>
			</view>
			<view class="subBtn">
				<button type="primary" @click="login">登录</button>
			</view>
		</view>
		<view class="footer" v-show="isShowFooter">
			<text>北京峰盛博远科技股份有限公司</text>
			<text>{{ version }}</text>
		</view>
	</view>
</template>

<script>
	import {
		isMobile,
		isEmpty
	} from '@/utils/CustomUitls.js'
	import {
		uploadFiles,
		compardFace,
		login,
		getPhoneCode,
		getServerApkVersion
	} from '@/api/api.js'
	import LunaUpgrade from '@/components/luna-upgrade/luna-upgrade.vue'
	const modal = uni.requireNativePlugin('modal')
	let lyBDFaceAuth

	export default {
		components: {
			LunaUpgrade,
		},
		data() {
			return {
				// 版本号
				version: plus.runtime.version,
				versionCode: plus.runtime.versionCode,
				// 升级内容
				notes: '',
				url: '',
				/** 账号密码输入框 */
				inputAccount: null,
				type: -1,
				inputPassword: '',
				accountTime: 0,
				/** 验证码模块 */
				msgText: '获取验证码',
				countDownTime: 60, // 倒计时
				countDown: false, // 是否点击了发送短信
				// 是否显示底部版本信息
				isShowFooter: true,
			}
		},
		mounted() {
			// console.log()
			// uni.showToast({
			// 	title: this.versionCode
			// })
			// const version = 0
			const version = plus.runtime.version
			const versionCode = plus.runtime.versionCode
			this.$nextTick(function() {
				// 在线更新
				uni.request({
					url: getApp().globalData.baseUrl + '/mFileUpdate/DownloadApk',
					method: 'POST',
					success: (response) => {
						if (response.statusCode === 200 && response.data) {
							const {
								data: res
							} = response
							console.log(res)
							console.log('version', versionCode)
							if (versionCode < res.version) {
								this.notes = '更新内容：' + res.remark
								this.url = 'http://120.79.9.200:82/' + res.versionPath
								console.log(this.url)
								this.$refs['showtip'].open()
							}
						}
					},
					fail: (e) => {
						console.error(e)
					},
				})

				uni.onKeyboardHeightChange((res) => {
					console.log(res.height)
					this.isShowFooter = res.height === 0 ? true : false
				})
			})
		},
		onLoad() {
			// #ifdef APP-PLUS
			if (uni.getSystemInfoSync().platform == 'ios' || uni.getSystemInfoSync().platform == 'android') {
				lyBDFaceAuth = uni.requireNativePlugin('longyoung-BDFaceAuth')
			}
			//引用插件
			// #endif
		},
		methods: {
			// 验证是否是手机号码
			isMobileCheck: function(e) {
				let value = e.detail.value
				// // console.log(e, "\n检查值:", value)
				if (!isMobile(value)) {
					uni.showToast({
						title: '手机号码有误',
						icon: 'none',
					})
					return false
				} else {
					// return true;
				}
			},
			isEmptyCheck: function(e) {
				let value = e.detail.value
				// // console.log(e, "\n检查值:", value)
				if (!isEmpty(value)) {
					uni.showToast({
						title: '验证码不能为空',
						icon: 'none',
					})
					return false
				} else if (value.length < 6) {
					uni.showToast({
						title: '验证码格式有误',
						icon: 'none',
					})
				} else {
					return true
				}
				// console.log("验证通过")
				// this.inputAccount = e.detail.value;
			},
			// // 去中文化
			// isExistChars:function(e,attr){
			// 	let value = e.detail.value;
			// 	this.$data[attr] =  this.customUtils.isExistChar(value,'');
			// },
			// 获取验证码
			getMessage: function() {
				// let _this = this;
				if (this.inputAccount) {
					if (!this.countDown) {
						var param = {
							phone: this.inputAccount,
						}
						this.getPhoneCodes(param)
					}
				} else {
					uni.showToast({
						title: '请输入手机号',
						icon: 'none',
					})
				}
			},
			getPhoneCodes(param) {
				// 设置不可再次点击按钮
				uni.showLoading({
					title: '获取短信验证码中',
				})
				this.countDown = true
				getPhoneCode(param)
					.then((res) => {
						// console.log("=====getPhoneCode:", res);
						uni.hideLoading()
						if (res.code == 0 && res.msg == '操作成功') {
							// 设置Toast
							uni.showToast({
								title: '发送成功',
								icon: 'none',
							})
							// 设置登录的type和验证码
							const {
								type,
								verificationCode
							} = res.data
							// this.inputPassword = verificationCode;
							this.type = type

							// 设置倒计时
							const start = setInterval(() => {
								//间歇调用计时器，间隔为1000ms
								if (this.countDownTime >= 0) {
									this.msgText = this.countDownTime-- + 's'
								} else {
									clearInterval(start) // 清除计时器
									this.msgText = '重新发送'
									this.countDown = false
									this.countDownTime = 60
								}
							}, 1000)
						} else {
							this.msgText = '重新发送'
							this.countDown = false
							this.countDownTime = 60
							uni.showToast({
								title: res.msg,
								icon: 'none',
							})
						}
					})
					.catch((e) => {
						console.error(e)
						uni.hideLoading()
						uni.showToast({
							title: e.message,
							icon: 'none',
						})
						this.msgText = '重新发送'
						this.countDown = false
						this.countDownTime = 60
					})
			},
			// 登录
			login: function() {
				if (!this.inputAccount) {
					uni.showToast({
						title: '请输入手机号码',
						icon: 'none',
					})
					return
				}
				if (!this.inputPassword) {
					uni.showToast({
						title: '请输入验证码',
						icon: 'none',
					})
					return
				}
				if (this.type == -1) {
					uni.showToast({
						title: '请重新获取验证码',
						icon: 'none',
					})
					return false
				}
				const param = {
					phone: this.inputAccount,
					verificationCode: this.inputPassword,
					type: this.type,
				}
				login(param).then((res) => {
					if (res.code == 0 && res.msg == '操作成功') {
						let eData = res.data
						getApp().globalData.userInfo = res.data.obj
						getApp().globalData.id = res.data.obj.id
						this.onScanFace(eData)
						// this.loginSuccess(eData);
						console.log('login:', eData)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none',
						})
					}
				})
			},
			onScanFace: function(eData) {
				uni.showLoading({
					title: '即将进行人像采集',
					icon: 'none',
					mask: true,
				})
				// console.log("lygg.onScanFace");
				// iOS License ID: testface1111-face-ios
				// 安卓 License ID: testface1111-face-android
				// 活体识别动作
				const action = [
					'Eye', // 眨眼
					'Mouth', // 张嘴
					'HeadLeft', // 头向左转
					'HeadRight', // 头向右转
					'HeadLeftOrRight', // 左右摇头
					'HeadUp', // 抬头
					'HeadDown', // 低头
				]

				const actionAry = []
				// 随机插入活体检测
				// for (let index = 0; index < 2; index++) {
				// 	const n = this.getRandomInt(0, action.length)
				// 	// console.log(n, 123)
				// 	actionAry.push(action[n])
				// }
				// // console.log(actionAry)

				// #ifdef APP-PLUS
				lyBDFaceAuth.scanFace({
						licenseID: 'testface1111-face-android', //必须与百度授权资料一致，注意安卓和 iOS 不一样。
						actionAry, //不传无动作
						surfaceRatio: 1,
						isLivenessRandom: 1, //不传默认有序，0有序，1随机
						isSound: 0, //不传默认有声音，0无声，1有声
						frontBack: 0, //不传默认前置，0前置，1后置
						txtColor: '#3987FD', //文字颜色
						bgColor: '#70000000', //背景颜色，iOS设置无效，需要换图片facecover_new.png，路径 nativeplugins\longyoung-BDFaceAuth-iOS\ios\com.baidu.idl.face.faceSDK.bundle，具体看示例。
						roundColor: '#3987FD', //圆的颜色
					},
					(result) => {
						if (result.bestImgBase64) {
							this.bestImgBase64Str = result.bestImgBase64

							this.resultStr = this.resultStr + '\n\n======base64字符串（太长，截取前200字符）：\n' + result.bestImgBase64.substring(0,
								200)
							delete result.bestImgBase64 //删除bestImgBase64
						}
						this.resultStr = this.resultStr + '\n======不包含图片部分：\n' + JSON.stringify(result)

						//status=0 且 err_code=0，为检测超时回调，仅安卓有效，iOS 无超时的说法。此处举例做关闭页面的操作，你可以做自己的逻辑。
						if (result.status == 0) {
							if (result.err_code == 0) {
								lyBDFaceAuth.closeAty({}, (result) => {
									uni.hideLoading()
								})
							}
						}

						// ***使用 uni.uploadFile()上传服务器，不需要将图片上传到服务器的，注释此处代码。
						// https://ask.dcloud.net.cn/question/30546, https://ask.dcloud.net.cn/question/76827
						// http://www.html5plus.org/doc/zh_cn/io.html#plus.io.URLType, https://ask.dcloud.net.cn/article/94
						if (this.bestImgBase64Str) {
							var bitmapT = new plus.nativeObj.Bitmap('test')
							//加载base64图片
							bitmapT.loadBase64Data(
								this.bestImgBase64Str,
								(res) => {
									// console.log(
									// "longyoung.loadBase64Data.suc=" + JSON.stringify(res)
									// );
									var options = {
										overwrite: true, //覆盖同名图片
										format: 'png',
										quality: 50,
										//去左右黑边
										clip: {
											width: '60%',
											left: '20%', //(100%-60%)/2
											height: '100%',
											top: '0%',
										},
										//截取头部
										// clip: {
										// 	width: "60%",
										// 	left: "20%", //(100%-60%)/2
										// 	height: "66%",
										// 	top: "9%"
										// }
									}
									//保存base64图片，请不要私自改变 _doc/ 这个头，除非你明确的知道 Bitmap.save() 的用法。
									bitmapT.save(
										'_doc/face.jpg',
										options,
										(res) => {
											bitmapT.clear() //销毁bitmap对象
											// console.log("longyoung.save.suc=" + JSON.stringify(res));
											this.imgBase64Str = res.target
											//图片上传服务器
											uni.uploadFile({
												url: getApp().globalData.baseUrl + 'mFileUpdate/file_upload?path=face',
												filePath: res.target,
												method: 'post',
												name: 'file',
												header: {
													'Content-Type': 'multipart/form-data',
												},
												success: (res) => {
													const requestData = JSON.parse(res.data)
													console.log(requestData, 'requestData')
													const {
														data
													} = requestData
													// 请求比对结果
													const postData = {
														urlA: data.filePath, // 现场抓拍图
														//   urlA: `${getApp().globalData.imageUrl}${data.subPath}/${data.fileName}`, // 现场抓拍图
														urlB: eData.obj.photoUrl, // 现场抓拍图
														urlD: 'd:/data/zjt/files/face/202007032115-b3e52bafeaaf4d11a2b7e7814334bfb5.jpg', // 现场抓拍图
														urlC: 'd:/data/zjt/files/face/202007031233-a69323ed70f84bf09a652affafe9f471.jpg', // 服务器图库存的图
													}
													console.log('请求比对结果', postData)
													if (eData.obj.photoUrl == '') {
														uni.showToast({
															title: '图片不存在,请联系管理人员',
															icon: 'none',
															duration: 2000,
														})
														return false
													}
													compardFace(postData.urlA, postData.urlB)
														.then((res) => {
															console.log('比对结果', res)
															if (res.code == 0) {
																const {
																	data
																} = res
																if (data.error_code == 0) {
																	if (Math.round(data.result.score) > 60) {
																		uni.hideLoading()
																		uni.showToast({
																			title: '人脸比对成功',
																			icon: 'none',
																			duration: 2000,
																		})
																		this.loginSuccess(eData)
																	} else {
																		uni.hideLoading()
																		uni.showToast({
																			title: '人脸比对失败，请重试！',
																			icon: 'none',
																			duration: 2000,
																		})
																		this.msgText = '重新发送'
																		this.countDown = false
																		this.countDownTime = 60
																	}
																} else {
																	uni.hideLoading()
																	uni.showToast({
																		title: '人脸比对失败，请重试！',
																		icon: 'none',
																		duration: 2000,
																	})
																	this.msgText = '重新发送'
																	this.countDown = false
																	this.countDownTime = 60
																}
															} else if (e.code == 404041 && e.msg == '图片不存在') {
																uni.showToast({
																	title: '图片不存在,请联系管理人员',
																	icon: 'none',
																	duration: 2000,
																})
															} else {
																uni.hideLoading()
																uni.showToast({
																	title: '人脸比对调用失败',
																	icon: 'none',
																	duration: 2000,
																})
															}
														})
														.catch((e) => {
															uni.hideLoading()
															console.error('compardFace========')
															console.error(e)
														})
												},
												fail: (res) => {
													// console.log("longyoung.uploadFile.fail=", res);
													uni.hideLoading()
													uni.showToast({
														title: '图片上传错误',
														icon: 'none',
													})
												},
											})
										},
										function(res) {
											// console.log("longyoung.save.fail=", res);
											uni.hideLoading()
											uni.showModal({
												title: 'bitmap保存错误',
												icon: 'none',
												content: JSON.stringify(res),
											})
										}
									)
								},
								function(res) {
									// console.log("longyoung.fail=", res);
									uni.hideLoading()
									uni.showModal({
										title: 'base64转bitmap错误',
										icon: 'none',
										content: JSON.stringify(res),
									})
								}
							)
						}
						// ***使用 uni.uploadFile()上传服务器，不需要将图片上传到服务器的，注释此处代码。
					}
				)
				// #endif
				// 测试
				// #ifdef H5

				// #endif
			},
			loginSuccess: function(eData) {
				uni.redirectTo({
					url: '../home/home?loginInfo=' + this.setUniFormatterData(eData),
				})
			},
			// 传参加码
			setUniFormatterData: function(n) {
				// //// console.log(encodeURIComponent(JSON.stringify(n)))
				return encodeURIComponent(JSON.stringify(n))
			}, // 传参解码
			getUniFormatterData: function(n) {
				// //// console.log(n,decodeURIComponent(n),JSON.parse(decodeURIComponent(n)))
				return JSON.parse(decodeURIComponent(n))
			},
		},
	}
</script>

<style lang="scss" scoped>
	.login_center {
		display: flex;
		flex-direction: column;
		height: 100%;

		.topBox {
			background-image: url(@/static/top_bg.png);
			background-size: contain;
			background-repeat: no-repeat;
			height: 550rpx;
			font-size: 12rpx;
			color: #fff;

			view {
				position: absolute;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				top: 40px;
				width: 100%;
			}

			.logo {
				width: 149rpx;
				height: 131rpx;
				margin-bottom: 16rpx;
			}

			.logoLabel {
				width: 143rpx;
				height: 52rpx;
				margin-bottom: 6rpx;
			}

			text {
				margin-bottom: 37px;
			}

			.person {
				width: 108rpx;
				height: 108rpx;
			}
		}

		.content {
			padding: 0 50rpx;
			flex: 1;

			button {
				color: #fff;
				border-radius: 60rpx;

				&::after {
					content: none;
				}
			}

			.phone,
			.msgCode {
				image {
					height: 50rpx;
					width: 30rpx;
					margin-right: 12rpx;
				}

				border-bottom: #e6e6e6 solid 1px;
				margin: 0 22rpx 20rpx;
				padding: 20rpx 0;
				display: flex;
				align-items: center;
				justify-content: space-between;

				input {
					flex: 1;
					padding: 0 20rpx;
				}
			}

			.msgCode {
				margin-bottom: 73rpx;

				input {
					margin: 0;
				}
			}

			.subBtn {
				border-radius: 60rpx;
				color: #fff;
			}
		}
	}
</style>
