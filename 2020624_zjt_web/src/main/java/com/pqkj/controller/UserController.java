package com.pqkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.FileProperties;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.utils.*;
import com.pqkj.entity.ZjtGzry;
import com.pqkj.entity.ZjtZjry;
import com.pqkj.service.RoleService;
import com.pqkj.service.UserRoleService;
import com.pqkj.utils.SMSUtils;
import com.pqkj.vo.req.*;
import com.pqkj.vo.resp.LoginRespVO;
import com.pqkj.vo.resp.UserOwnRoleRespVO;
import com.pqkj.common.aop.annotation.LogAnnotation;
import com.pqkj.entity.SysUser;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "组织模块-用户管理")
@RequestMapping("/sys")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @PostMapping(value = "/user/login")
    @ApiOperation(value = "用户登录接口")
    public DataResult<LoginRespVO> login(@RequestBody @Valid LoginReqVO vo) {
        DataResult<LoginRespVO> result = DataResult.success();
        result.setData(userService.login(vo));
        return result;
    }

    @PostMapping("/user/register")
    @ApiOperation(value = "用户注册接口")
    public DataResult<String> register(@RequestBody @Valid RegisterReqVO vo) {
        DataResult<String> result = DataResult.success();
        result.setData(userService.register(vo));
        return result;
    }


    @GetMapping("/user/token")
    @ApiOperation(value = "用户刷新token接口")
    @LogAnnotation(title = "用户管理", action = "用户刷新token")
    public DataResult<String> refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        DataResult<String> result = DataResult.success();
        result.setData(userService.refreshToken(refreshToken, accessToken));
        return result;
    }

    @GetMapping("/user/unLogin")
    @ApiOperation(value = "引导客户端去登录")
    public DataResult unLogin() {
        DataResult result = DataResult.getResult(BaseResponseCode.TOKEN_ERROR);
        return result;
    }

    @PutMapping("/user")
    @ApiOperation(value = "更新用户信息接口")
    @LogAnnotation(title = "用户管理", action = "更新用户信息")
    @RequiresPermissions("sys:user:update")
    public DataResult updateUserInfo(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        userService.updateUserInfo(vo, userId);
        return DataResult.success();
    }

    @PutMapping("/user/info")
    @ApiOperation(value = "更新用户信息接口")
    @LogAnnotation(title = "用户管理", action = "更新用户信息")
    public DataResult updateUserInfoById(@RequestBody @Valid UserUpdateReqVO vo, HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        vo.setId(userId);
        userService.updateUserInfo(vo, userId);
        return DataResult.success();
    }

    @GetMapping("/user/{id}")
    @ApiOperation(value = "查询用户详情接口")
    @LogAnnotation(title = "用户管理", action = "查询用户详情")
    @RequiresPermissions("sys:user:detail")
    public DataResult<SysUser> detailInfo(@PathVariable("id") String id) {
        DataResult<SysUser> result = DataResult.success();
        result.setData(userService.detailInfo(id));
        return result;
    }

    @GetMapping("/user")
    @ApiOperation(value = "查询用户详情接口")
    @LogAnnotation(title = "用户管理", action = "查询用户详情")
    public DataResult<SysUser> youSelfInfo(HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        DataResult<SysUser> result = DataResult.success();
        result.setData(userService.detailInfo(userId));
        return result;
    }

    @PostMapping("/users")
    @ApiOperation(value = "分页获取用户列表接口")
    @RequiresPermissions("sys:user:list")
    @LogAnnotation(title = "用户管理", action = "分页获取用户列表")
    public DataResult<IPage<SysUser>> pageInfo(@RequestBody UserPageReqVO vo) {
        return DataResult.success(userService.pageInfo(vo));
    }

    @PostMapping("/user")
    @ApiOperation(value = "新增用户接口")
    @RequiresPermissions("sys:user:add")
    @LogAnnotation(title = "用户管理", action = "新增用户")
    public DataResult addUser(@RequestBody @Valid UserAddReqVO vo) {
        userService.addUser(vo);
        return DataResult.success();
    }

    @GetMapping("/user/logout")
    @ApiOperation(value = "退出接口")
    @LogAnnotation(title = "用户管理", action = "退出")
    public DataResult logout(HttpServletRequest request) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        userService.logout(accessToken, refreshToken);
        return DataResult.success();
    }

    @PutMapping("/user/pwd")
    @ApiOperation(value = "修改密码接口")
    @LogAnnotation(title = "用户管理", action = "更新密码")
    public DataResult updatePwd(@RequestBody UpdatePasswordReqVO vo, HttpServletRequest request) {
        String accessToken = request.getHeader(Constant.ACCESS_TOKEN);
        String refreshToken = request.getHeader(Constant.REFRESH_TOKEN);
        String userId = JwtTokenUtil.getUserId(accessToken);
        userService.updatePwd(vo, userId, accessToken, refreshToken);
        return DataResult.success();
    }

    @DeleteMapping("/user")
    @ApiOperation(value = "删除用户接口")
    @LogAnnotation(title = "用户管理", action = "删除用户")
    @RequiresPermissions("sys:user:delete")
    public DataResult deletedUser(@RequestBody @ApiParam(value = "用户id集合") List<String> userIds, HttpServletRequest request) {
        String userId = JwtTokenUtil.getUserId(request.getHeader(Constant.ACCESS_TOKEN));
        userService.deletedUsers(userIds, userId);
        return DataResult.success();
    }

    @GetMapping("/user/roles/{userId}")
    @ApiOperation(value = "赋予角色-获取所有角色接口")
    @LogAnnotation(title = "用户管理", action = "赋予角色-获取所有角色接口")
    @RequiresPermissions("sys:user:role:detail")
    public DataResult<UserOwnRoleRespVO> getUserOwnRole(@PathVariable("userId") String userId) {
        DataResult<UserOwnRoleRespVO> result = DataResult.success();
        result.setData(userService.getUserOwnRole(userId));
        return result;
    }

    @PutMapping("/user/roles/{userId}")
    @ApiOperation(value = "赋予角色-用户赋予角色接口")
    @LogAnnotation(title = "用户管理", action = "赋予角色-用户赋予角色接口")
    @RequiresPermissions("sys:user:update:role")
    public DataResult<UserOwnRoleRespVO> setUserOwnRole(@PathVariable("userId") String userId, @RequestBody List<String> roleIds) {
        DataResult result = DataResult.success();
        userService.setUserOwnRole(userId, roleIds);
        return result;
    }

    @ApiOperation(value = "生成验证码")
    @GetMapping(value = "/getVerify")
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
        response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        try {
            ImageCodeUtil randomValidateCode = new ImageCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            log.error("生成验证码失败");
        }
    }

    @ApiOperation(value = "校验验证码")
    @PostMapping(value = "/checkVerify")
    public DataResult checkVerify(@RequestParam String imageCode, HttpSession session) {
        //从session中获取随机数
        Object random = session.getAttribute(ImageCodeUtil.IMAGE_RANDOM_CODEKEY);
        if (random != null && String.valueOf(random).equals(imageCode)) {
            return DataResult.success();
        }
        return DataResult.fail("验证码输入有误");
    }

    @ApiOperation(value = "获取APP验证码")
    @PostMapping("/getVerify")
    public DataResult getVerify(@RequestBody @ApiParam("{\"phone\":\"手机号码\"}") JSONObject jsonObject) {
        String phone = jsonObject.getString("phone");
        String verificationCode = VerificationCodeUtil.getVerificationCode(6,phone);
        DataResult dataResult = VerificationCodeUtil.checkRyByPhone(phone);
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("verificationCode", verificationCode);
        if (dataResult.getCode() == 0) {
            JSONObject resultRy = JSONObject.parseObject(JSONObject.toJSONString(dataResult.getData()));
            JSONObject data = resultRy.getJSONObject("data");
            resultMap.put("type", resultRy.getIntValue("type"));
            if (resultRy.getIntValue("type") == 0) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("phone", phone);
                queryWrapper.eq("deleted", 1);
                SysUser sysUser = userService.getOne(queryWrapper);
                if (sysUser == null) {
                    sysUser = new SysUser();
                    String id = RandomGUID.getDatetUUID();
                    sysUser.setCreateTime(new Date());
                    sysUser.setId(id);
                    sysUser.setDeleted(1);
                    sysUser.setUpdateTime(new Date());
                    sysUser.setIsVerification(0);
                    sysUser.setVerificationCode(verificationCode);
                    sysUser.setVerificationCodeTime(DateTimeUI.getCurrentDateTime());
                    sysUser.setUnits(data.getString("szdw"));
                    sysUser.setRealName(data.getString("xm"));
                    sysUser.setClearPwd("123456");
                    sysUser.setPhone(phone);
                    sysUser.setUsername(phone);
                    sysUser.setStatus(1);
                    sysUser.setUserType(2);
                    sysUser.setPassword("3228b2247e2f62b2457b926da8223618");
                    sysUser.setSalt("db22c6e366d4408fa090");
                    userService.save(sysUser);
                    UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
                    List<String> roleIds = new ArrayList<>();
                    roleIds.add("eba81012b1665f47879e76c56db2614c");
                    reqVO.setUserId(id);
                    reqVO.setRoleIds(roleIds);
                    userRoleService.addUserRoleInfo(reqVO);
                } else {
                    sysUser.setUpdateTime(new Date());
                    sysUser.setIsVerification(0);
                    sysUser.setVerificationCode(verificationCode);
                    sysUser.setVerificationCodeTime(DateTimeUI.getCurrentDateTime());
                    userService.updateById(sysUser);
                }
            } else {
                throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
            }
        } else {
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        Object[] objects = SMSUtils.sendSmsMessage(phone, verificationCode);
        resultMap.put("smsMsg", objects[1]);
        if ((boolean) objects[0]) {
            return DataResult.success(resultMap);
        } else {
            return DataResult.success(resultMap);
        }

    }
}
