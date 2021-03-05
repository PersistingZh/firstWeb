package com.pqkj.utils;

import com.alibaba.fastjson.JSONObject;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtils {

    public static void main(String[] args) throws IOException {
        String longitude = "113.314282";
        String latitude = "23.395352";
        String url = "http://api.map.baidu.com/ag/coord/convert?from=4&to=0&x="+longitude+"&y="+latitude;
        String post = HttpUtil.doPost(url, new JSONObject());
        JSONObject resultJson = JSONObject.parseObject(post);
        String x = resultJson.getString("x");
        String y = resultJson.getString("y");
        // BASE64解密
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytesX = decoder.decodeBuffer(x);
        byte[] bytesY = decoder.decodeBuffer(y);
        System.out.println("BASE64解密X：" + new String(bytesX));
        System.out.println("BASE64解密Y：" + new String(bytesY));
    }
}
