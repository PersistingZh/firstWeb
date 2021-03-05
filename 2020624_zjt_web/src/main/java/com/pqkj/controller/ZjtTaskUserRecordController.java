package com.pqkj.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.ZjtTaskUserRelate;
import com.pqkj.service.IZjtTaskUserRelateService;
import com.pqkj.vo.req.PageReqVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtTaskUserRecordService;
import com.pqkj.entity.ZjtTaskUserRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 用户与任务记录表 前端控制器
 *
 * @author zbc
 * @since 2020-06-28
 */
@Api(tags = {"任务相关"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtTaskUserRecordController {

    @Resource
    private IZjtTaskUserRecordService zjtTaskUserRecordService;

    @Resource
    private IZjtTaskUserRelateService zjtTaskUserRelateService;
    /**
    * 跳转到页面
    */
    @ApiOperation(value = "后台数据页面")
    @GetMapping("/index/zjtTaskUserRecord")
    public String zjtTaskUserRecord() {
    return "zjtTaskUserRecord/list";
    }



}
