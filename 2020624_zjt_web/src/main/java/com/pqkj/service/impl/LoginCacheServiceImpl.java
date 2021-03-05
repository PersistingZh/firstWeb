package com.pqkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pqkj.entity.LoginCache;
import com.pqkj.mapper.LoginCacheMapper;
import com.pqkj.service.ILoginCacheService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录缓存表 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-07-04
 */
@Service
public class LoginCacheServiceImpl extends ServiceImpl<LoginCacheMapper, LoginCache> implements ILoginCacheService {

    @Override
    public LoginCache getLoginCacheByUserId(String userId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        return super.getOne(queryWrapper);
    }

    @Override
    public boolean checkMacCode(String userId, String macCode) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        LoginCache loginCache = super.getOne(queryWrapper);
        if(loginCache!=null){
            if (loginCache.getMacCode().equals(macCode)){
                return true;
            }
        }
        return false;
    }
}
