package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IWarningUserRelateViewService;
import com.pqkj.entity.WarningUserRelateView;
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
 * @since 2020-07-03
 */
@Api(tags = {"预警相关"})
@Slf4j
@Controller
@RequestMapping("/")
public class WarningUserRelateViewController {

    @Resource
    private IWarningUserRelateViewService warningUserRelateViewService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/warningUserRelateView")
    public String warningUserRelateView() {
    return "warningUserRelateView/list";
    }

    @ApiOperation(value = "查询预警列表")
    @PostMapping("warningUserRelateView/listByPage")
    @RequiresPermissions("warningUserRelateView:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody WarningUserRelateView warningUserRelateView){
        Page page = new Page(warningUserRelateView.getPage(), warningUserRelateView.getLimit());
        QueryWrapper<WarningUserRelateView> queryWrapper = new QueryWrapper();
        if(!StringUtil.isNullorEmpty(warningUserRelateView.getWarningId())){
            queryWrapper.eq("warning_id",warningUserRelateView.getWarningId());
        }
        if(!StringUtil.isNullorEmpty(warningUserRelateView.getUserId())){
            queryWrapper.eq("user_id",warningUserRelateView.getUserId());
        }
        queryWrapper.eq("is_del",0);
        //查询条件示例
        //queryWrapper.lambda().eq(WarningUserRelateView::getId, warningUserRelateView.getId());
        IPage<WarningUserRelateView> iPage = warningUserRelateViewService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

}
