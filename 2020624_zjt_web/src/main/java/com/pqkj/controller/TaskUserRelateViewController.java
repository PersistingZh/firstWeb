package com.pqkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.ZjtTask;
import com.pqkj.entity.ZjtTaskResult;
import com.pqkj.entity.ZjtTaskUserRelate;
import com.pqkj.quartz.MyScheduler;
import com.pqkj.service.*;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.pqkj.entity.TaskUserRelateView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * VIEW 前端控制器
 *
 * @author zbc
 * @since 2020-06-28
 */
@Api(tags = {"任务相关"})
@Slf4j
@Controller
@RequestMapping("/")
public class TaskUserRelateViewController {
    private static final Logger logger = LoggerFactory.getLogger(TaskUserRelateViewController.class);

    @Resource
    private ITaskUserRelateViewService taskUserRelateViewService;
    @Resource
    private IZjtTaskUserRelateService zjtTaskUserRelateService;
    @Resource
    private IZjtTaskResultService zjtTaskResultService;
    @Resource
    private IZjtTaskService taskService;
    /**
    * 跳转到页面
    */
    @ApiOperation(value = "后台数据页面")
    @GetMapping("/index/taskUserRelateView")
    public String taskUserRelateView() {
    return "taskUserRelateView/list";
    }


    @ApiOperation(value = "查询任务分页接口")
    @PostMapping("taskUserRelateView/listByPage")
    @RequiresPermissions("taskUserRelateView:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody TaskUserRelateView taskUserRelateView){
        Page page = new Page(taskUserRelateView.getPage(), taskUserRelateView.getLimit());
        QueryWrapper<TaskUserRelateView> queryWrapper = new QueryWrapper();
        queryWrapper.eq("is_del",0);
        IPage<TaskUserRelateView> iPage = taskUserRelateViewService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "查询用户任务接口")
    @PostMapping("zjtTaskUserRelate/listByUserId")
    @RequiresPermissions("zjtTaskUserRelate:list")
    @ResponseBody
    public DataResult listByUserId(@RequestBody @ApiParam(value = "{\"userId\":\"用户id\",\"taskId\":\"任务ID\",\"phone\":\"手机号码\"}") JSONObject jsonObject){
        String userId = jsonObject.getString("userId");
        String taskId = jsonObject.getString("taskId");
        String phone = jsonObject.getString("phone");
        QueryWrapper<TaskUserRelateView> queryWrapper = new QueryWrapper();
        if (!StringUtil.isNullorEmpty(userId)){
            queryWrapper.eq("user_id",userId);
        }
        if (!StringUtil.isNullorEmpty(taskId)){
            queryWrapper.eq("task_id",taskId);
        }
        if (!StringUtil.isNullorEmpty(taskId)){
            queryWrapper.eq("task_id",taskId);
        }
        if (!StringUtil.isNullorEmpty(phone)){
            queryWrapper.like("phone",phone);
        }
        queryWrapper.eq("is_del",0);
        List<TaskUserRelateView> list = taskUserRelateViewService.list(queryWrapper);
        return DataResult.success(list);
    }
    @ApiOperation(value = "生成任务")
    @PostMapping("zjtTaskResult/createTask")
    @RequiresPermissions("zjtTaskResult:list")
    @ResponseBody
    public DataResult createTask(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.lt("effective_time", DateTimeUI.getCurrentDateTime());
        queryWrapper.gt("failure_time",DateTimeUI.getCurrentDateTime());
        queryWrapper.eq("cycle",1);
        List<TaskUserRelateView> taskUserRelateViewList = taskUserRelateViewService.list(queryWrapper);
        if (taskUserRelateViewList.size()>0){
            for (TaskUserRelateView taskUserRelateView : taskUserRelateViewList) {
                ZjtTaskResult zjtTaskResult = new ZjtTaskResult();
                BeanUtils.copyProperties(taskUserRelateView,zjtTaskResult);
                zjtTaskResultService.addTaskResult(zjtTaskResult);
                UpdateWrapper<ZjtTaskUserRelate> updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id",taskUserRelateView.getId()).set("is_generate","1");
                zjtTaskUserRelateService.update(updateWrapper);
                logger.info("创建任务---------------\n"+"任务名称： "+taskUserRelateView.getTaskName()+"\n用户姓名："+taskUserRelateView.getUserName()+"\n时间：" + DateTimeUI.getCurrentDateTime());
            }
        }
        return DataResult.success();
    }

    @ApiOperation(value = "查询用户未配置的任务接口")
    @PostMapping("zjtTaskUserRelate/listByUserIdAndNotConfig")
    @RequiresPermissions("zjtTaskUserRelate:list")
    @ResponseBody
    public DataResult listByUserIdAndNotConfig(@RequestBody @ApiParam(value = "{\"userId\":\"用户id\",\"type\":\"0:未完成，1:已完成\"}") JSONObject jsonObject){
        String userId = jsonObject.getString("userId");
        if (StringUtil.isNullorEmpty(userId)){
            return DataResult.success("userId 不能为空");
        }
        QueryWrapper<TaskUserRelateView> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        List<TaskUserRelateView> list = taskUserRelateViewService.list(queryWrapper);
        List<String> taskId = new ArrayList<>();
        if(list.size()>0){
            for (TaskUserRelateView taskUserRelateView : list) {
                taskId.add(taskUserRelateView.getTaskId());
            }
            QueryWrapper<ZjtTask> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.notIn("id",taskId);
            return DataResult.success(taskService.list(queryWrapper1));
        }else{
            return DataResult.success(taskService.list());
        }

    }
}
