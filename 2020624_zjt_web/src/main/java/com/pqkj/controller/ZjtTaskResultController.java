package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.DateTimeUI;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.TaskUserRelateView;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtTaskResultService;
import com.pqkj.entity.ZjtTaskResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 任务结果 前端控制器
 *
 * @author zbc
 * @since 2020-06-29
 */
@Api(tags = {"任务结果"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtTaskResultController {

    @Resource
    private IZjtTaskResultService zjtTaskResultService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/zjtTaskResult")
    public String zjtTaskResult() {
    return "zjtTaskResult/list";
    }

    @ApiOperation(value = "新增任务结果")
    @PostMapping("zjtTaskResult/add")
    @RequiresPermissions("zjtTaskResult:add")
    @ResponseBody
    public DataResult add(@RequestBody ZjtTaskResult zjtTaskResult){
        zjtTaskResultService.save(zjtTaskResult);
        return DataResult.success();
    }

    @ApiOperation(value = "删除任务结果")
    @DeleteMapping("zjtTaskResult/delete")
    @RequiresPermissions("zjtTaskResult:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtTaskResultService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新任务结果")
    @PutMapping("zjtTaskResult/update")
    @RequiresPermissions("zjtTaskResult:update")
    @ResponseBody
    public DataResult update(@RequestBody ZjtTaskResult zjtTaskResult){
        zjtTaskResultService.updateById(zjtTaskResult);
        return DataResult.success();
    }

    @ApiOperation(value = "查询任务结果分页数据")
    @PostMapping("zjtTaskResult/listByPage")
    @RequiresPermissions("zjtTaskResult:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ZjtTaskResult zjtTaskResult){
        Page page = new Page(zjtTaskResult.getPage(), zjtTaskResult.getLimit());
        QueryWrapper<ZjtTaskResult> queryWrapper = new QueryWrapper();
        //查询条件示例
        //queryWrapper.lambda().eq(ZjtTaskResult::getId, zjtTaskResult.getId());
        IPage<ZjtTaskResult> iPage = zjtTaskResultService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
