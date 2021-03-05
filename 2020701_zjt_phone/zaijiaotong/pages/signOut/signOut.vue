<template>
	<view>
		<text>是否退出当前登录？</text>
		<view class="btns">
			<button type="default">取消</button>
			<button type="primary" @click="loginOut">确认退出</button>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		},
		methods:{
			loginOut:function(){
				uni.redirectTo({
					url:"../login/login"
				})
			}
		}
	}
</script>

<style lang="scss">
	text{
		color: rgba(153, 153, 153, 1);
		width: 100%;
		display: block;
		font-size: 20px;
		text-align: center;
		margin: 287rpx 0;
	}
	.btns {
		display: flex;
		align-items: center;

		button {
			width: 323rpx;
			font-size: 18px;
			text-align: center;
			font-family: PingFang SC;
			border-radius: 160rpx;

			&:first-child {
				background-color: rgba(255, 255, 255, 1);
				color: rgba(19, 149, 242, 1);
				border: 1px solid rgba(19, 149, 242, 1);
			}
		}
	}
</style>
