package com.pqkj.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pqkj.common.FileUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.quartz.HttpUtil;
import com.pqkj.service.AuthService;
import com.pqkj.utils.CompareFaceUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Api(tags = {"人脸识别相关"})
@Slf4j
@Controller
@RequestMapping("/face")
public class CompareFaceController {

    @ApiOperation(value = "人脸识别")
    @PostMapping("compareFace")
    @ResponseBody
    public DataResult compareFace(@RequestBody JSONObject jsonObject) {
        String urlA = jsonObject.getString("urlA");
        String urlB = jsonObject.getString("urlB");
        try {
            return faceMatch(FileUtil.imageToBase64ByLocal(urlA), FileUtil.imageToBase64ByLocal(urlB), AuthService.getAuth());
//            return DataResult.success(CompareFaceUtils.compareFace(urlA,urlB).getJSONObject("data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataResult.fail("系统异常");
    }

    public DataResult faceMatch(String urlABase64, String urlBBase64, String accessToken) throws Exception {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        HashMap<String, Object> urlAMap = new HashMap<>();
        urlAMap.put("image", urlABase64);
        urlAMap.put("image_type", "BASE64");
        HashMap<String, Object> urlBMap = new HashMap<>();
        urlBMap.put("image", urlBBase64);
        urlBMap.put("image_type", "BASE64");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(urlAMap);
        jsonArray.add(urlBMap);
        // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
        System.out.println(jsonArray.toJSONString());
        String result = HttpUtil.post(url, accessToken, "application/json", jsonArray.toJSONString());
        System.out.println(result);
        return DataResult.success(JSONObject.parseObject(result));
    }

}
