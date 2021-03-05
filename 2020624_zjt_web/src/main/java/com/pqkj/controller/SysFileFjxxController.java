package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.ISysFileFjxxService;
import com.pqkj.entity.SysFileFjxx;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 文件附加信息表 前端控制器
 *
 * @author zbc
 * @since 2020-07-13
 */
@Api(tags = {"文件附加信息表"})
@Slf4j
@Controller
@RequestMapping("/")
public class SysFileFjxxController {

    @Resource
    private ISysFileFjxxService sysFileFjxxService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/sysFileFjxx")
    public String sysFileFjxx() {
    return "sysFileFjxx/list";
    }

    @ApiOperation(value = "新增文件附加信息表")
    @PostMapping("sysFileFjxx/add")
    @RequiresPermissions("sysFileFjxx:add")
    @ResponseBody
    public DataResult add(@RequestBody SysFileFjxx sysFileFjxx){
        sysFileFjxxService.save(sysFileFjxx);
        return DataResult.success();
    }

    @ApiOperation(value = "删除文件附加信息表")
    @DeleteMapping("sysFileFjxx/delete")
    @RequiresPermissions("sysFileFjxx:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        sysFileFjxxService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新文件附加信息表")
    @PutMapping("sysFileFjxx/update")
    @RequiresPermissions("sysFileFjxx:update")
    @ResponseBody
    public DataResult update(@RequestBody SysFileFjxx sysFileFjxx){
        sysFileFjxxService.updateById(sysFileFjxx);
        return DataResult.success();
    }

    @ApiOperation(value = "查询文件附加信息表分页数据")
    @PostMapping("sysFileFjxx/listByPage")
    @RequiresPermissions("sysFileFjxx:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody SysFileFjxx sysFileFjxx){
        Page page = new Page(sysFileFjxx.getPage(), sysFileFjxx.getLimit());
        QueryWrapper<SysFileFjxx> queryWrapper = new QueryWrapper();
        //查询条件示例
        //queryWrapper.lambda().eq(SysFileFjxx::getId, sysFileFjxx.getId());
        IPage<SysFileFjxx> iPage = sysFileFjxxService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
