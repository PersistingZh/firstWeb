package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.ITaskResultViewService;
import com.pqkj.entity.TaskResultView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * VIEW 前端控制器
 *
 * @author zbc
 * @since 2020-06-29
 */
@Api(tags = {"VIEW"})
@Slf4j
@Controller
@RequestMapping("/")
public class TaskResultViewController {

    @Resource
    private ITaskResultViewService taskResultViewService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/taskResultView")
    public String taskResultView() {
    return "taskResultView/list";
    }

    @ApiOperation(value = "新增VIEW")
    @PostMapping("taskResultView/add")
    @RequiresPermissions("taskResultView:add")
    @ResponseBody
    public DataResult add(@RequestBody TaskResultView taskResultView){
        taskResultViewService.save(taskResultView);
        return DataResult.success();
    }

    @ApiOperation(value = "删除VIEW")
    @DeleteMapping("taskResultView/delete")
    @RequiresPermissions("taskResultView:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        taskResultViewService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新VIEW")
    @PutMapping("taskResultView/update")
    @RequiresPermissions("taskResultView:update")
    @ResponseBody
    public DataResult update(@RequestBody TaskResultView taskResultView){
        taskResultViewService.updateById(taskResultView);
        return DataResult.success();
    }

    @ApiOperation(value = "查询VIEW分页数据")
    @PostMapping("taskResultView/listByPage")
    @RequiresPermissions("taskResultView:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody TaskResultView taskResultView){
        Page page = new Page(taskResultView.getPage(), taskResultView.getLimit());
        QueryWrapper<TaskResultView> queryWrapper = new QueryWrapper();
        if (!StringUtil.isNullorEmpty(taskResultView.getIsFinish())){
            queryWrapper.eq("is_finish",taskResultView.getIsFinish());
        }
        if (!StringUtil.isNullorEmpty(taskResultView.getClockEffective())){
            queryWrapper.eq("clock_effective",taskResultView.getClockEffective());
        }
        if(!StringUtil.isNullorEmpty(taskResultView.getCreateTime())){
            queryWrapper.like("create_time",taskResultView.getCreateTime());
        }
        queryWrapper.orderByDesc("create_time");
        IPage<TaskResultView> iPage = taskResultViewService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }


}
