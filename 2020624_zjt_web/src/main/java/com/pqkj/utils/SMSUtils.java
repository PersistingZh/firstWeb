package com.pqkj.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.MD5Util;
import com.pq.framework.util.RandomGUID;
import com.xsxx.sms.SmsClient;
import com.xsxx.sms.model.SubmitResp;
import net.sf.json.xml.XMLSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by My on 2018/8/31.
 */
@Service
public class SMSUtils {
    public  static String smdurl="http://14.116.158.210:8088/v2sms.aspx";
    public  static String account="朋奇科技";
    public  static String passowrd="pengqi.123";
    public  static  Object[]  smsSendOne (String activityId,String iphone,String type,HashMap<String,String> dataMap) throws Exception {
        String tel=iphone;
        if (iphone.indexOf("+86") != -1) {
            tel = tel.replaceAll("\\+86", "");
        } else if (iphone.indexOf("+852") != -1) {
            tel = tel.replaceAll("\\+852", "");
        } else if (iphone.indexOf("+853") != -1) {
            tel = tel.replaceAll("\\+853", "");
        } else if (iphone.indexOf("+886") != -1) {
            tel = tel.replaceAll("\\+886", "");
        }
        Object[] resultObj = new Object[4];
        resultObj[0] = false;
        resultObj[1] = "发送失败";
        JSONObject obj=new JSONObject();
        String timelong= DateTimeUI.getCurrentDateTimeLong();
        String sign="";
        String content=getSign(activityId)+getMode(type);
        //短信模板
        for(String key:dataMap.keySet())
        {
            content=content.replace(key,dataMap.get(key));
        }
        try {
            sign= MD5Util.getStringMD5(account+passowrd+timelong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cs="userid=160&timestamp="+timelong+"&sign="+sign+"&mobile="+tel+"&sendTime=&action=send&extno=&content="+content;
        String result=HttpUtil.post(smdurl,cs).replace("<?xml version=\"1.0\" encoding=\"utf-8\" ?>","");
        System.out.println(result);
        //xml转json
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSONObject messageObj=JSONObject.parseObject(xmlSerializer.read(result).toString());
        if(messageObj.getString("message").equals("ok")){
            resultObj[0] = true;
            resultObj[1] = "发送成功";
        }
        resultObj[2] = messageObj.get("message");
        resultObj[3] =content;
        return resultObj;
    }
    //获取活动模板
    public  static String  getMode (String type) throws Exception {
        String model="";
        switch (type) {
            case "住宿安排":
                model="尊敬的 param_cyry ：您在 param_activity 中所申请的住宿服务已通过，{param_zxxx}"; break;
            case "信息修正":
                model="尊敬的  param_cyry：您申请参加 param_activity 所填写的个人信息有误 param_cwms，请打开以下链接进行修改。param_link"; break;
            case "领证通知":
                model="尊敬的 param_cyry ：您参加 param_activity 时使用的证件【param_zzlx】已制作完毕 param_lzfs。";break;
            case "调整通知":
                model="「温馨提示」各位领导，同事：根据工作安排，第二届“21世纪海上丝绸之路”中国（广东）国际传播论坛改为9月19日下午5点开幕，20日上午9点开始主论坛，其他不变。请知悉，谢谢";break;
            case "春节大数据":
                model="环珠海防护圈监控:param_content"; break;
        }
        return model;
    }
    //获取活动签名
    public  static String  getSign (String activityId) throws Exception {
        String sign="";
        switch (activityId) {
            case "2f103aa6334545c1837a1b3bf85b98b7":
                sign="【第二届海丝论坛】";break;
            case "f58719a9ad8f45399a7b6fe9f434afd0":
                sign="【第二届海丝论坛】";break;
            default:sign="【SAAS服务平台】";break;
        }
        return sign;
    }

    public  static Object[]   sendDxMessage(String  tel,String dxyzm) { ;
        Object[] resultObj = new Object[4];
        resultObj[0] = false;
        resultObj[1] = "发送失败";
        JSONObject obj=new JSONObject();
        String timelong= DateTimeUI.getCurrentDateTimeLong();
        String sign="";
        String content="【在矫通】 验证码 : "+dxyzm+"。您正在使用短信验证码登录功能。如非本人操作,请忽略。";
        //短信模板
        try {
            sign= MD5Util.getStringMD5(account+passowrd+timelong);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String cs="userid=160&timestamp="+timelong+"&sign="+sign+"&mobile="+tel+"&sendTime=&action=send&extno=&content="+content;
        String result= HttpUtil.post(smdurl,cs).replace("<?xml version=\"1.0\" encoding=\"utf-8\" ?>","");
        System.out.println(result);
        //xml转json
        XMLSerializer xmlSerializer = new XMLSerializer();
        JSONObject messageObj=JSONObject.parseObject(xmlSerializer.read(result).toString());
        if(messageObj.getString("message").equals("ok")){
            resultObj[0] = true;
            resultObj[1] = "发送成功";
        }
        resultObj[2] = messageObj.get("message");
        resultObj[3] =content;
        return  resultObj;
    }

    public  static Object[] sendSmsMessage(String  tel,String dxyzm) { ;
        Object[] resultObj = new Object[4];
        resultObj[0] = false;
        resultObj[1] = "发送失败";
        String content="【在矫通】 验证码 : "+dxyzm+"。您正在使用短信验证码登录功能。如非本人操作,请忽略。";
        SmsClient smsClient = new SmsClient("api.wxxsxx.com", "80", "fsby01hyhy", "jgEB91+~");
        SubmitResp submitResp = smsClient.submit(tel,content);
        if(submitResp.success()){
            System.out.println(JSON.toJSON(submitResp));
            resultObj[0] = true;
            resultObj[1] = "发送成功";
        }else{
            System.out.println(submitResp.getStatus() + submitResp.getMsg());
            resultObj[2] = submitResp.getMsg();
            resultObj[3] =content;
        }
        return  resultObj;
    }
    public static void main(String[] args) {
        SmsClient smsClient = new SmsClient("api.wxxsxx.com", "80", "fsby01hyhy", "jgEB91+~");
        SubmitResp submitResp = smsClient.submit("13824139304","【在矫通】 验证码 :");
        if(submitResp.success()){
            System.out.println(JSON.toJSON(submitResp));
        }else{
            System.out.println(submitResp.getStatus() + submitResp.getMsg());
        }
    }
}
