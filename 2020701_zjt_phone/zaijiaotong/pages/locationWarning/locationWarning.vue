<template>
	<view>
		<template>
			<view v-if="warningList.length>0">
				<view v-for="item in warningList" :key="item.id" class="item itemBaseStyle">
					<image src="../../static/icon_yujing.png" mode="aspectFill"></image>
					<view class="info">
						<view>
							<text class="label">警告内容</text>
							<text class="contentText">{{item.warningContent == null ? "暂无内容":item.warningContent}}</text>
						</view>
						<view>
							<text class="label">警告时间</text>
							<text class="contentText">{{item.createTime}}</text>
						</view>
					</view>
					<uni-icons type="arrowright" size="18" color="#D9D9D9"></uni-icons>
				</view>
				<uni-load-more :status="status" :show-icon="true" v-if="showLoadMore"></uni-load-more>
			</view>
			<view v-else>
				<!-- <view class="item itemBaseStyle">
					<image src="../../static/icon_yujing.png" mode="aspectFill"></image>
					<view class="info">
						<view>
							<text class="label">警告内容</text>
							<text class="contentText">暂无警告内容</text>
						</view>
						<view>
							<text class="label">警告时间</text>
							<text class="contentText">暂无警告时间</text>
						</view>
					</view>
					<uni-icons type="arrowright" size="18" color="#D9D9D9"></uni-icons>
				</view> -->
				<view class="noData">
					<text>暂无数据</text>
				</view>
			</view>
		</template>
	</view>
</template>

<script>
	import uniLoadMore from "@/components/uni-load-more/uni-load-more.vue"

	import {
		getWarningList
	} from "@/api/api.js"
	export default {
		components: {
			uniLoadMore
		},
		data() {
			return {
				userInfo: null,
				warningList: [],
				// 下拉刷新
				status: 'more',
				showLoadMore: true,
				loadMoreText: "加载中...",
				newCurrent: 1,
				newPagesSize: 10
			};
		},
		onLoad(param) {
			this.warningList = [];
			this.status = "more";
			this.showLoadMore = false;
			this.newCurrent = 1;
			this.newPagesSize = 10;
			this.getUserInfo(param);
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
		mounted() {},
		methods: {
			/** 获取用户信息
			 * @param {Object} param
			 */
			getUserInfo: function(param) {
				console.log("getUserInfo", )
				if (param.userInfo != undefined || param.userInfo != null) {
					this.userInfo = this.getUniFormatterData(param.userInfo)
					this.initList(this.userInfo.id, this.newCurrent, this.newPagesSize);
					// console.log(this.userInfo)
				} else {
					this.userInfo = null;
				}
			},
			/** 初始用户任务列表
			 * @param {Object} userId
			 */
			initList: function(userId, newCurrent, newPagesSize) {
				var param = {
					userId: userId,
					"page": newCurrent,
					"limit": newPagesSize,
				};
				console.log("getWarningList param:", param)
				getWarningList(param).then(res => {
					if (res.code == 0 && res.msg == "操作成功") {
						console.log("getWarningList:", res)
						let eData = res.data.records;
						if (eData.length > 0) {
							this.warningList = eData;
						} else {
							this.warningList = [];
							_this.status = 'noMore';
						}
					}
				});
			},
			getMoreList: function(userId, newCurrent, newPagesSize) {
				var param = {
					userId: userId,
					"page": newCurrent,
					"limit": newPagesSize,
				};
				getWarningList(param).then(res => {
					if (res.code == 0 && res.msg == "操作成功") {
						console.log("getMoreList:", res)
						let eData = res.data.records;
						if (eData.length > 0) {
							this.warningList = this.warningList.concat(eData);
						} else {
							// this.warningList = [];
							this.status = 'noMore';
						}
					}
				});
			},
			// getWarningList: function() {
			// 	for (var i = 0; i < 30; i++) {
			// 		const n = {
			// 			icon: 'icon_yujing.png',
			// 			content: '超出有效活动范围',
			// 			time: '2020-06-22  17:58:55'
			// 		}
			// 		this.warningList.push(n)
			// 	}
			// 	// console.log(this.warningList)
			// },
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

		.info {
			flex: 1;
			font-size: 14px;
			margin-left: 22rpx;

			.label {
				color: #999;
			}

			.contentText {
				margin-left: 40rpx;
				color: #333;
			}
		}
	}
</style>
