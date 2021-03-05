package com.pqkj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pqkj.vo.req.SysLogPageReqVO;
import com.pqkj.common.aop.annotation.LogAnnotation;
import com.pqkj.entity.SysLog;
import com.pqkj.service.LogService;
import com.pqkj.common.utils.DataResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/sys")
@Api(tags = "系统模块-系统操作日志管理")
@RestController
public class SysLogController {

    @Autowired
    private LogService logService;

    @PostMapping("/logs")
    @ApiOperation(value = "分页查询系统操作日志接口")
    @LogAnnotation(title = "系统操作日志管理", action = "分页查询系统操作日志")
    @RequiresPermissions("sys:log:list")
    public DataResult<IPage<SysLog>> pageInfo(@RequestBody SysLogPageReqVO vo) {
        return DataResult.success(logService.pageInfo(vo));
    }

    @DeleteMapping("/logs")
    @ApiOperation(value = "删除日志接口")
    @LogAnnotation(title = "系统操作日志管理", action = "删除系统操作日志")
    @RequiresPermissions("sys:log:delete")
    public DataResult delete(@RequestBody List<String> logIds) {
        logService.delete(logIds);
        return DataResult.success();
    }
}
