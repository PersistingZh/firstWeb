package com.pqkj.service;

import com.pqkj.entity.LoginCache;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 登录缓存表 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-07-04
 */
public interface ILoginCacheService extends IService<LoginCache> {

    LoginCache getLoginCacheByUserId(String userId);

    boolean checkMacCode(String userId,String macCode);
}
