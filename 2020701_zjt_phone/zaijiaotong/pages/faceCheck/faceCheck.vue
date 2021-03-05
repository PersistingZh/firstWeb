<template>
  <view></view>
</template>

<script>
import { uploadFiles, compardFace } from '@/api/api'

const modal = uni.requireNativePlugin('modal')
let lyBDFaceAuth

export default {
  data() {
    return {
      imgBase64Str: null,
      resultStr: {},
      //
      clockInfo: null,
    }
  },
  onLoad(param) {
    // #ifdef APP-PLUS
    if (uni.getSystemInfoSync().platform == 'ios' || uni.getSystemInfoSync().platform == 'android') {
      lyBDFaceAuth = uni.requireNativePlugin('longyoung-BDFaceAuth')
    }
    this.getUserInfo(param)
    //引用插件
    // #endif
  },
  methods: {
    /** 获取上一页参数
     * @param {Object} param
     */
    getUserInfo: function (param) {
      this.clockInfo = this.getUniFormatterData(param.clockInfo)
      if (this.clockInfo != undefined || this.clockInfo != null) {
        this.clockInfo = this.getUniFormatterData(param.clockInfo)
        this.onScanFace()
      } else {
        this.clockInfo = null
      }
      // modal.toast({
      // 	message: this.clockInfo,
      // 	duration: 30
      // });
    },
    openMap: function () {
      uni.redirectTo({
        url: '../map/map?clockInfo=' + this.setUniFormatterData(this.clockInfo),
      })
    },
    // 传参加码
    setUniFormatterData: function (n) {
      return encodeURIComponent(JSON.stringify(n))
    }, // 传参解码
    getUniFormatterData: function (n) {
      return JSON.parse(decodeURIComponent(n))
    },
    onScanFace: function (param) {
      let _this = this
      uni.showLoading({
        title: '采集人像进行比对中',
        mask: true,
      })
      console.log('lygg.onScanFace')
      // iOS License ID: testface1111-face-ios
      // 安卓 License ID: testface1111-face-android
      // 活体识别动作
      const action = [
        'Eye', // 眨眼
        'Mouth', // 张嘴
        'HeadLeft', // 头向左转
        'HeadRight', // 头向右转
        'HeadLeftOrRight', // 左右摇头
        'HeadUp', // 抬头
        'HeadDown', // 低头
      ]

      const actionAry = []
      // 随机插入活体检测
      // for (let index = 0; index < 2; index++) {
      // 	const n = this.getRandomInt(0, action.length)
      // 	console.log(n, 123)
      // 	actionAry.push(action[n])
      // }
      // console.log(actionAry)

      // #ifdef APP-PLUS
      lyBDFaceAuth.scanFace(
        {
          licenseID: 'testface1111-face-android', //必须与百度授权资料一致，注意安卓和 iOS 不一样。
          actionAry, //不传无动作
          surfaceRatio: 1,
          isLivenessRandom: 1, //不传默认有序，0有序，1随机
          isSound: 0, //不传默认有声音，0无声，1有声
          frontBack: 0, //不传默认前置，0前置，1后置
          txtColor: '#3987FD', //文字颜色
          bgColor: '#70000000', //背景颜色，iOS设置无效，需要换图片facecover_new.png，路径 nativeplugins\longyoung-BDFaceAuth-iOS\ios\com.baidu.idl.face.faceSDK.bundle，具体看示例。
          roundColor: '#3987FD', //圆的颜色
        },
        (result) => {
          if (result.bestImgBase64) {
            this.bestImgBase64Str = result.bestImgBase64

            this.resultStr = this.resultStr + '\n\n======base64字符串（太长，截取前200字符）：\n' + result.bestImgBase64.substring(0, 200)
            delete result.bestImgBase64 //删除bestImgBase64
          }
          this.resultStr = this.resultStr + '\n======不包含图片部分：\n' + JSON.stringify(result)

          //status=0 且 err_code=0，为检测超时回调，仅安卓有效，iOS 无超时的说法。此处举例做关闭页面的操作，你可以做自己的逻辑。
          if (result.status == 0) {
            if (result.err_code == 0) {
              lyBDFaceAuth.closeAty({}, (result) => {
                uni.hideLoading()
                // console.log(
                // 	"result.closeAt bestImgBase64Stry=" + JSON.stringify(result)
                // );
              })
            }
          } else if (result.status == 2) {
            uni.hideLoading()
            uni.navigateBack({
              delta: 1,
            })
            // uni.showModal({
            // 	title: "用户取消操作",
            // 	icon:"none"
            // });
          }

          // ***使用 uni.uploadFile()上传服务器，不需要将图片上传到服务器的，注释此处代码。
          // https://ask.dcloud.net.cn/question/30546, https://ask.dcloud.net.cn/question/76827
          // http://www.html5plus.org/doc/zh_cn/io.html#plus.io.URLType, https://ask.dcloud.net.cn/article/94
          if (this.bestImgBase64Str) {
            var bitmapT = new plus.nativeObj.Bitmap('test')
            //加载base64图片
            bitmapT.loadBase64Data(this.bestImgBase64Str, (res) => {
              console.log('longyoung.loadBase64Data.suc=' + JSON.stringify(res))
              var options = {
                overwrite: true, //覆盖同名图片
                format: 'png',
                quality: 50,
                //去左右黑边
                clip: {
                  width: '60%',
                  left: '20%', //(100%-60%)/2
                  height: '100%',
                  top: '0%',
                },
                //截取头部
                // clip: {
                // 	width: "60%",
                // 	left: "20%", //(100%-60%)/2
                // 	height: "66%",
                // 	top: "9%"
                // }
              }
              //保存base64图片，请不要私自改变 _doc/ 这个头，除非你明确的知道 Bitmap.save() 的用法。
              bitmapT.save(
                '_doc/face.jpg',
                options,
                (res) => {
                  bitmapT.clear() //销毁bitmap对象
                  console.log('longyoung.save.suc=' + JSON.stringify(res))

                  this.imgBase64Str = res.target

                  //图片上传服务器
                  uni.uploadFile({
                    url: getApp().globalData.baseUrl + 'mFileUpdate/file_upload?path=face',
                    filePath: res.target,
                    method: 'post',
                    name: 'file',
                    header: {
                      'Content-Type': 'multipart/form-data',
                    },
                    success: (res) => {
                      const requestData = JSON.parse(res.data)
                      const { data } = requestData
                      console.log(data)
                      console.log(data.filePath)
                      // 请求比对结果
                      const postData = {
                        urlA: data.filePath, // 现场抓拍图
                        urlB: getApp().globalData.userInfo.photoUrl, // 现场抓拍图
                        urlD: 'd:/data/zjt/files/face/202007032115-b3e52bafeaaf4d11a2b7e7814334bfb5.jpg', // 现场抓拍图
                        urlC: 'd:/data/zjt/files/face/202007031233-a69323ed70f84bf09a652affafe9f471.jpg', // 服务器图库存的图
                      }
                      compardFace(postData.urlA, postData.urlB)
                        .then((res) => {
                          console.log('比对结果', res)
                          if (res.code == 0) {
                            const { data } = res
                            if (data.error_code == 0) {
                              if (Math.round(data.result.score) > 60) {
                                // if (Math.round(data.confidence) > 61) {
                                console.log(123)
                                uni.hideLoading()
                                uni.showToast({
                                  title: '人脸比对成功',
                                  duration: 2000,
                                })
                                this.openMap()
                              } else {
                                console.log(1231231312)
                                uni.hideLoading()
                                uni.showToast({
                                  title: '人脸比对失败，请重试！',
                                  duration: 2000,
                                })
                                setTimeout(function () {
                                  uni.navigateBack({
                                    delta: 1,
                                  })
                                }, 1000)
                              }
                            } else {
                              uni.hideLoading()
                              uni.showToast({
                                title: '人脸比对失败，请重试！',
                                duration: 2000,
                              })
                              setTimeout(function () {
                                uni.navigateBack({
                                  delta: 1,
                                })
                              }, 1000)
                            }
                          } else {
                            uni.hideLoading()
                            uni.showToast({
                              title: '人脸比对调用失败',
                              duration: 2000,
                            })
                            uni.navigateBack({
                              delta: 1,
                            })
                          }
                        })
                        .catch((e) => {
                          uni.hideLoading()
                          consoe.error(e)
                        })
                    },
                    fail: (res) => {
                      console.log('longyoung.uploadFile.fail=', res)
                      uni.hideLoading()
                      uni.showToast({
                        title: '图片上传错误',
                        icon: 'none',
                      })
                    },
                  })
                },
                function (res) {
                  console.log('longyoung.save.fail=', res)
                  uni.hideLoading()
                  uni.showModal({
                    title: 'bitmap保存错误',
                    content: JSON.stringify(res),
                  })
                }
              )
            })
          }
          // ***使用 uni.uploadFile()上传服务器，不需要将图片上传到服务器的，注释此处代码。
        },
        function () {
          cosole.log('取消人脸操作')
          uni.hideLoading()
          uni.showToast({
            title: '用户取消操作',
            icon: 'none',
            duration: 2000,
          })
          uni.navigateBack({
            delta: 1,
          })
        }
      )
      // #endif
      // 测试
      // #ifdef H5
      this.openMap()
      // #endif
    },
    getRandomInt: function (min, max) {
      min = Math.ceil(min)
      max = Math.floor(max)
      return Math.floor(Math.random() * (max - min)) + min //不含最大值，含最小值
    },
  },
}
</script>

<style lang="scss" scoped>
image {
  position: absolute;
  width: 100%;
  height: 100%;
}

button {
  position: absolute;
  bottom: 60rpx;
  border-radius: 150rpx;
  background-color: rgba(19, 149, 242, 1);
  color: rgba(255, 255, 255, 1);
  font-size: 18px;
  text-align: center;
  font-family: Roboto;
  width: 100%;
  border: 1px solid rgba(255, 255, 255, 0);
}
</style>
