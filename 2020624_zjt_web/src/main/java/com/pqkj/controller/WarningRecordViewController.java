package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IWarningRecordViewService;
import com.pqkj.entity.WarningRecordView;
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
 * @since 2020-07-05
 */
@Api(tags = {"VIEW"})
@Slf4j
@Controller
@RequestMapping("/")
public class WarningRecordViewController {

    @Resource
    private IWarningRecordViewService warningRecordViewService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/warningRecordView")
    public String warningRecordView() {
    return "warningRecordView/list";
    }

    @ApiOperation(value = "新增VIEW")
    @PostMapping("warningRecordView/add")
    @RequiresPermissions("warningRecordView:add")
    @ResponseBody
    public DataResult add(@RequestBody WarningRecordView warningRecordView){
        warningRecordViewService.save(warningRecordView);
        return DataResult.success();
    }

    @ApiOperation(value = "删除VIEW")
    @DeleteMapping("warningRecordView/delete")
    @RequiresPermissions("warningRecordView:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        warningRecordViewService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新VIEW")
    @PutMapping("warningRecordView/update")
    @RequiresPermissions("warningRecordView:update")
    @ResponseBody
    public DataResult update(@RequestBody WarningRecordView warningRecordView){
        warningRecordViewService.updateById(warningRecordView);
        return DataResult.success();
    }

    @ApiOperation(value = "查询VIEW分页数据")
    @PostMapping("warningRecordView/listByPage")
    @RequiresPermissions("warningRecordView:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody WarningRecordView warningRecordView){
        Page page = new Page(warningRecordView.getPage(), warningRecordView.getLimit());
        QueryWrapper<WarningRecordView> queryWrapper = new QueryWrapper();
        queryWrapper.eq("warning_type",1);
        if (!StringUtil.isNullorEmpty(warningRecordView.getCreateTime())){
            queryWrapper.like("create_time",warningRecordView.getCreateTime());
        }
        queryWrapper.eq("is_del",0);
        queryWrapper.orderByDesc("create_time");
        IPage<WarningRecordView> iPage = warningRecordViewService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
