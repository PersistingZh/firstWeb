package com.pqkj.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.com.viapi.FileUtils;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.io.IOException;
import com.aliyuncs.facebody.model.v20191230.*;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.common.utils.DataResult;

public class CompareFaceUtils {

    private static final String ACCESS_KEY_ID = "LTAICnNNLmmSoG9A";
    private static final String SECRET = "yazmUJICa58V4iK0oYUmsyeFaRklZ9";
    public static String uploadFile(String filePath) throws ClientException, IOException {
        //String file = /home/admin/file/1.jpg  或者本地上传
        FileUtils fileUtils = FileUtils.getInstance(ACCESS_KEY_ID,SECRET);
        String ossurl = fileUtils.upload(filePath);
        System.out.println(ossurl);
        return ossurl;
    }
    public static JSONObject compareFace(String urlA,String urlB) throws IOException, ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", ACCESS_KEY_ID, SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        String imageURLA = uploadFile(urlA);
        String imageURLB = uploadFile(urlB);
        CompareFaceRequest request = new CompareFaceRequest();
        request.setRegionId("cn-shanghai");
        request.setImageURLA(imageURLA);
        request.setImageURLB(imageURLB);
        try {
            CompareFaceResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
            return JSONObject.parseObject(JSONObject.toJSONString(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            throw new BusinessException(BaseResponseCode.NOT_PHOTO);
        }
        return new JSONObject();
    }
    public static void main(String[] args) throws IOException, ClientException {
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", ACCESS_KEY_ID, SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        String imageURLA = uploadFile("http://120.77.46.205:83/compareFace/dilireba1.jpg");
        String imageURLB = uploadFile("http://120.77.46.205:83/compareFace/dilireba2.jpg");
        CompareFaceRequest request = new CompareFaceRequest();
        request.setRegionId("cn-shanghai");
        request.setImageURLA(imageURLA);
        request.setImageURLB(imageURLB);
        try {
            CompareFaceResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }
}
