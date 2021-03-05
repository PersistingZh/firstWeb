package com.pqkj.controller;

import cn.hutool.core.date.DateRange;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.vo.req.PageReqVO;
import com.pqkj.vo.req.SysParameterAddVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.ISysParameterService;
import com.pqkj.entity.SysParameter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;

/**
 * 系统公用参数 前端控制器
 *
 * @author zbc
 * @since 2020-07-09
 */
@Api(tags = {"系统公用参数"})
@Slf4j
@Controller
@RequestMapping("/")
public class SysParameterController {

    @Resource
    private ISysParameterService sysParameterService;

    /**
     * 跳转到页面
     */
    @GetMapping("/index/sysParameter")
    public String sysParameter() {
        return "sysParameter/list";
    }

    @ApiOperation(value = "新增系统公用参数")
    @PostMapping("sysParameter/add")
    @RequiresPermissions("sysParameter:add")
    @ResponseBody
    public DataResult add(@RequestBody @Valid SysParameterAddVO vo) {
        sysParameterService.addSysParameter(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除系统公用参数")
    @DeleteMapping("sysParameter/delete")
    @RequiresPermissions("sysParameter:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids) {
        sysParameterService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新系统公用参数")
    @PutMapping("sysParameter/update")
    @RequiresPermissions("sysParameter:update")
    @ResponseBody
    public DataResult update(@RequestBody SysParameter sysParameter) {
        sysParameterService.updateById(sysParameter);
        return DataResult.success();
    }

    @ApiOperation(value = "查询系统公用参数分页数据")
    @PostMapping("sysParameter/listByPage")
    @RequiresPermissions("sysParameter:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody SysParameter sysParameter) {
        Page page = new Page(sysParameter.getPage(), sysParameter.getLimit());
        QueryWrapper<SysParameter> queryWrapper = new QueryWrapper();
        //查询条件示例
        //queryWrapper.lambda().eq(SysParameter::getId, sysParameter.getId());
        IPage<SysParameter> iPage = sysParameterService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "根据名称获取系统公用参数")
    @PostMapping("sysParameter/getSysParameterByName")
    @RequiresPermissions("sysParameter:list")
    @ResponseBody
    public DataResult getSysParameterByName(@RequestBody SysParameter sysParameter) {
        QueryWrapper<SysParameter> queryWrapper = new QueryWrapper();
        //查询条件示例
        if(StringUtil.isNullorEmpty(sysParameter.getParameterName())){
           return DataResult.fail("参数为空");
        }
        queryWrapper.eq("parameter_name",sysParameter.getParameterName());
        return DataResult.success(sysParameterService.getOne(queryWrapper));
    }
}
