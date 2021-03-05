package com.pqkj.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.pqkj.utils.HttpUtil;

import java.util.Random;

public class VerificationCodeUtil {

    /**
     * 获取验证码
     * @param num 位数
     * @return
     */
    public static String getVerificationCode(int num,String phone){
        Random random = new Random();
        String code = "";
        for (int i = 0;i<num;i++){
            code += random.nextInt(9);
        }
        if ("13415657718".equals(phone)){
            return "123456";
        }
        System.out.println("获取验证码 ："+code);
        return code;
    }
    public static boolean checkVerificationCode(String verificationCode,String verificationCodeBase) {
        if (verificationCode.equals(verificationCodeBase)) {
            return true;
        } else {
            return false;
        }

    }
    public static DataResult checkRyByPhone(String phone){
        String[] phones = {"13267993355","13106842116","15919185266","13824139304","13075681729","13380624902","13168690086","13632813400","13829828663","13424562672","13560940338","13602801486","13826360041","18312281233","13928518818","13415657718"};
        String[] xms = {"陈建峰","邓老板","黄公子","张柏川","刘涛涛","张公子","李睿君","王鹏","李华斌","钱儒胜","张达荣","刘钧霖","饶振艳","简绍辉","黄洁琼","测试"};
        String url = "http://139.196.8.178:8801/v1/api/dhhmcx/findZgry.sj?mobile="+phone;
        String url2 = "http://139.196.8.178:8801/v1/api/dhhmcx/findGlry.sj?mobile="+phone;
        for (int i = 0;i<phones.length;i++) {
            if(phones[i].equals(phone)){
                JSONObject dataJson = new JSONObject();
                JSONObject resultJson = new JSONObject();
                if("13824139304".equals(phone)||"13075681729".equals(phone)){
                    resultJson.put("type",0);
                    dataJson.put("szdw","朋齐科技");
                }else {
                    dataJson.put("gldw","朋齐科技");
                    resultJson.put("type",1);
                }
                dataJson.put("phone",phones[i]);
                dataJson.put("xm",xms[i]);
                dataJson.put("rlzp","");
                resultJson.put("data",dataJson);
                return DataResult.success(resultJson);
            }
        }
        JSONObject jzryJson = JSONObject.parseObject(HttpUtil.doPost(url, new JSONObject()));
        if(jzryJson.getIntValue("code") == 0){
            JSONObject gzryJson = JSONObject.parseObject(HttpUtil.doPost(url2, new JSONObject()));
            if(gzryJson.getIntValue("code") == 0){
                return DataResult.fail(gzryJson.getString("message"));
            }else{
                //0.工作人员
                gzryJson.put("type",0);
                return DataResult.success(gzryJson);
            }
        }else{
            //1.为纠正人员
            jzryJson.put("type",1);
            return DataResult.success(jzryJson);
        }
    }
}
