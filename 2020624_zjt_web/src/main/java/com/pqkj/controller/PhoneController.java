package com.pqkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.FileProperties;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.common.utils.DataResult;
import com.pqkj.common.utils.VerificationCodeUtil;
import com.pqkj.entity.*;
import com.pqkj.service.*;
import com.pqkj.utils.SMSUtils;
import com.pqkj.vo.req.PunchClockReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Api(tags = {"手机端接口"})
@Slf4j
@Controller
@RequestMapping("/phone")
public class PhoneController {
    @Resource
    private ITaskResultViewService taskResultViewService;
    @Resource
    private IZjtTaskUserRecordService zjtTaskUserRecordService;
    @Resource
    private IWarningRecordViewService warningRecordViewService;
    @Resource
    private IZjtZjryService zjtZjryService;
    @Resource
    private IZjtTaskResultService zjtTaskResultService;
    @Resource
    private IZjtGzryService zjtGzryService;
    @Resource
    private ILoginCacheService loginCacheService;
    @Resource
    private IWarningUserRelateViewService warningUserRelateViewService;

    @ApiOperation(value = "APP登录")
    @PostMapping("zjtZjry/login")
    @ResponseBody
    public DataResult login(@RequestBody JSONObject jsonObject){
        int type = jsonObject.getIntValue("type");
        String phone = jsonObject.getString("phone");
        String verificationCode = jsonObject.getString("verificationCode");
        String macCode = jsonObject.getString("macCode");
        QueryWrapper loginCacheWrapper = new QueryWrapper();
        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("type",type);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone",phone);
        queryWrapper.eq("is_del",0);
        if (StringUtil.isNullorEmpty(verificationCode)){
            throw new BusinessException(BaseResponseCode.NO_VERIFICATION_CODE);
        }
        if(type == 0){
            ZjtGzry gzry = zjtGzryService.getOne(queryWrapper);
            if (gzry.getIsVerification()==1||StringUtil.isNullorEmpty(gzry.getVerificationCode())){
                throw new BusinessException(BaseResponseCode.NOT_VERIFICATION_CODE);
            }
            boolean checkVerificationCode = VerificationCodeUtil.checkVerificationCode(gzry.getVerificationCode(), verificationCode);
            if(!checkVerificationCode){
                throw new BusinessException(BaseResponseCode.NOT_CHECK_VERIFICATION_CODE);
            }else{
                gzry.setVerificationTime(DateTimeUI.getCurrentDateTime());
                gzry.setIsVerification(1);
                zjtGzryService.updateById(gzry);
                resultMap.put("obj",gzry);
                loginCacheWrapper.eq("user_id",gzry.getId());
                LoginCache loginCache = loginCacheService.getOne(loginCacheWrapper);
                if(loginCache == null){
                    loginCache = new LoginCache();
                    loginCache.setMacCode(macCode);
                    loginCache.setCreateTime(DateTimeUI.getCurrentDateTime());
                    loginCache.setLoginTime(DateTimeUI.getCurrentDateTime());
                    loginCache.setId(RandomGUID.getDatetUUID());
                    loginCache.setOnline(1);
                    loginCache.setUserId(gzry.getId());
                    loginCacheService.save(loginCache);
                }else{
                    loginCache.setMacCode(macCode);
                    loginCache.setLoginTime(DateTimeUI.getCurrentDateTime());
                    loginCacheService.updateById(loginCache);
                }
                return DataResult.success(resultMap);
            }
        }else if (type == 1){
            ZjtZjry zjry = zjtZjryService.getOne(queryWrapper);
            if ("1".equals(zjry.getIsVerification())||StringUtil.isNullorEmpty(zjry.getVerificationCode())){
                throw new BusinessException(BaseResponseCode.NOT_VERIFICATION_CODE);
            }
            boolean checkVerificationCode = VerificationCodeUtil.checkVerificationCode(zjry.getVerificationCode(), verificationCode);
            if(!checkVerificationCode){
                throw new BusinessException(BaseResponseCode.NOT_CHECK_VERIFICATION_CODE);
            }else{
                zjry.setVerificationTime(DateTimeUI.getCurrentDateTime());
                zjry.setIsVerification("1");
                zjry.setIsOnline(1);
                zjry.setLastOnlineTime(DateTimeUI.getCurrentDateTime());
                zjtZjryService.updateById(zjry);
                resultMap.put("obj",zjry);
                loginCacheWrapper.eq("user_id",zjry.getId());
                LoginCache loginCache = loginCacheService.getOne(loginCacheWrapper);
                if(loginCache == null){
                    loginCache = new LoginCache();
                    loginCache.setMacCode(macCode);
                    loginCache.setCreateTime(DateTimeUI.getCurrentDateTime());
                    loginCache.setLoginTime(DateTimeUI.getCurrentDateTime());
                    loginCache.setId(RandomGUID.getDatetUUID());
                    loginCache.setOnline(1);
                    loginCache.setUserId(zjry.getId());
                    loginCacheService.save(loginCache);
                }else{
                    loginCache.setMacCode(macCode);
                    loginCache.setLoginTime(DateTimeUI.getCurrentDateTime());
                    loginCacheService.updateById(loginCache);
                }
                return DataResult.success(resultMap);
            }
        }else{
            return DataResult.fail("type不能为空");
        }
    }

