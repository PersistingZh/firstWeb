package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtWarningRecordService;
import com.pqkj.entity.ZjtWarningRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 *  前端控制器
 *
 * @author zbc
 * @since 2020-07-04
 */
@Api(tags = {""})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtWarningRecordController {

    @Resource
    private IZjtWarningRecordService zjtWarningRecordService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/zjtWarningRecord")
    public String zjtWarningRecord() {
    return "zjtWarningRecord/list";
    }

    @ApiOperation(value = "新增")
    @PostMapping("zjtWarningRecord/add")
    @RequiresPermissions("zjtWarningRecord:add")
    @ResponseBody
    public DataResult add(@RequestBody ZjtWarningRecord zjtWarningRecord){
        zjtWarningRecordService.save(zjtWarningRecord);
        return DataResult.success();
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("zjtWarningRecord/delete")
    @RequiresPermissions("zjtWarningRecord:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtWarningRecordService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新")
    @PutMapping("zjtWarningRecord/update")
    @RequiresPermissions("zjtWarningRecord:update")
    @ResponseBody
    public DataResult update(@RequestBody ZjtWarningRecord zjtWarningRecord){
        zjtWarningRecordService.updateById(zjtWarningRecord);
        return DataResult.success();
    }

    @ApiOperation(value = "查询分页数据")
    @PostMapping("zjtWarningRecord/listByPage")
    @RequiresPermissions("zjtWarningRecord:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ZjtWarningRecord zjtWarningRecord){
        Page page = new Page(zjtWarningRecord.getPage(), zjtWarningRecord.getLimit());
        QueryWrapper<ZjtWarningRecord> queryWrapper = new QueryWrapper();
        //查询条件示例
        IPage<ZjtWarningRecord> iPage = zjtWarningRecordService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
