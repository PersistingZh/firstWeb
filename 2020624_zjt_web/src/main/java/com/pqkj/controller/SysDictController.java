package com.pqkj.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.DataResult;
import com.pqkj.service.ISysDictService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import com.pqkj.entity.SysDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 字典管理 前端控制器
 * </p>
 *
 * @author zbc
 * @since 2020-03-27
 */
@Api(tags = {"字典管理"})
@Slf4j
@Controller
@RequestMapping("/")
public class SysDictController {

    @Resource
    private ISysDictService sysDictService;

    /**
    * 跳转到页面
    */
    @GetMapping("/index/sysDict")
    public String sysDict() {
    return "sysDict/list";
    }

    @ApiOperation(value = "新增字典管理")
    @PostMapping("sysDict/add")
    @RequiresPermissions("sysDict:add")
    @ResponseBody
    public DataResult add(@RequestBody SysDict sysDict){
        sysDictService.save(sysDict);
        return DataResult.success();
    }

    @ApiOperation(value = "删除字典管理")
    @DeleteMapping("sysDict/delete")
    @RequiresPermissions("sysDict:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        sysDictService.removeByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新字典管理")
    @PutMapping("sysDict/update")
    @RequiresPermissions("sysDict:update")
    @ResponseBody
    public DataResult update(@RequestBody SysDict sysDict){
        sysDictService.updateById(sysDict);
        return DataResult.success();
    }

    @ApiOperation(value = "查询字典管理分页数据")
    @PostMapping("sysDict/listByPage")
    @RequiresPermissions("sysDict:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody SysDict sysDict){
        Page page = new Page(sysDict.getPage(), sysDict.getLimit());
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper();
        if (StrUtil.isNotEmpty(sysDict.getSdPid())){
            queryWrapper.lambda().like(SysDict::getSdPid, sysDict.getSdPid())
                    .orderByAsc(SysDict::getOrderr);
        }
        if (StrUtil.isNotEmpty(sysDict.getSdName())){
            queryWrapper.lambda().like(SysDict::getSdName, sysDict.getSdName());
        }
        if(StrUtil.isNotEmpty(sysDict.getType())){
            queryWrapper.lambda().eq(SysDict::getType,sysDict.getType());
        }
        IPage<SysDict> iPage = sysDictService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }
    @PostMapping("sysDict/getDicListByMap")
    @ResponseBody
    public DataResult getDicListByMap(@RequestBody JSONObject jsonRequest){
        Object[] obj = new Object[3];
        try {
            JSONObject conditionMap = jsonRequest.getJSONObject("condition");
            JSONObject ordersMap=jsonRequest.getJSONObject("ordersMap");
            QueryWrapper<SysDict> queryWrapper = new QueryWrapper();
            if (conditionMap.getString("sdPid").equals("0")){
                queryWrapper.eq("sd_pid",conditionMap.getString("sdPid"))
                        .eq("sd_level",conditionMap.getString("sdLevel"))
                        .orderByAsc("orderr");
                //.eq(SysDict::geto)
            }else {
                queryWrapper.eq("sd_pid",conditionMap.getString("sdPid"))
                        .orderByAsc("orderr");
            }

            List<SysDict>  list = sysDictService.list(queryWrapper);
            obj[0]=list;
            if(conditionMap.containsKey("openid")){
                SysDict sysDict=sysDictService.getById(conditionMap.getString("openid"));
                if(sysDict != null){
                    String[] strArr = sysDict.getPath().split("#");
                    if(strArr != null && strArr.length > 0){
                        queryWrapper = new QueryWrapper();
                        queryWrapper.like("path",strArr[1]);
                        queryWrapper.ne("id",strArr[1]);
                        List<SysDict>  sysHylbdmList = sysDictService.list(queryWrapper);
                        if(sysHylbdmList != null && sysHylbdmList.size() > 0){
                            obj[1] = sysHylbdmList;
                            obj[2] = sysDict.getPath();
                        }
                    }
                }
            }
            return DataResult.success(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
