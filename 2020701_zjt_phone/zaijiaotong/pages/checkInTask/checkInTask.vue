<template>
	<view v-if="userInfo != null">
		<view class="taskOverview itemBaseStyle" :class="completeStatus">
			<image src="@/static/icon_icon_kaoqing.png" mode="aspectFill"></image>
			<view class="contentText">
				<view>
					<text class="label">任务名称</text>
					<text class="content">{{userInfo.taskName == null ? "--" : userInfo.taskName}}</text>
				</view>
				<view>
					<text class="label">任务编号</text>
					<text class="content">{{userInfo.taskNum == null ? "--" : userInfo.taskNum}}</text>
				</view>
				<template v-if="userInfo.isFinish == 0">
					<image class="taskState" src="/static/uncomplete.png" mode="aspectFill"></image>
				</template>
				<template v-if="userInfo.isFinish == 1">
					<image class="taskState" :src="userInfo.clockEffective == '1' ? '/static/icon_null.png':userInfo.clockEffective == '0' ? '/static/complete.png' : '/static/uncomplete.png' "
					 mode="aspectFill"></image>
				</template>
			</view>
		</view>
		<view class="taskInfo itemBaseStyle">
			<text class="title">任务详情</text>
			<view class="contentText">
				<view>
					<text class="label">任务描述</text>
					<text class="content">{{userInfo.taskDesc == null ? "--" : userInfo.taskDesc}}</text>
				</view>
				<view>
					<text class="label">开始时间</text>
					<text class="content">{{userInfo.startTime == null ? "--" : userInfo.startTime}}</text>
				</view>
				<view>
					<text class="label">结束时间</text>
					<text class="content">{{userInfo.endTime == null ? "--" : userInfo.endTime}}</text>
				</view>
				<view>
					<text class="label">定位核验地点</text>
					<text class="content">{{userInfo.taskAddress == null ? "--" : userInfo.taskAddress}}</text>
					<uni-icons type="location-filled" size="20" color="#1395F2"></uni-icons>
				</view>
			</view>
		</view>
		<view style="width: 100vw;display: block;text-align: center;">
			<template v-if="userInfo.isFinish ==0">
				<button style="width: 80vw;" :disabled="!isDuringDate(userInfo.startTime,userInfo.endTime)" type="primary" class="subBtn"
				 @click="subBtn(userInfo)">定位核验</button>
			</template>
			<template v-else>
				<button style="width: 80vw;background: #CCCCCC;color: #FFF;" disabled="disabled" type="primary" class="subBtn"
				 @click="subBtn(userInfo)">已定位核验</button>
			</template>
		</view>
	</view>
	<view v-else>
		<view class="taskOverview itemBaseStyle" :class="completeStatus">
			<image src="@/static/icon_icon_kaoqing.png" mode="aspectFill"></image>
			<view class="contentText">
				<view>
					<text class="label">任务名称</text>
					<text class="content">暂无任务名称</text>
				</view>
				<view>
					<text class="label">任务编号</text>
					<text class="content">暂无任务编号</text>
				</view>
			</view>
		</view>
		<view class="taskInfo itemBaseStyle">
			<text class="title">任务详情</text>
			<view class="contentText">
				<view>
					<text class="label">任务描述</text>
					<text class="content">暂无任务</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		getTaskList
	} from "@/api/api.js"
	const location = uni.requireNativePlugin("Location")

	export default {
		data() {
			return {
				userInfo: null,
				clockInfo: null,
				taskList: [],
				clockLongitude: "",
				clockLatitude: "",
				newDate: ""
			};
		},
		onLoad(param) {
			this.newDate = Date.parse(new Date());
			this.getUserInfo(param);
			this.getLocation();
		},
		methods: {
			/** 获取用户信息
			 * @param {Object} param
			 */
			getUserInfo: function(param) {
				console.log("selectListItem", )
				if (param.selectListItem != undefined || param.selectListItem != null) {
					this.userInfo = this.getUniFormatterData(param.selectListItem)
				} else {
					this.userInfo = null;
				}
			},
			subBtn: function(selectItem) {
				uni.showLoading({
					title: "获取定位核验信息中，请稍等...",
				})
				// #ifdef APP-PLUS
				// 只是让系统弹出获取权限提示框
				uni.getLocation({
					type: 'wgs84',
					success: function(res) {},
					fail: function(err) {
						console.log(err)
					}
				});
				// 获取定位
				// locationMode  定位模式
				//   0  Battery_Saving,
				//   1  Device_Sensors,
				//   2  Hight_Accuracy;
				// 	设置是否开启gps定位
				//     setGpsFirst
				// 设置定位超时时间
				//     setHttpTimeOut
				location.getLocation({
					locationMode: 0,
					openGps: true,
					timeOut: 3000
				}, (ret) => {
					// console.log(ret, 1212)
					const {
						code,
						data
					} = ret
					uni.hideLoading();
					if (code === 0) {
						this.clockLongitude = data.longitude
						this.clockLatitude = data.latitude
						const clockInfo = {
							"clockAddress": data.address,
							"clockEffective": 0,
							"clockLongitude": parseFloat(this.clockLongitude),
							"clockLatitude": parseFloat(this.clockLatitude),
							"taskRemarks": selectItem.taskRemarks,
							"id": selectItem.id,
							// 区域圆
							"clockRadius": selectItem.taskRange,
							"taskLongitude": parseFloat(selectItem.taskLongitude),
							"taskLatitude": parseFloat(selectItem.taskLatitude)
						};
						console.log("checkInTask=============clockInfo")
						console.log(clockInfo)
						uni.navigateTo({
							url: '../faceCheck/faceCheck?clockInfo=' + this.setUniFormatterData(clockInfo)
						})
					} else {
						uni.showToast({
							title: "获取定位失败，请打开定位权限跟gps！",
							icon: "none"
						})
					}
				})
				// #endif
				// #ifdef H5
				const clockInfo = {
					"clockAddress": selectItem.taskAddress,
					"clockEffective": 0,
					"clockLongitude": parseFloat(this.clockLongitude),
					"clockLatitude": parseFloat(this.clockLatitude),
					"taskRemarks": selectItem.taskRemarks,
					"id": selectItem.id,
					// 区域圆
					"clockRadius": selectItem.taskRange,
					"taskLongitude": parseFloat(selectItem.taskLongitude),
					"taskLatitude": parseFloat(selectItem.taskLatitude)
				};
				uni.hideLoading();
				console.log(clockInfo)
				uni.navigateTo({
					url: '../faceCheck/faceCheck?clockInfo=' + this.setUniFormatterData(clockInfo)
				});
				// #endif
			},
			// 获取定位坐标
			getLocation: function() {
				// #ifdef APP-PLUS
				// 获取定位
				// locationMode  定位模式
				//   0  Battery_Saving,
				//   1  Device_Sensors,
				//   2  Hight_Accuracy;
				// 	设置是否开启gps定位
				//     setGpsFirst
				// 设置定位超时时间
				//     setHttpTimeOut
				location.getLocation({
					locationMode: 0,
					openGps: true,
					timeOut: 3000
				}, (ret) => {
					console.log(ret, 1212)
					const {
						code,
						data
					} = ret
					if (code === 0) {
						this.clockLongitude = data.longitude
						this.clockLatitude = data.latitude
					} else {
						uni.showToast({
							title: "获取定位失败，错误代码" + code + "请打开定位权限跟gps！",
							icon: "none"
						})
					}
				})
				// #endif
				// #ifdef H5
				this.clockLongitude = 113.51271063518064;
				this.clockLatitude = 22.27593687120433;
				// #endif
			},
			// 传参加码
			setUniFormatterData: function(n) {
				// //console.log(encodeURIComponent(JSON.stringify(n)))
				return encodeURIComponent(JSON.stringify(n))
			}, // 传参解码
			getUniFormatterData: function(n) {
				// //console.log(n,decodeURIComponent(n),JSON.parse(decodeURIComponent(n)))
				return JSON.parse(decodeURIComponent(n))
			},
			isDuringDate: function(beginDateStr, endDateStr) {
				var curDate = Date.parse(new Date()),
					beginDate = Date.parse(beginDateStr.replace(/-/g, '/')),
					endDate = Date.parse(endDateStr.replace(/-/g, '/'));
				console.log("【==================================================");
				var isSameDay = this.isSameDay(new Date(), beginDateStr.replace(/-/g, '/'));
				console.log(isSameDay);
				console.log("==================================================】");
				if (curDate >= beginDate && curDate <= endDate) {
					// 在日期范围内 => 正常定位核验
					return true;
				}
				// if (curDate >= beginDate && curDate >= endDate && this.isSameDay(new Date(),beginDateStr.replace(/-/g,'/'))) {
				if (curDate >= beginDate && curDate >= endDate) {
					// 在同一天 且 已过日期范围  => 迟到定位核验
					return true;
				}
				return false;
			},
			// 判断当前时间与开始时间是否同一天
			isSameDay: function(timeStampA, timeStampB) {
				let dateA = new Date();
				let dateB = new Date(timeStampB);
				return (dateA.setHours(0, 0, 0, 0) == dateB.setHours(0, 0, 0, 0));
			}
		},
		computed: {
			completeStatus: function() {
				const n = Math.random()
				// console.log(n)
				// return n > 0.5 ? 'complete' : 'uncomplete'
				return 'complete'
			}
		}
	}