    @ApiOperation(value = "获取APP验证码")
    @PostMapping("zjtZjry/getVerify")
    @ResponseBody
    public DataResult getVerify(@RequestBody @ApiParam("{\"phone\":\"手机号码\"}") JSONObject jsonObject){
        String phone = jsonObject.getString("phone");
        String verificationCode = VerificationCodeUtil.getVerificationCode(6,phone);
        DataResult dataResult = VerificationCodeUtil.checkRyByPhone(phone);
        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("verificationCode",verificationCode);
        if(dataResult.getCode()==0){
            JSONObject resultRy = JSONObject.parseObject(JSONObject.toJSONString(dataResult.getData()));
            JSONObject data = resultRy.getJSONObject("data");
            resultMap.put("type",resultRy.getIntValue("type"));
            if (resultRy.getIntValue("type") == 0){
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("phone",phone);
                queryWrapper.eq("is_del",0);
                ZjtGzry gzry = zjtGzryService.getOne(queryWrapper);
                if(gzry == null){
                    gzry = new ZjtGzry();
                    gzry.setCreateTime(DateTimeUI.getCurrentDateTime());
                    gzry.setId(RandomGUID.getDatetUUID());
                    gzry.setIsDel(0);
                    gzry.setUpdateTime(DateTimeUI.getCurrentDateTime());
                    gzry.setIsVerification(0);
                    gzry.setVerificationCode(verificationCode);
                    gzry.setVerificationCodeTime(DateTimeUI.getCurrentDateTime());
                    gzry.setUnits(data.getString("szdw"));
                    gzry.setName(data.getString("xm"));
                    gzry.setPassword("123456");
                    gzry.setPhone(phone);
                    zjtGzryService.save(gzry);
                }else {
                    gzry.setUpdateTime(DateTimeUI.getCurrentDateTime());
                    gzry.setIsVerification(0);
                    gzry.setVerificationCode(verificationCode);
                    gzry.setVerificationCodeTime(DateTimeUI.getCurrentDateTime());
                    zjtGzryService.updateById(gzry);
                }
            }else{
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("phone",jsonObject.getString("phone"));
                queryWrapper.eq("is_del",0);
                ZjtZjry zjry = zjtZjryService.getOne(queryWrapper);
                if(zjry == null){
                    zjry = new ZjtZjry();
                    zjry.setCreateTime(DateTimeUI.getCurrentDateTime());
                    zjry.setId(RandomGUID.getDatetUUID());
                    zjry.setIsDel(0);
                    zjry.setUpdateTime(DateTimeUI.getCurrentDateTime());
                    zjry.setIsVerification("0");
                    zjry.setVerificationCode(verificationCode);
                    zjry.setVerificationCodeTime(DateTimeUI.getCurrentDateTime());
                    zjry.setPassword("123456");
                    zjry.setPhone(phone);
                    zjry.setGldw(data.getString("gldw"));
                    zjry.setUserName(data.getString("xm"));
                    String imagePath = "/home/java/zjtWeb/files/"+FileProperties.getValue("jzryImge")+phone+".jpg";
                    if(!StringUtil.isNullorEmpty(data.getString("rlzp"))){
                        generateImage(data.getString("rlzp"),imagePath);
                        zjry.setPhotoUrl(imagePath);
                    }
                    zjtZjryService.save(zjry);
                }else{
                    zjry.setIsVerification("0");
                    zjry.setUpdateTime(DateTimeUI.getCurrentDateTime());
                    zjry.setVerificationCodeTime(DateTimeUI.getCurrentDateTime());
                    zjry.setVerificationCode(verificationCode);
                    zjtZjryService.updateById(zjry);
                }
            }
        }else{
            return dataResult;
        }
        Object[] objects = SMSUtils.sendSmsMessage(phone, verificationCode);
        resultMap.put("smsMsg",objects[1]);
        if((boolean)objects[0]){
            return DataResult.success(resultMap);
        }else{
            return DataResult.success(resultMap);
        }
    }
    @ApiOperation(value = "根据矫正人员ID查询任务")
    @PostMapping("taskResult/list")
    @ResponseBody
    public DataResult list(@RequestBody TaskResultView taskResultView){
        boolean b = loginCacheService.checkMacCode(taskResultView.getUserId(), taskResultView.getMacCode());
        if(!b){
            throw new BusinessException(BaseResponseCode.NOT_CHECK_MAC_CODE);
        }
        Page<TaskResultView> page = new Page(taskResultView.getPage(),taskResultView.getLimit());
        QueryWrapper<TaskResultView> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",taskResultView.getUserId());
        if(!StringUtil.isNullorEmpty(taskResultView.getIsFinish())){
            queryWrapper.eq("is_finish",taskResultView.getIsFinish());
        }
        queryWrapper.orderByAsc("is_finish","start_time");
        Page<TaskResultView> list = taskResultViewService.page(page,queryWrapper);
        return DataResult.success(list);
    }
    @ApiOperation(value = "获取矫正人员未完成任务数量")
    @PostMapping("taskResult/taskNum")
    @ResponseBody
    public DataResult taskNum(@RequestBody TaskResultView taskResultView){
        boolean b = loginCacheService.checkMacCode(taskResultView.getUserId(), taskResultView.getMacCode());
        if(!b){
            throw new BusinessException(BaseResponseCode.NOT_CHECK_MAC_CODE);
        }
        QueryWrapper<TaskResultView> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",taskResultView.getUserId());
        queryWrapper.eq("is_finish",0);
        int count = taskResultViewService.count(queryWrapper);
        return DataResult.success(count);
    }
    @ApiOperation(value = "位置预警列表")
    @PostMapping("warning/list")
    @ResponseBody
    public DataResult warningList(@RequestBody ZjtWarning zjtWarning){
        Page page = new Page(zjtWarning.getPage(),zjtWarning.getLimit());
        boolean b = loginCacheService.checkMacCode(zjtWarning.getUserId(), zjtWarning.getMacCode());
        if(!b){
            throw new BusinessException(BaseResponseCode.NOT_CHECK_MAC_CODE);
        }
        QueryWrapper<WarningRecordView> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",zjtWarning.getUserId());
        Page pageList = warningRecordViewService.page(page, queryWrapper);
        return DataResult.success(pageList);
    }
    @ApiOperation(value = "位置预警列表数量")
    @PostMapping("warning/count")
    @ResponseBody
    public DataResult warningCount(@RequestBody ZjtWarning zjtWarning){
        boolean b = loginCacheService.checkMacCode(zjtWarning.getUserId(), zjtWarning.getMacCode());
        if(!b){
            throw new BusinessException(BaseResponseCode.NOT_CHECK_MAC_CODE);
        }
        QueryWrapper<WarningRecordView> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",zjtWarning.getUserId());
        int count = warningRecordViewService.count(queryWrapper);
        return DataResult.success(count);
    }

