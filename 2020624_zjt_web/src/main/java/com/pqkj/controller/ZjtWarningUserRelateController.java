package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import com.pqkj.vo.req.WarningRelateAddReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtWarningUserRelateService;
import com.pqkj.entity.ZjtWarningUserRelate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 预警与用户关系表 前端控制器
 *
 * @author zbc
 * @since 2020-07-03
 */
@Api(tags = {"预警与用户关系表"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtWarningUserRelateController {

    @Resource
    private IZjtWarningUserRelateService zjtWarningUserRelateService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/zjtWarningUserRelate")
    public String zjtWarningUserRelate() {
    return "zjtWarningUserRelate/list";
    }

    @ApiOperation(value = "配置预警")
    @PostMapping("zjtWarningUserRelate/add")
    @RequiresPermissions("zjtWarningUserRelate:add")
    @ResponseBody
    public DataResult add(@RequestBody WarningRelateAddReqVO vo){
        zjtWarningUserRelateService.addWarningRelate(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除预警与用户关系表")
    @DeleteMapping("zjtWarningUserRelate/delete")
    @RequiresPermissions("zjtWarningUserRelate:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtWarningUserRelateService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "查询预警与用户关系表分页数据")
    @PostMapping("zjtWarningUserRelate/listByPage")
    @RequiresPermissions("zjtWarningUserRelate:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ZjtWarningUserRelate zjtWarningUserRelate){
        Page page = new Page(zjtWarningUserRelate.getPage(), zjtWarningUserRelate.getLimit());
        QueryWrapper<ZjtWarningUserRelate> queryWrapper = new QueryWrapper();
        //查询条件示例
        queryWrapper.eq("user_id",zjtWarningUserRelate.getUserId());
        IPage<ZjtWarningUserRelate> iPage = zjtWarningUserRelateService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