</script>

<style lang="scss" scoped>

	.taskState {
		position: absolute;
		right: 10rpx;
		top: 30rpx;
		width: 164rpx;
		height: 150rpx;
		z-index: 10;
	}

	.taskOverview {
		display: flex;
		align-items: center;
		margin: 22rpx 32rpx;
		padding: 35rpx;
		background-repeat: no-repeat;
		background-size: 150rpx;
		background-position: center;
		background-position-x: 98%;
		position: relative;

		&::after {
			content: "";
			width: 20rpx;
			height: 72rpx;
			background-image: url(@/static/line.png);
			background-size: contain;
			position: absolute;
			bottom: -50rpx;
			left: 30rpx;
			z-index: 99;
		}

		&::before {
			content: "";
			width: 20rpx;
			height: 72rpx;
			background-image: url(@/static/line.png);
			background-size: contain;
			position: absolute;
			bottom: -50rpx;
			right: 30rpx;
			z-index: 99;
		}

		image {
			width: 85rpx;
			height: 85rpx;
		}

		.contentText {
			margin-left: 38rpx;
			padding: 0;

			.content {
				margin-left: 30rpx;
				display: block;
				width: 40vw;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}
		}
	}

	.taskInfo {
		margin-bottom: 87rpx;
		text-align: center;
		font-family: SourceHanSansSC-regular;
		border-radius: 0;

		.title {
			display: block;
			padding: 15px 0;
			border-bottom: 1px #D9D9D9 dashed;
			color: #999;
		}
	}

	.contentText {
		display: flex;
		flex-direction: column;
		align-items: start;
		justify-content: center;
		font-size: 14px;
		padding: 22rpx 64rpx 22rpx 22rpx;

		view {
			margin-bottom: 20rpx;
			display: flex;
			align-items: center;
			width: 100%;

			&:last-child {
				margin: 0;
			}
		}

		.label {
			color: #adadad;
		}

		.content {
			color: #333;
			margin-left: 60rpx;
			flex: 1;
			text-align: left;
		}
	}

	.subBtn {
		margin-bottom: 50rpx;
		border-radius: 150rpx;
	}

	.subBtn:disabled {
		background-color: #CCCCCC;
		color: #FFFFFF;
	}
</style>
