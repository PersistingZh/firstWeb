package com.pqkj.service;

import com.alibaba.fastjson.JSONObject;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.AppVersion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zbc
 * @since 2020-07-09
 */
public interface IAppVersionService extends IService<AppVersion> {

    JSONObject findVersionsTopOne(String kkApp);

    DataResult addOrUpdate(AppVersion appVersion);
}
