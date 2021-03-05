<template>
	<view>
		<view class="status_bar">
			<!-- 这里是状态栏 -->
		</view>

		<view class="divStatusBar"> <text>在矫通</text> </view>

		<view class="contentBox">
			<view class="user itemBaseStyle" @click="openUserInfo">
				<template v-if="userInfo==null">
					<view class="avatar">
						<image src="@/static/icon_head.png" mode="aspectFill"></image>
					</view>
					<view class="userInfo">
						<view>
							<text class="name">***</text>
							<text class="userRole">******</text>
						</view>
						<text class="phone">***********</text>
					</view>
				</template>
				<template v-else>
					<view class="avatar">
						<image :src="userInfo.imageUrl" mode="aspectFill"></image>
					</view>
					<view class="userInfo">
						<view>
							<text class="name">{{userInfo.userType == 1 ? userInfo.userName : userInfo.name}}</text>
							<text class="userRole">{{userInfo.userType == 1 ? "司法矫正人员":"司法人员"}}</text>
						</view>
						<text class="phone">{{userInfo.phone != null ? userInfo.phone.toString().substring(0,3) + "********" : "***********" }}</text>
					</view>
				</template>
				<uni-icons type="arrowright" size="18" color="#D9D9D9"></uni-icons>
			</view>
			<view class="itemBox">
				<template v-for="(item) in itemArr">
					<view class="item itemBaseStyle" :key="item.id" @click="openPage(item.label)">
						<image :src="imgUrl(item.icon)" mode="aspectFill"></image>
						<view class="labelText">
							<text>{{item.label}}</text>
							<text>{{item.unReadNum}}</text>
						</view>
						<text class="subTitle">{{item.subtitle}}</text>
					</view>
				</template>
			</view>
		</view>

		<view class="footer">
			<text>北京峰盛博远科技股份有限公司</text>
			<text>{{version}}</text>
		</view>
	</view>
</template>

