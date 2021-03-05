package com.pqkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.AppVersion;
import com.pqkj.mapper.AppVersionMapper;
import com.pqkj.service.IAppVersionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-07-09
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {

    @Override
    public JSONObject findVersionsTopOne(String appType) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("app_type",appType);
        queryWrapper.eq("is_online",1);
        queryWrapper.orderByDesc("create_time");
        AppVersion appVersion = super.getOne(queryWrapper);
        if(appVersion!=null){
            return new JSONObject()
                    .fluentPut("version", appVersion.getVersionCode())
                    .fluentPut("versionName", appVersion.getVersionName())
                    .fluentPut("versionPath",appVersion.getVersionPath())
                    .fluentPut("appName", appVersion.getAppName())
                    .fluentPut("apkName", appVersion.getApkName())
                    .fluentPut("remark",appVersion.getRemark());
        }else{
            return null;
        }
    }

    @Override
    public DataResult addOrUpdate(AppVersion appVersion) {
        DataResult dataResult = DataResult.success();
        if (StringUtil.isNullorEmpty(appVersion.getId())){
            appVersion.setCreateTime(DateTimeUI.getCurrentDateTime());
            appVersion.setUpdateTime(DateTimeUI.getCurrentDateTime());
            appVersion.setId(RandomGUID.getDatetUUID());
            if(StringUtil.isNullorEmpty(appVersion.getIsOnline())){
                appVersion.setIsOnline(0);
            }else if (appVersion.getIsOnline() == 1){
                UpdateWrapper<AppVersion> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("is_online",0);
                super.update(updateWrapper);
            }
            super.save(appVersion);
            dataResult.setData("添加成功");
        }else{
            appVersion.setUpdateTime(DateTimeUI.getCurrentDateTime());
            if(appVersion.getIsOnline() == 1){
                UpdateWrapper<AppVersion> updateWrapper = new UpdateWrapper<>();
                updateWrapper.set("is_online",0).ne("id",appVersion.getId());
                super.update(updateWrapper);
            }
            super.updateById(appVersion);
            dataResult.setData("编辑成功");
        }
        return dataResult;
    }
}
