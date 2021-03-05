<script>
	const dcRichAlert = uni.requireNativePlugin('DCloud-RichAlert')

	export default {
		globalData: {
			baseUrl: "http://112.33.40.121:81/",
			imageUrl: "http://112.33.40.121:82/",
			id: '',
			loginUser: {},
			IMEICODE: "", // 设备标识
			USEID: "",
			loginUser: {

			},
			userInfo: null,
		},
		onLaunch: function() {
			// #ifdef APP-PLUS
			// 提示用户获取定位权限
			uni.getLocation({
				success() {},
				fail(e) {
					// App跳转系统的设置界面
					uni.getSystemInfo({
						success(res) {
							if (res.platform == 'ios') { //IOS
								plus.runtime.openURL("app-settings://");
							} else if (res.platform == 'android') { //安卓
								let main = plus.android.runtimeMainActivity();
								let Intent = plus.android.importClass("android.content.Intent");
								let mIntent = new Intent('android.settings.ACTION_SETTINGS');
								main.startActivity(mIntent);
							}
						}
					});
				}
			})
			// 获取设备唯一id
			plus.device.getInfo({
				success: function(e) {
					console.log('getDeviceInfo success: ' + JSON.stringify(e));
					console.log('getDeviceInfo success: ' + JSON.stringify(e.uuid));
					getApp().globalData.IMEICODE = e.uuid.split(",")[0];
				},
				fail: function(e) {
					console.log('getDeviceInfo failed: ' + JSON.stringify(e));
				}
			});
			// 获取客户端当前版本号码，今天在线升级判断
			// plus.runtime.version;
			// const version = 0
			// const versionCode = plus.runtime.versionCode;
			// uni.request({
			// 	url: getApp().globalData.baseUrl + '/mFileUpdate/DownloadApk',
			// 	method: 'POST',
			// 	success: (response) => {
			// 		console.log(response)
			// 		if (response.statusCode === 200 && response.data) {
			// 			const {
			// 				data: res
			// 			} = response
			// 			console.log(res)
			// 			if (version < res.version) {


			// 				uni.showModal({
			// 					title: '升级提示',
			// 					content: '这是一个升级提示',
			// 					showCancel: false,
			// 					success: (res) => {
			// 						console.log(res, 'confirm')
			// 						if (res.confirm) {
			// 							console.log('用户点击确定，开始下载');

			// 							const downloadTask = uni.downloadFile({
			// 								url: 'http://120.79.9.200:82/apk/zjtAPK v1.1.1.apk',
			// 								success: (downloadResult) => {
			// 									if (downloadResult.statusCode === 200) {
			// 										plus.runtime.install(downloadResult.tempFilePath, {
			// 											force: false
			// 										}, function() {
			// 											console.log('install success...');
			// 											plus.runtime.restart();
			// 										}, function(e) {
			// 											console.error(e)
			// 											console.error('install fail...');
			// 										});
			// 									}
			// 								},
			// 								fail: (e) => {
			// 									console.error(e)
			// 									uni.showToast({
			// 										title: '下载失败',
			// 										icon: 'none'
			// 									})
			// 								},
			// 								complete: () => {
			// 									console.log('下载完成')
			// 								}
			// 							});

			// 							downloadTask.onProgressUpdate((res) => {
			// 								console.log('下载进度' + res.progress);
			// 								// 测试条件，取消下载任务。
			// 								if (res.progress > 50) {
			// 									downloadTask.abort();
			// 								}
			// 							});

			// 						}
			// 					}
			// 				})

			// 			}
			// 		}

			// 	},
			// 	fail: (e) => {
			// 		console.error(e)

			// 		uni.showToast({
			// 			title: '获取服务器版本失败',
			// 			icon: 'none'
			// 		})
			// 	}
			// })




			// #endif
		},
		onShow: function() {
			console.log('App Show123')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style>
	/*每个页面公共css */
	body {
		background-color: #f3f4f6;
		font-family: PingFang SC;
	}
	uni-page-body{
		height: 100%;
	}

	.itemBaseStyle {
		position: relative;
		background: #ffffff;
		border-radius: 8px;
		box-shadow: 0px 2px 6px 0px rgba(139, 159, 217, 0.2);
		border: 1px solid rgba(255, 255, 255, 0);
	}

	.footer {
		color: rgba(153, 153, 153, 1);
		font-size: 12px;
		position: absolute;
		bottom: 26rpx;
		display: flex;
		flex-direction: column;
		font-family: PingFang SC;
		justify-content: center;
		width: 100%;
		align-items: center;
	}
</style>
