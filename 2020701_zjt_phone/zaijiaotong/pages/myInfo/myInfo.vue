<template>
	<view>

		<view class="status_bar">
			<!-- 这里是状态栏 -->
		</view>

		<view class="divStatusBar">
			<view>
				<i class="uni-btn-icon" style="color: rgb(255, 255, 255); font-size: 27px;" @click="goHome"></i>
				<text>我的</text>
				<i class="uni-btn-icon" style=" opacity:0;color: rgb(255, 255, 255); font-size: 27px;"></i>
			</view>
		</view>

		<view v-if="userInfo != null" class="userInfo">
			<image :src="userInfo.imageUrl" style="border-radius: 100%;" mode="aspectFill"></image>
			<view class="info">
				<view>
					<text>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</text>
					<text>{{userInfo.userType == 1 ? userInfo.userName : userInfo.name}}</text>
				</view>
				<view>
					<text>联系电话：</text>
					<text>{{userInfo.phone != null ? userInfo.phone.toString().substring(0,3) + "********" : "***********" }}</text>
				</view>
				<view>
					<text>关注类型：</text>
					<text>{{userInfo.userType == 1 ? "司法矫正人员":"司法人员"}}</text>
				</view>
			</view>
			<view class="outBtn">
				<button type="primary" @click="openSignOut">退出当前登录</button>
			</view>
		</view>
		<view v-else>
			<image src="@/static/icon_head.png" mode=""></image>
			<view class="info">
				<view>
					<text>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</text>
					<text>***</text>
				</view>
				<view>
					<text>联系电话：</text>
					<text>***********</text>
				</view>
				<view>
					<text>关注类型：</text>
					<text>******</text>
				</view>
			</view>
			<view class="outBtn">
				<button type="primary" @click="openSignOut">返回登录</button>
			</view>
		</view>
		<view class="footer">
			<text>北京峰盛博远科技股份有限公司</text>
			<text>{{version}}</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				// 版本号
				version: plus.runtime.version,
				versionCode: plus.runtime.versionCode,
				userInfo: null,
			};
		},
		onLoad(param) {
			this.getUserInfo(param);
		},
		methods: {
			/** 获取用户信息
			 * @param {Object} param
			 */
			getUserInfo: function(param) {
				console.log("getUserInfo", )
				if (param.userInfo != undefined || param.userInfo != null) {
					this.userInfo = this.getUniFormatterData(param.userInfo)
					console.log(this.userInfo)
				} else {
					this.userInfo = null;
				}
			},
			openSignOut: function() {
				uni.navigateTo({
					url: '../signOut/signOut',
					// animationType: 'pop-in',
					// animationDuration: 200
				})
			},
			goHome: function() {
				uni.navigateBack({
					delta: 1,
					// animationType: 'pop-out',
					// animationDuration: 200
				})
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
		width: 100%;
		height: 109px;
		background-image: url(@/static/top_bg.png);
		background-repeat: no-repeat;
		background-size: cover;
		background-position-y: 100%;

		i {
			margin: 0 5rpx;
		}

		view {
			padding: 10rpx 3rpx;
			display: flex;
			align-items: start;
		}

		text {
			font-weight: 700;
			font-size: 16px;
			line-height: 30px;
			text-align: center;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
			color: #fff;
			flex: 1;
		}

	}

	.userInfo {
		width: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		position: absolute;
		top: 200rpx;


		image {
			width: 133rpx;
			height: 133rpx;
			margin-bottom: 82rpx;
		}

		.info {
			width: 100%;

			view {
				border-bottom: 1px solid #BBBBBB;
				margin: 0 90rpx;
				padding: 15rpx 0;

				text {
					font-family: PingFang SC;
					font-size: 16px;

					&:first-child {
						color: rgba(153, 153, 153, 1);
					}

					&:last-child {
						color: rgba(51, 51, 51, 1);
					}
				}
			}
		}

		.outBtn {
			margin-top: 216rpx;
			text-align: center;
			font-family: PingFang SC;
			width: 100%;
			color: rgba(255, 255, 255, 1);
			font-size: 18px;

			button {
				margin: 0 50rpx;
				border-radius: 150rpx;
				background-color: #148af0;
				border: 1px solid rgba(255, 255, 255, 0);

			}
		}
	}
</style>