<script>
	import uniIcons from "@/components/uni-icons/uni-icons.vue"
	import coordtransform from "@/npm/node_modules/coordtransform"
	import {
		getTaskCount,
		getWarningCount,
		receivePhoneGPS
	} from "@/api/api.js"

	const location = uni.requireNativePlugin("Location")
	const testModule = uni.requireNativePlugin("TestModule")
	const modal = uni.requireNativePlugin('modal');
	export default {
		components: {
			uniIcons
		},
		computed: {

		},
		data() {
			return {
				// 版本号
				version: plus.runtime.version,
				versionCode: plus.runtime.versionCode,
				// 用户信息
				userInfo: null,
				userImageUrl: "",
				// 用户类型
				// userType: 0,
				// 首页列表
				itemArr: [{
					id: "itemArr0",
					icon: 'icon_icon_kaoqing',
					label: '定位核验',
					subtitle: '查看并完成定位核验',
					unReadNum: 0
				}, {
					id: "itemArr1",
					icon: 'icon_yujing',
					label: '位置预警',
					subtitle: '位置异常告警通知',
					unReadNum: 0
				}]

			};
		},
		onLoad(param) {
			location.getLocation({
				locationMode: 0,
				openGps: true,
				timeOut: 3000
			}, (ret) => {
				const {
					code,
					data
				} = ret
				console.log("ret==========================")
				console.log(ret);
				const gcj02towgs84 = coordtransform.gcj02towgs84(data.longitude, data.latitude);
				const postData = {
					userId: this.userInfo.id,
					longitude: gcj02towgs84[0],
					latitude: gcj02towgs84[1],
					address: ret.data.address
				}
				// modal.toast({
				// 	message: ret,
				// 	duration: 30
				// });
				if (code === 0) {
					receivePhoneGPS(postData).then(res => {
						console.log(res)
					}).catch(e => {
						console.error(e)
					})
				} else {
					uni.showToast({
						title: "获取定位失败，请打开定位权限跟gps！",
						icon: "none"
					})
				}
			});
			// console.log("home==========================")
			setInterval(() => {
				location.getLocation({
					locationMode: 0,
					openGps: true,
					timeOut: 3000
				}, (ret) => {
					const {
						code,
						data
					} = ret
					console.log(ret);
					const gcj02towgs84 = coordtransform.gcj02towgs84(data.longitude, data.latitude);
					const postData = {
						userId: this.userInfo.id,
						longitude: gcj02towgs84[0],
						latitude: gcj02towgs84[1],
						address: ret.data.address
					}
					if (code === 0) {
						receivePhoneGPS(postData).then(res => {
							console.log(res)
						}).catch(e => {
							console.error(e)
						})
					} else {
						uni.showToast({
							title: "获取定位失败，请打开定位权限跟gps！",
							icon: "none"
						})
					}
				})

			}, 5 * 60 * 1000);
			// setInterval(() => {
			// 			// modal.toast({
			// 			// 	message: ret,
			// 			// 	duration: 30
			// 			// });
			// 		// })
			// }, 3000)
			this.getUserInfo(param);
			// testModule.gotoNativePage()


			uni.getLocation({
				type: 'wgs84',
				success: function(res) {
					// console.log('res', res)
					// console.log('当前位置的经度：' + longitude);
					// console.log('当前位置的纬度：' + latitude);
					// modal.toast({
					// 	message: res,
					// 	duration: 30
					// });
				},
				fail: function(err) {
					console.log(err)
				}
			});
		},
		onShow() {
			console.log("home onShow ================")
			console.log(this.userInfo.id);
			if (this.userInfo.id) {
				this.initPageList(this.userInfo.id);
			}
		},
		activated() {
			console.log("home activated================")
			if (this.userInfo.id) {
				this.initPageList(this.userInfo.id);
			}
		},
		methods: {
			imgUrl: function(path) {
				console.log(path)
				return '../../static/' + path + '.png'
			},
			/** 获取用户信息
			 * @param {Object} param
			 */
			getUserInfo: function(param) {
				if (param.loginInfo != undefined || param.loginInfo != null) {
					const userData = this.getUniFormatterData(param.loginInfo)
					this.userInfo = userData.obj;
					this.userInfo['userType'] = userData.type;
					this.userInfo['imageUrl'] = getApp().globalData.imageUrl + this.userInfo.photoUrl.substring(24);
					console.log(this.userInfo['imageUrl'],"this.userInfo['imageUrl']")
					// console.log(this.getUniFormatterData(param.loginInfo))
					// console.log(this.userInfo)
					// console.log(getApp().globalData.USEID)
					this.initPageList(this.userInfo.id);
					console.log(this.userInfo)

				} else {
					this.userInfo = null;
					this.userImageUrl = "@/static/icon_head.png";
				}
			},
			// 打开退出页面
			openUserInfo: function() {
				uni.navigateTo({
					// url: '../signOut/signOut'
					url: '../myInfo/myInfo?userInfo=' + this.setUniFormatterData(this.userInfo),
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			initPageList: function(userId) {
				var param = {
					userId: userId
				};
				getTaskCount(param).then(res => {
					// console.log("getTaskCount:", res);
					if (res.code == 0 && res.msg == "操作成功") {
						if (res.data != null) {
							this.itemArr[0].unReadNum = res.data;
						}
					}
				});
				getWarningCount(param).then(res => {
					// console.log("getWarningCount", res);
					if (res.code == 0 && res.msg == "操作成功") {
						if (res.data != null) {
							this.itemArr[1].unReadNum = res.data;
						}
					}
				});
			},
			// 两个按钮跳转页面
			openPage: function(label) {
				// console.log(label)
				switch (label) {
					case '定位核验':
						uni.navigateTo({
							url: '../checkInTask/checkInTaskList?userInfo=' + this.setUniFormatterData(this.userInfo),
							animationType: 'pop-in',
							animationDuration: 200
						})
						break;
					case '位置预警':
						uni.navigateTo({
							url: '../locationWarning/locationWarning?userInfo=' + this.setUniFormatterData(this.userInfo),
							animationType: 'pop-in',
							animationDuration: 200
						})
						break;
					default:
						break;
				}
			},
			// 传参加码
			setUniFormatterData: function(n) {
				// //console.log(encodeURIComponent(JSON.stringify(n)))
				return encodeURIComponent(JSON.stringify(n))
			}, // 传参解码
			getUniFormatterData: function(n) {
				// //console.log(n,decodeURIComponent(n),JSON.parse(decodeURIComponent(n)))
				return JSON.parse(decodeURIComponent(n))
			}
		}
	}
</script>

<style lang="scss" scoped>
	.status_bar {
		height: var(--status-bar-height);
		width: 100%;
		background-color: #108bff;
	}

	.divStatusBar {
		display: flex;
		align-items: start;
		padding: 10rpx 0;
		background-color: #108bff;
		height: 143rpx;

		text {
			font-weight: 700;
			font-size: 16px;
			line-height: 30px;
			text-align: center;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			font-family: PingFang SC;
			color: #FFFFFF;
			margin: 0 auto;
		}
	}

	.contentBox {
		position: absolute;
		top: 120rpx;
		width: 100%;

		.user {
			margin: 32rpx;
			padding: 24rpx;
			display: flex;
			align-items: center;

			.avatar {
				width: 100rpx;
				height: 100rpx;

				image {
					width: 100%;
					height: 100%;
					border-radius: 50%;
				}
			}

			.userInfo {
				flex: 1;
				display: flex;
				flex-direction: column;
				padding-left: 36rpx;
				font-family: PingFang SC;

				view {
					display: flex;
					align-items: center;
				}

				.name {
					color: #000;
					font-size: 16px;
					// padding: 10rpx;
				}

				.userRole {
					color: #FF7A42;
					border: 1px solid #FF7A42;
					padding: 2px 4px;
					border-radius: 4px;
					font-size: 12px;
					margin-left: 18rpx;
				}

				.phone {
					margin-top: 13rpx;
					color: #ccc;
					font-size: 12px;
					// padding: 10rpx;
				}
			}

		}

		.itemBox {
			display: flex;
			flex-wrap: wrap;
			align-items: center;
			justify-content: space-around;
			margin-top: 66rpx;

			.item {
				text-align: center;
				width: 280rpx;
				height: 280rpx;
				display: flex;
				flex-direction: column;
				justify-content: center;
				align-items: center;
				font-family: PingFang SC;

				image {
					width: 85rpx;
					height: 85rpx;
				}

				.labelText {
					display: flex;
					align-items: center;
					font-size: 18px;
					color: #333;
					font-size: 16px;
					text-align: center;
					margin-top: 60rpx;
					font-family: PingFang SC;

					text {
						&:last-child {
							background-color: #FF6666;
							border-radius: 50%;
							color: #fff;
							width: 30rpx;
							height: 30rpx;
							line-height: 30rpx;
							font-size: 10px;
							margin-left: 5rpx;
							display: block;
						}
					}
				}

				.subTitle {
					font-family: PingFang SC;
					text-align: center;
					color: #d9d9d9;
					font-size: 12px;
					margin-top: 8rpx;
				}
			}
		}
	}
</style>
