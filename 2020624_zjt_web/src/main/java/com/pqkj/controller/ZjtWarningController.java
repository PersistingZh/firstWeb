package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import com.pqkj.vo.req.WarningAddReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtWarningService;
import com.pqkj.entity.ZjtWarning;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;

/**
 *  前端控制器
 *
 * @author zbc
 * @since 2020-06-28
 */
@Api(tags = {"预警相关"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtWarningController {

    @Resource
    private IZjtWarningService zjtWarningService;

    /**
    * 跳转到页面
    */
    @ApiOperation(value = "后台数据页面")
    @GetMapping("/index/zjtWarning")
    public String zjtWarning() {
    return "zjtWarning/list";
    }

    @ApiOperation(value = "新增")
    @PostMapping("zjtWarning/add")
    @RequiresPermissions("zjtWarning:add")
    @ResponseBody
    public DataResult add(@RequestBody @Valid WarningAddReqVO vo){
        zjtWarningService.addWarning(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("zjtWarning/delete")
    @RequiresPermissions("zjtWarning:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtWarningService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("zjtWarning/update")
    @RequiresPermissions("zjtWarning:update")
    @ResponseBody
    public DataResult update(@RequestBody ZjtWarning zjtWarning){
        zjtWarningService.updateById(zjtWarning);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("zjtWarning/listByPage")
    @RequiresPermissions("zjtWarning:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ZjtWarning zjtWarning){
        Page page = new Page(zjtWarning.getPage(), zjtWarning.getLimit());
        QueryWrapper<ZjtWarning> queryWrapper = new QueryWrapper();
        //查询条件示例
        IPage<ZjtWarning> iPage = zjtWarningService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
