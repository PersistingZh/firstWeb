package com.pqkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import com.pqkj.vo.req.TaskAddReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtTaskService;
import com.pqkj.entity.ZjtTask;
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
 * 任务表 前端控制器
 *
 * @author zbc
 * @since 2020-06-28
 */
@Api(tags = {"任务相关"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtTaskController {

    @Resource
    private IZjtTaskService zjtTaskService;

    /**
    * 跳转到页面
    */
    @ApiOperation(value = "后台数据页面")
    @GetMapping("/index/zjtTask")
    public String zjtTask() {
    return "zjtTask/list";
    }

    @ApiOperation(value = "新增任务")
    @PostMapping("zjtTask/add")
    @RequiresPermissions("zjtTask:add")
    @ResponseBody
    public DataResult add(@RequestBody @Valid TaskAddReqVO vo){
        zjtTaskService.addTask(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除任务")
    @DeleteMapping("zjtTask/delete")
    @RequiresPermissions("zjtTask:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtTaskService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "编辑任务")
    @PutMapping("zjtTask/update")
    @RequiresPermissions("zjtTask:update")
    @ResponseBody
    public DataResult update(@RequestBody ZjtTask zjtTask){
        zjtTaskService.updateById(zjtTask);
        return DataResult.success();
    }

    @ApiOperation(value = "查询任务分页数据")
    @PostMapping("zjtTask/listByPage")
    @RequiresPermissions("zjtTask:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ZjtTask zjtTask){
        Page page = new Page(zjtTask.getPage(), zjtTask.getLimit());
        QueryWrapper<ZjtTask> queryWrapper = new QueryWrapper();
        //查询条件示例
        //queryWrapper.lambda().eq(ZjtTask::getId, zjtTask.getId());
        IPage<ZjtTask> iPage = zjtTaskService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    @ApiOperation(value = "根据id获取任务")
    @PostMapping("zjtTask/getTaskById")
    @RequiresPermissions("zjtTask:list")
    @ResponseBody
    public DataResult getTaskById(@RequestBody @ApiParam("{\"id\":\"任务id\"}") JSONObject jsonObject){
        return DataResult.success(zjtTaskService.getById(jsonObject.getString("id")));
    }
}
