<template>
	<view class="map-body">
		<view class="map" id="mapContainer">
			<web-view style="height: 500px;" :src="mapUrl" @message="message"></web-view>
		</view>
	</view>
</template>

<script>
	import {
		punchClock
	} from "@/api/api.js"
	const location = uni.requireNativePlugin("Location")
	const modal = uni.requireNativePlugin('modal');
	export default {
		data() {
			return {
				clockInfo: null,
				mapUrl: "/hybrid/html/webMap/webMap.html",
				locationInfo: {
					latitude: null,
					longitude: null
				}
			};
		},
		onLoad(param) {
			let _this = this;
			this.getClockInfo(param);
	
			// location.testAsyncFunc({
			// 		'name': 'unimp2',
			// 		'age': 1
			// 	},
			// 	(ret) => {
			// 		console.log(ret, 1212)
			// 		const {
			// 			code
			// 		} = ret
			// 		this.locationInfo.latitude = code.latitude
			// 		this.locationInfo.longitude = code.longitude
			// 		modal.toast({
			// 			message: this.locationInfo,
			// 			duration: 1.5
			// 		});
			// 		// this.setMapUrl(this.locationInfo.longitude, this.locationInfo.latitude)
			// 		// console.log(this.mapUrl)
			// 	})
			
			// uni.getLocation({
			// 	type: 'wgs84',
			// 	success: function(res) {
			// 		let longitude = res.longitude;
			// 		let latitude = res.latitude;
			// 		console.log('当前位置的经度：' + longitude);
			// 		console.log('当前位置的纬度：' + latitude);
			// 		_this.mapUrl += "?longitude=" + encodeURIComponent(longitude) + "&latitude=" + latitude

			// 	},
			// 	fail: function(err) {
			// 		console.log(err)
			// 	}
			// });
		},
		methods: {
			/** 定位信息
			 * @param {Object} param
			 */
			getClockInfo: function(param) {
				console.log("map getClockInfo", )
				this.clockInfo = this.getUniFormatterData(param.clockInfo)
				if (this.clockInfo != undefined || this.clockInfo != null) {
					console.log("获取 clockInfo", this.clockInfo)
					this.openMap(this.clockInfo);
				} else {
					this.clockInfo = null;
					uni.showToast({
						title:"获取 clockInfo失败，请重新获取",
						icon:"none"
					})
				}
			},
			openMap: function(clockInfo) {
				let _this = this;
				let clockAddress = clockInfo.clockAddress,
					clockEffective = clockInfo.clockEffective,
					clockLatitude = clockInfo.clockLatitude,
					clockLongitude = clockInfo.clockLongitude,
					id = clockInfo.id,
					taskRemarks = clockInfo.taskRemarks,
					clockRadius = clockInfo.clockRadius,
					taskLongitude = clockInfo.taskLongitude,
					taskLatitude = clockInfo.taskLatitude;
				_this.mapUrl += "?clockRadius=" + encodeURIComponent(clockRadius) +
					"&taskLongitude=" + encodeURIComponent(taskLongitude) +
					"&taskLatitude=" + encodeURIComponent(taskLatitude) +
					"&clockLatitude=" + encodeURIComponent(clockLatitude) +
					"&clockLongitude=" + encodeURIComponent(clockLongitude)
			},
			// web-view接收信息
			message(e) {
				console.log(e)
				let postData = e.detail.data[0];
				if (postData.action !== 'message') {
					if (postData.action) {
						this.clockInfo.clockLongitude = postData.clockLongitude;
						this.clockInfo.clockLatitude = postData.clockLatitude;
						this.punchClockFun(this.clockInfo);
					} else if (!postData.action) {
						// this.punchClockFun(this.clockInfo);
					}
				}
			},
			// 定位核验方法
			punchClockFun: function(clockInfo) {
				punchClock(clockInfo).then(res => {
					console.log("==punchClock", res);
					if(res.msg == "操作成功"){
						uni.showModal({
							title:"定位核验成功",
							success() {
							}
						})
					}else{
						uni.showModal({
							title:res.msg,
							success() {
							}
						})
					}
					return res;
				}).then(res => {
					// console.log(123123, res)
					const page = getCurrentPages()
					// console.log(page)
					uni.navigateBack({
						delta: 2
					});
				});
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
	#container {
		width: 300px;
		height: 180px;
	}

	.map-body {
		position: relative;
		display: flex;
		flex-direction: column;
		width: 100%;

		.map {
			width: 100%;
			height: 80vh;

			map {
				width: 100%;
				height: 100%;
				flex: 1;
			}
		}

		.btns {
			height: 5vh;
			color: #fff;
			display: flex;
			align-items: center;

			view {
				width: 100%;
			}

			button {
				margin: 0 25rpx;
				height: 100rpx;
				border-radius: 150rpx;
			}
		}
	}
</style>
