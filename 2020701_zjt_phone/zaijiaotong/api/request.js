const BASE_URL = getApp().globalData.baseUrl;
const IMAGE_URL = getApp().globalData.baseUrl;
const IMEICODE = getApp().globalData.IMEICODE || '';

const service = (config) => {
	config.url = BASE_URL + config.url;
	if (!config.data) {
		config.data = {};
	}
	if (IMEICODE) {
		config.data['macCode'] = IMEICODE;
	} else {
		config.data['macCode'] = 'testPhone';
	}
	if (getApp().globalData.userInfo) {
		config.data['userId'] = getApp().globalData.userInfo['id'];
	}
	let promise = new Promise(function(resolve, reject) {
		uni.request(config).then(responses => {
			console.log(config,responses)
			// 异常
			if (responses[0]) {
				reject({
					message: "请求错误",
					url: config.url
				});
			} else {
				const response = responses[1].data; // 如果返回的结果是data.data的，嫌麻烦可以用这个，return res,这样只返回一个data
				// 设置向相应拦截器
				if (response.code === 0 || response.code === 404042) {
					resolve(response);
				} else {
					if (response.code === 404040) {
						uni.showToast({
							title: response.msg,
							icon: "none",
							duration: 2000
						})
						uni.reLaunch({
							url: '../login/login',
							success(res) {
								console.log('res', res)
							},
							fail(e) {
								console.error('e', e)
							}
						})
						reject({
							message: response.msg,
							code: response.code,
							url: config.url
						});
					} else {
						uni.showToast({
							title: response.msg,
							icon: "none",
							duration: 2000
						})
						reject({
							message: response.msg,
							code: response.code,
							url: config.url
						});
					}
				}
			}
		}).catch(error => {
			reject(error);
		})
	})
	return promise;

}

export default service
