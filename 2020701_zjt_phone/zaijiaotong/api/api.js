import request from "./request.js";

/** 登录接口
 * @param {Object} {"phone": "13696382274","verificationCode": "417671"}
 */
export function login(postData) {
	return request({
		url: 'phone/zjtZjry/login',
		method: 'post',
		data: postData
	})
}
/** 获取验证码
 * @param {Object}  {"phone": ""}
 */
export function getPhoneCode(postData) {
	return request({
		url: 'phone/zjtZjry/getVerify',
		method: 'post',
		data: postData
	})
}


/** 获取矫正人员未完成任务数量
 * @param {Object} {"userId":""}
 */
export function getTaskCount(postData) {
	return request({
		url: 'phone/taskResult/taskNum',
		method: 'post',
		data: postData
	})
}

/** 获取矫正人员未完成任务列表
 * @param {Object}  {"userId":""}
 */
export function getTaskList(postData) {
	return request({
		url: 'phone/taskResult/list',
		method: 'post',
		data: postData
	})
}

/** 获取矫正人员预警数量
 * @param {Object} {"userId":""}
 */
export function getWarningCount(postData) {
	return request({
		url: 'phone/warning/count',
		method: 'post',
		data: postData
	})
}
/** 获取矫正人员预警列表  
 * @param {Object} {"userId":""}
 */
export function getWarningList(postData) {
	return request({
		url: 'phone/warning/list',
		method: 'post',
		data: postData
	})
}
/** 打卡 
 * @param {Object} {"clockAddress": "","clockEffective": 0,"clockLatitude": "","clockLongitude": "","id": "","taskRemarks": ""}
 */
export function punchClock(postData) {
	return request({
		url: 'phone/taskResult/punchClock',
		method: 'post',
		data: postData
	})
}



/**
 * 上传文件
 * @export
 * @param {*} files
 * @returns
 */
export function uploadFiles(files) {
	return request({
		url: "mFileUpdate/file_upload",
		method: "post",
		headers: {
			"Content-Type": "multipart/form-data",
		},
		data: files,
	});
};
/**
 * 人脸比对（阿里云）
 * @export
 * @param {*} urlA
 * @param {*} urlB
 * @returns
 */
export function compardFace(urlA, urlB) {
	return request({
		url: "face/compareFace",
		method: "post",
		data: {
			urlA,
			urlB
		},
	});
};
/**
 * 上报人员位置（五分钟调用一次）
 * @export
 * @param {*} postData
 * @returns
 */
export function receivePhoneGPS(postData) {
	return request({
		url: "phone/gps/receivePhoneGPS",
		method: "post",
		data: postData
	});
};

/**
 * 获取线上apk版本
 * @export
 * @param {*} postData
 * @returns
 */
export function getServerApkVersion(postData) {
	return request({
		url: "mFileUpdate/DownloadApk",
		method: "post",
		data: postData
	});
};
