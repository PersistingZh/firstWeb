package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.service.ISysPropertyService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.entity.SysProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 系统属性 前端控制器
 * </p>
 *
 * @author zbc
 * @since 2020-04-06
 */
@Api(tags = {"系统属性"})
@Slf4j
@Controller
@RequestMapping("/")
public class SysPropertyController {

    @Resource
    private ISysPropertyService sysPropertyService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/sysProperty")
    public String sysProperty() {
    return "sysProperty/list";
    }

    @ApiOperation(value = "新增系统属性")
    @PostMapping("sysProperty/add")
    @RequiresPermissions("sysProperty:add")
    @ResponseBody
    public DataResult add(@RequestBody SysProperty sysProperty){
        sysPropertyService.save(sysProperty);
        return DataResult.success();
    }

    @ApiOperation(value = "删除系统属性")
    @DeleteMapping("sysProperty/delete")
    @RequiresPermissions("sysProperty:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        sysPropertyService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新系统属性")
    @PutMapping("sysProperty/update")
    @RequiresPermissions("sysProperty:update")
    @ResponseBody
    public DataResult update(@RequestBody SysProperty sysProperty){
        sysPropertyService.updateById(sysProperty);
        return DataResult.success();
    }

    @ApiOperation(value = "查询系统属性分页数据")
    @PostMapping("sysProperty/listByPage")
    @RequiresPermissions("sysProperty:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody SysProperty sysProperty){
        Page page = new Page(sysProperty.getPage(), sysProperty.getLimit());
        QueryWrapper queryWrapper = new QueryWrapper();
        //查询条件示例
        //queryWrapper.eq("id", sysProperty.getId());
        IPage<SysProperty> iPage = sysPropertyService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
