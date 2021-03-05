package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.GzryAddReqVO;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtGzryService;
import com.pqkj.entity.ZjtGzry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 工作人员 前端控制器
 *
 * @author zbc
 * @since 2020-06-29
 */
@Api(tags = {"工作人员"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtGzryController {

    @Resource
    private IZjtGzryService zjtGzryService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/zjtGzry")
    public String zjtGzry() {
    return "zjtGzry/list";
    }

    @ApiOperation(value = "新增工作人员")
    @PostMapping("zjtGzry/add")
    @RequiresPermissions("zjtGzry:add")
    @ResponseBody
    public DataResult add(@RequestBody GzryAddReqVO vo){
        zjtGzryService.addGzry(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除工作人员")
    @DeleteMapping("zjtGzry/delete")
    @RequiresPermissions("zjtGzry:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtGzryService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新工作人员")
    @PutMapping("zjtGzry/update")
    @RequiresPermissions("zjtGzry:update")
    @ResponseBody
    public DataResult update(@RequestBody ZjtGzry zjtGzry){
        zjtGzryService.updateById(zjtGzry);
        return DataResult.success();
    }

    @ApiOperation(value = "查询工作人员分页数据")
    @PostMapping("zjtGzry/listByPage")
    @RequiresPermissions("zjtGzry:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ZjtGzry zjtGzry){
        Page page = new Page(zjtGzry.getPage(), zjtGzry.getLimit());
        QueryWrapper<ZjtGzry> queryWrapper = new QueryWrapper();
        //查询条件示例
        //queryWrapper.lambda().eq(ZjtGzry::getId, zjtGzry.getId());
        IPage<ZjtGzry> iPage = zjtGzryService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


}
