<template>
	<view v-if="taskLists.length > 0">
		<view v-for="taskItem in taskLists" :key="taskItem.id">
			<view class="item itemBaseStyle" :class="completeStatus" @click="selectListItem(taskItem)">
				<image src="@/static/icon_icon_kaoqing.png" mode="aspectFill"></image>
				<view class="info">
					<view>
						<text class="label">任务名称</text>
						<text class="contentText">{{taskItem.taskName == null ? "--" : taskItem.taskName }}</text>
					</view>
					<!-- <view>
						<text class="label">任务编号</text>
						<text class="contentText">{{taskItem.taskNum == null ? "--" : taskItem.taskNum}}</text>
					</view> -->
					<view>
						<text class="label">开始时间</text>
						<text class="contentText">{{taskItem.startTime == null ? "--" : taskItem.startTime }} </text>
					</view>
					<view>
						<text class="label">结束时间</text>
						<text class="contentText">{{taskItem.endTime == null ? "--" : taskItem.endTime }} </text>
					</view>
					<template v-if="taskItem.isFinish == 0">
						<image class="taskState" src="/static/uncomplete.png"
						 mode="aspectFill"></image>
					</template>
					<template v-if="taskItem.isFinish == 1">
						<image class="taskState" :src="taskItem.clockEffective == '1' ? '/static/icon_null.png':taskItem.clockEffective == '0' ? '/static/complete.png' : '/static/uncomplete.png' "
						 mode="aspectFill"></image>
					</template>
				</view>
				<!-- <uni-icons type="arrowright" size="18" color="#D9D9D9"></uni-icons> -->
			</view>
		</view>
		<uni-load-more :status="status" :show-icon="true" v-if="showLoadMore"></uni-load-more>
	</view>
	<view v-else>
		<view class="noData">
			<text>暂无数据</text>
		</view>
	</view>
</template>

<script>
	import uniLoadMore from "@/components/uni-load-more/uni-load-more.vue"
	import uniIcons from "@/components/uni-icons/uni-icons.vue"
	import {
		getTaskList
	} from "@/api/api.js"
	const location = uni.requireNativePlugin("Location")

	export default {
		components: {
			uniIcons,
			uniLoadMore
		},
		data() {
			return {
				userInfo: null,
				clockInfo: null,
				taskLists: [],
				clockLongitude: "",
				clockLatitude: "",
				// 下拉刷新
				status: 'more',
				showLoadMore: true,
				loadMoreText: "加载中...",
				newCurrent: 1,
				newPagesSize: 10
			};
		},
		onLoad(param) {
			// 
			this.status = "more";
			this.showLoadMore = false;
			this.newCurrent = 1;
			this.newPagesSize = 10;
			//
			this.getUserInfo(param);
			this.getLocation();
		},
		// 上拉加载
		onReachBottom() {
			var _this = this;
			console.log("onReachBottom 上拉加载");
			_this.status = 'loading';
			_this.showLoadMore = true;
			setTimeout(() => {
				_this.newCurrent += 1;
				_this.getMoreList(_this.userInfo.id, _this.newCurrent, _this.newPagesSize);
				_this.status = 'more';
			}, 300);
		},
		// 下拉刷新  
		onPullDownRefresh() {
			var _this = this;
			console.log('onPullDownRefresh 下拉刷新');
			_this.newCurrent = 1;
			_this.newPagesSize = 10;
			setTimeout(() => {
				_this.initList(_this.userInfo.id, _this.newCurrent, _this.newPagesSize);
			}, 300);
		},
		onShow(){
			console.log("checkInTaskList  onShow =====================")
		},
		methods: {
			/** 获取用户信息
			 * @param {Object} param
			 */
			getUserInfo: function(param) {
				console.log("getUserInfo", )
				if (param.userInfo != undefined || param.userInfo != null) {
					this.userInfo = this.getUniFormatterData(param.userInfo)
					this.initList(this.userInfo.id,this.newCurrent,this.newPagesSize);
					// console.log(this.userInfo)
				} else {
					this.userInfo = null;
				}
			},
			/** 获取矫正人员预警列表
			 * @param {Object} userId
			 */
			initList: function(userId, newCurrent, newPagesSize) {
				getTaskList({
					userId,
					"page": newCurrent,
					"limit": newPagesSize,
					isFinish: ""
				}).then(res => {
					if (res.code == 0 && res.msg == "操作成功") {
						// console.log("getTaskList:", res)
						let eData = res.data.records;
						if (eData.length > 0) {
							this.taskLists = eData;
						} else {
							this.taskLists = [];
						}
						console.log('taskLists', this.taskLists)
					}
				});
			},
			getMoreList: function(userId, newCurrent, newPagesSize) {
				getTaskList({
					userId,
					"page": newCurrent,
					"limit": newPagesSize,
					isFinish: ""
				}).then(res => {
					if (res.code == 0 && res.msg == "操作成功") {
						// console.log("getTaskList:", res)
						let eData = res.data.records;
						if (eData.length > 0) {
							this.taskLists = this.taskLists.concat(eData);
						} else {
							// this.taskLists = [];
							this.status = 'noMore';
						}
						console.log('taskLists', this.taskLists)
					}
				});
			},
			selectListItem: function(selectListItem) {
				uni.navigateTo({
					url: '../checkInTask/checkInTask?selectListItem=' + this.setUniFormatterData(selectListItem),
				})
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
					console.log(ret, 1212)
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
					"clockRadius": 30,
					"taskLongitude": parseFloat(selectItem.taskLongitude),
					"taskLatitude": parseFloat(selectItem.taskLatitude)
				};
				uni.hideLoading();

				console.log(clockInfo)
				uni.navigateTo({
					url: '../faceCheck/faceCheck?clockInfo=' + this.setUniFormatterData(clockInfo)
				})
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
				console.log(123123123);
				// #endif

				console.log(this.clockLatitude)
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
	.uncomplete {
		// background-image: url(@/static/uncomplete.png);
	}

	.complete {
		// background-image: url(@/static/complete.png);
	}
	.noData {
		text-align: center;
		height: 50rpx;
		line-height: 50rpx;
	}
	.item {
		display: flex;
		align-items: center;
		background-color: #fff;
		padding: 20rpx 28rpx;
		margin: 22rpx 18rpx;
		
		image {
			width: 85rpx;
			height: 85rpx;
		}
		.taskState {
			position: absolute;
			right: 10rpx;
			top: 30rpx;
			width: 154rpx;
			height: 140rpx;
			z-index: 10;
		}
		.info {
			display: flex;
			flex-direction: column;
			align-items: start;
			justify-content: center;
			view {
				margin-bottom: 20rpx;
				display: flex;
				align-items: center;
				width: 100%;
			
				&:last-child {
					margin: 0;
				}
			}
			flex: 1;
			font-size: 14px;
			margin-left: 22rpx;
	
			.label {
				color: #999;
			}
			.contentText {
				margin-left: 40rpx;
				color: #333;
				
				display: block;
				width: 40vw;
				overflow: hidden;
				text-overflow:ellipsis;
				white-space: nowrap;
				
			}
		}
	}
</style>
