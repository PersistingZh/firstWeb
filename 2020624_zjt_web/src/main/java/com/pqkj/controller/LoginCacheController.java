package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.ILoginCacheService;
import com.pqkj.entity.LoginCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 登录缓存表 前端控制器
 *
 * @author zbc
 * @since 2020-07-04
 */
@Api(tags = {"登录缓存表"})
@Slf4j
@Controller
@RequestMapping("/")
public class LoginCacheController {

    @Resource
    private ILoginCacheService loginCacheService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/loginCache")
    public String loginCache() {
    return "loginCache/list";
    }

    @ApiOperation(value = "新增登录缓存表")
    @PostMapping("loginCache/add")
    @RequiresPermissions("loginCache:add")
    @ResponseBody
    public DataResult add(@RequestBody LoginCache loginCache){
        loginCacheService.save(loginCache);
        return DataResult.success();
    }

    @ApiOperation(value = "删除登录缓存表")
    @DeleteMapping("loginCache/delete")
    @RequiresPermissions("loginCache:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        loginCacheService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新登录缓存表")
    @PutMapping("loginCache/update")
    @RequiresPermissions("loginCache:update")
    @ResponseBody
    public DataResult update(@RequestBody LoginCache loginCache){
        loginCacheService.updateById(loginCache);
        return DataResult.success();
    }


}
