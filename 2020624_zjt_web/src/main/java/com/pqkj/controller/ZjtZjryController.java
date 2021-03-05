package com.pqkj.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.common.utils.DataResult;
import com.pqkj.common.utils.VerificationCodeUtil;
import com.pqkj.entity.SysUser;
import com.pqkj.entity.ZjtTaskUserRelate;
import com.pqkj.service.IZjtTaskUserRelateService;
import com.pqkj.service.IZjtWarningUserRelateService;
import com.pqkj.service.UserService;
import com.pqkj.vo.req.PageReqVO;
import com.pqkj.vo.req.TaskUserRelateAddReqVO;
import com.pqkj.vo.req.WarningRelateAddReqVO;
import com.pqkj.vo.req.ZjryAddReqVO;
import com.pqkj.vo.resp.ZjryRespVO;
import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.pqkj.service.IZjtZjryService;
import com.pqkj.entity.ZjtZjry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;

/**
 * 矫正人员 前端控制器
 *
 * @author zbc
 * @since 2020-06-29
 */
@Api(tags = {"矫正人员"})
@Slf4j
@Controller
@RequestMapping("/")
public class ZjtZjryController {

    @Resource
    private IZjtZjryService zjtZjryService;
    @Resource
    private UserService userService;
    @Resource
    private IZjtTaskUserRelateService zjtTaskUserRelateService;
    @Resource
    private IZjtWarningUserRelateService zjtWarningUserRelateService;
    /**
    * 跳转到页面
    */
    @ApiOperation(value = "后台数据页面")
    @GetMapping("/index/zjtZjry")
    public String zjtZjry() {
    return "zjtZjry/list";
    }

    @ApiOperation(value = "新增矫正人员")
    @PostMapping("zjtZjry/add")
    @RequiresPermissions("zjtZjry:add")
    @ResponseBody
    public DataResult add(@RequestBody ZjryAddReqVO vo){
        zjtZjryService.addZjry(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "删除矫正人员")
    @DeleteMapping("zjtZjry/delete")
    @RequiresPermissions("zjtZjry:delete")
    @ResponseBody
    public DataResult delete(@RequestBody @ApiParam(value = "id集合") List<String> ids){
        zjtZjryService.deleteByIds(ids);
        return DataResult.success();
    }

    @ApiOperation(value = "更新矫正人员")
    @PutMapping("zjtZjry/update")
    @RequiresPermissions("zjtZjry:update")
    @ResponseBody
    public DataResult update(@RequestBody ZjtZjry zjtZjry){
        zjtZjryService.updateById(zjtZjry);
        return DataResult.success();
    }

    @ApiOperation(value = "查询矫正人员分页数据")
    @PostMapping("zjtZjry/listByPage")
    @RequiresPermissions("zjtZjry:list")
    @ResponseBody
    public DataResult findListByPage(@RequestBody ZjtZjry zjtZjry){
        Page page = new Page(zjtZjry.getPage(), zjtZjry.getLimit());
        QueryWrapper<ZjtZjry> queryWrapper = new QueryWrapper();
        //查询条件示例
        if(!StringUtil.isNullorEmpty(zjtZjry.getPhone())){
            queryWrapper.like("phone",zjtZjry.getPhone());
        }
        if(!StringUtil.isNullorEmpty(zjtZjry.getIsOnline())){
            queryWrapper.eq("is_online",zjtZjry.getIsOnline());
        }
        queryWrapper.eq("is_del",0);
        IPage<ZjtZjry> iPage = zjtZjryService.page(page, queryWrapper);
        return DataResult.success(iPage);
    }

    @ApiOperation(value = "配置任务")
    @PostMapping("zjtZjry/configTask")
    @ResponseBody
    public DataResult configTask(@RequestBody TaskUserRelateAddReqVO vo){
        zjtTaskUserRelateService.configTask(vo);
        return DataResult.success();
    }
    @ApiOperation(value = "配置预警")
    @PostMapping("zjtZjry/configWarning")
    @ResponseBody
    public DataResult configWarning(@RequestBody WarningRelateAddReqVO vo){
        zjtWarningUserRelateService.configWarning(vo);
        return DataResult.success();
    }

    @ApiOperation(value = "定位平台对接数据")
    @PostMapping("zjtZjry/findLocation")
    @ResponseBody
    public HashMap findLocation(@RequestBody JSONObject jsonObject){
        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("code",1);
        String user = jsonObject.getString("user");
        String password = jsonObject.getString("password");
        String shbs = jsonObject.getString("shbs");
        String shbsname = jsonObject.getString("shbsname");
        String batch = jsonObject.getString("pch");
        if (StringUtil.isNullorEmpty(user)){
            resultMap.put("msg","user is null!");
            return resultMap;
        }
        if (StringUtil.isNullorEmpty(password)){
            resultMap.put("msg","password is null!");
            return resultMap;
        }
        if (StringUtil.isNullorEmpty(batch)){
            resultMap.put("msg","pch is null!");
            return resultMap;
        }
        if (StringUtil.isNullorEmpty(shbs)){
            resultMap.put("msg","shbs is null!");
            return resultMap;
        }
        if (StringUtil.isNullorEmpty(shbsname)){
            resultMap.put("msg","shbsname is null!");
            return resultMap;
        }
        if(!"北京峰盛博远科技股份有限公司".equals(shbsname)){
            resultMap.put("msg","shbsname is null!");
            return resultMap;
        }
        if(!"BJFSBYKJGFYXGS".equals(shbs)){
            resultMap.put("msg","shbs is null!");
            return resultMap;
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",user);
        queryWrapper.eq("clear_pwd",password);
        SysUser test = userService.getOne(queryWrapper);
        if (test!=null){
            QueryWrapper<ZjtZjry> zjryQueryWrapper = new QueryWrapper();
            zjryQueryWrapper.eq("batch",batch);
            List<ZjtZjry> list = zjtZjryService.list(zjryQueryWrapper);
            resultMap.put("code",200);
            resultMap.put("msg","操作成功");
            resultMap.put("pch",batch);
            List<ZjryRespVO> resultList = new ArrayList<>();
            if (list.size() == 0){
                Page<ZjtZjry> page = new Page<>(0,5000);
                QueryWrapper zjryFiveThousand = new QueryWrapper();
                zjryFiveThousand.isNull("batch");
                IPage<ZjtZjry> iPage = zjtZjryService.page(page, zjryFiveThousand);
                List<ZjtZjry> records = iPage.getRecords();
                if(records.size()>0){
                    for (ZjtZjry record : records) {
                        ZjryRespVO zjryRespVO = new ZjryRespVO();
                        record.setBatch(batch);
                        BeanUtils.copyProperties(record,zjryRespVO);
                        resultList.add(zjryRespVO);
                        zjtZjryService.updateById(record);
                    }
                }
                resultMap.put("location",resultList);
                return resultMap;
            }else{
                for (ZjtZjry zjry : list) {
                    ZjryRespVO zjryRespVO = new ZjryRespVO();
                    BeanUtils.copyProperties(zjry,zjryRespVO);
                    resultList.add(zjryRespVO);
                }
                resultMap.put("location",resultList);
                return resultMap;
            }
        }else{
            resultMap.put("msg","user or password is failed validation!");
            return resultMap;
        }
    }

}
