package com.pqkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import com.pqkj.vo.req.TaskUserRelateAddReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtTaskUserRelateService;
import com.pqkj.entity.ZjtTaskUserRelate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 任务与用户关系表 前端控制器
 *
 * @author zbc
 * @since 2020-06-28
 */
@Api(tags = {"任务相关"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtTaskUserRelateController {

    @Resource
    private IZjtTaskUserRelateService zjtTaskUserRelateService;

    /**
    * 跳转到页面
    */
    @ApiOperation(value = "后台数据页面")
    @GetMapping("/index/zjtTaskUserRelate")
    public String zjtTaskUserRelate() {
    return "zjtTaskUserRelate/list";
    }

    @ApiOperation(value = "用户匹配任务")
    @PostMapping("zjtTaskUserRelate/add")
    @RequiresPermissions("zjtTaskUserRelate:add")
    @ResponseBody
    public DataResult add(@RequestBody TaskUserRelateAddReqVO vo){
        zjtTaskUserRelateService.addTaskUserRelate(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "取消用户匹配任务")
    @DeleteMapping("zjtTaskUserRelate/delete")
    @RequiresPermissions("zjtTaskUserRelate:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtTaskUserRelateService.removeByIds(ids);
        return DataResult.success();
    }



}