    @ApiOperation(value = "打卡")
    @PostMapping("taskResult/punchClock")
    @ResponseBody
    public DataResult punchClock(@RequestBody PunchClockReqVO vo){
        boolean b = loginCacheService.checkMacCode(vo.getUserId(), vo.getMacCode());
        if(!b){
            throw new BusinessException(BaseResponseCode.NOT_CHECK_MAC_CODE);
        }
        ZjtTaskResult zjtTaskResult = zjtTaskResultService.getById(vo.getId());
        BeanUtils.copyProperties(vo,zjtTaskResult);
        zjtTaskResult.setIsFinish(1);
        zjtTaskResult.setFinishTime(DateTimeUI.getCurrentDateTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (new Date().before(sdf.parse(zjtTaskResult.getEndTime()))){
                zjtTaskResult.setClockEffective(0);
            }else{
                zjtTaskResult.setClockEffective(1);
            }
            ZjtTaskUserRecord zjtTaskUserRecord = new ZjtTaskUserRecord();
            zjtTaskUserRecord.setTaskId(zjtTaskResult.getTaskId());
            zjtTaskUserRecord.setTaskRemarks(vo.getTaskRemarks());
            zjtTaskUserRecord.setTaskState(0);
            zjtTaskUserRecord.setUserId(zjtTaskResult.getUserId());
            zjtTaskUserRecordService.saveTaskUserRecord(zjtTaskUserRecord);
            zjtTaskResultService.updateById(zjtTaskResult);
            if (new Date().before(sdf.parse(zjtTaskResult.getEndTime()))){
                return DataResult.success("打卡成功");
            }else{
                throw new BusinessException(BaseResponseCode.CLOCK_DELAY);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DataResult.fail("打卡失败");
    }
    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null) return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
