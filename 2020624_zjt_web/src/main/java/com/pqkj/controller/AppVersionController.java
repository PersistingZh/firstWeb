package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IAppVersionService;
import com.pqkj.entity.AppVersion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 *  前端控制器
 *
 * @author zbc
 * @since 2020-07-09
 */
@Api(tags = {""})
@Slf4j
@Controller
@RequestMapping("/")
public class AppVersionController {

    @Resource
    private IAppVersionService appVersionService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/appVersion")
    public String appVersion() {
    return "appVersion/list";
    }

    @ApiOperation(value = "新增")
    @PostMapping("appVersion/addOrUpdate")
    @RequiresPermissions("appVersion:add")
    @ResponseBody
    public DataResult add(@RequestBody AppVersion appVersion){
        return appVersionService.addOrUpdate(appVersion);
    }

    @ApiOperation(value = "删除")
    @PostMapping("appVersion/delete")
    @RequiresPermissions("appVersion:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        appVersionService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("appVersion/listByPage")
    @RequiresPermissions("appVersion:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody AppVersion appVersion){
        Page page = new Page(appVersion.getPage(), appVersion.getLimit());
        QueryWrapper<AppVersion> queryWrapper = new QueryWrapper();
        //查询条件示例
        if(!StringUtil.isNullorEmpty(appVersion.getVersionCode())){
            queryWrapper.like("version_code",appVersion.getVersionCode());
        }
        queryWrapper.orderByDesc("create_time");
        IPage<AppVersion> iPage = appVersionService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
