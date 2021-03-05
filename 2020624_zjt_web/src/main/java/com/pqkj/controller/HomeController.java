package com.pqkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.pqkj.common.utils.Constant;
import com.pqkj.common.utils.DataResult;
import com.pqkj.common.utils.JwtTokenUtil;
import com.pqkj.service.*;
import com.pqkj.vo.resp.HomeRespVO;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Api(tags = "首页数据")
public class HomeController {
    @Autowired
    private HomeService homeService;
    /**
     * 包含用户信息和菜单列表
     *
     * @param request
     * @return
     */
    @GetMapping("/home")
    @ApiOperation(value = "获取首页数据接口")
    public DataResult<HomeRespVO> getHomeInfo(HttpServletRequest request) {
        String accessToken = request.getHeader("authorization");
        /**
         * 通过access_token拿userId
         */
        String userId = JwtTokenUtil.getUserId(accessToken);
        DataResult<HomeRespVO> result = DataResult.success();
        result.setData(homeService.getHomeInfo(userId));

        return result;
    }


    /**
     * 获取权限列表
     */
    @GetMapping("/getPermissionInfo")
    @ApiOperation(value = "获取权限列表")
    public DataResult getPermissionInfo(HttpServletRequest request) {
        String accessToken = request.getHeader("authorization");
        Claims claimsFromToken = JwtTokenUtil.getClaimsFromToken(accessToken);
        DataResult result = DataResult.success();
        result.setData(claimsFromToken.get(Constant.JWT_PERMISSIONS_KEY));
        return result;
    }
}
