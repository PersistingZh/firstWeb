package com.pqkj.common.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.common.utils.Constant;
import com.pqkj.common.utils.JwtTokenUtil;
import com.pqkj.entity.SysUser;
import com.pqkj.service.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CustomRealm extends AuthorizingRealm {
    @Autowired
    @Lazy
    private RedisService redisService;

    @Autowired
    @Lazy
    private PermissionService permissionService;
    @Autowired
    @Lazy
    private RoleService roleService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        if (httpSessionService.getCurrentSession() == null) {
//            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
//        }
//        if (httpSessionService.getCurrentSession().get(Constant.ROLES_KEY) != null) {
//            authorizationInfo.addRoles((Collection<String>) httpSessionService.getCurrentSession().get(Constant.ROLES_KEY));
//        }
//        if (httpSessionService.getCurrentSession().get(Constant.PERMISSIONS_KEY) != null) {
//            authorizationInfo.addStringPermissions((Collection<String>) httpSessionService.getCurrentSession().get(Constant.PERMISSIONS_KEY));
//        }
        String accessToken= (String) SecurityUtils.getSubject().getPrincipal();
        String userId= JwtTokenUtil.getUserId(accessToken);
        log.info("userId={}",userId);
        if(redisService.hasKey(Constant.JWT_REFRESH_KEY+userId)&&redisService.getExpire(Constant.JWT_REFRESH_KEY+userId, TimeUnit.MILLISECONDS)>JwtTokenUtil.getRemainingTime(accessToken)){
            List<String> roleNames=roleService.getRoleNames(userId);
            if(roleNames!=null&&!roleNames.isEmpty()){
                authorizationInfo.addRoles(roleService.getRoleNames(userId));
            }
            authorizationInfo.setStringPermissions(permissionService.getPermissionsByUserId(userId));
        }else {
            Claims claimsFromToken = JwtTokenUtil.getClaimsFromToken(accessToken);
            if(claimsFromToken.get(Constant.JWT_ROLES_KEY)!=null){
                authorizationInfo.addRoles((Collection<String>) claimsFromToken.get(Constant.JWT_ROLES_KEY));
            }
            if(claimsFromToken.get(Constant.JWT_PERMISSIONS_KEY)!=null){
                authorizationInfo.addStringPermissions((Collection<String>) claimsFromToken.get(Constant.JWT_PERMISSIONS_KEY));
            }
        }
        return authorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        CustomPasswordToken token= (CustomPasswordToken) authenticationToken;
        SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo(token.getPrincipal(),token.getPrincipal(),getName());
        return simpleAuthenticationInfo;
//        System.out.println("============用户验证==============");
//        //从token中获取信息,此token只是shiro用于身份验证的,并非前端传过来的token.
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        String username = token.getUsername();
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("username", username);
//        SysUser sysUser = userService.getOne(queryWrapper);
//        String password = sysUser.getPassword();
//
//        if (null == password) {
//            throw new AuthenticationException("doGetAuthenticationInfo中的用户名不对");
//        } else if (!password.equals(new String(token.getPassword()))) {
//            throw new AuthenticationException("doGetAuthenticationInfo中的密码不对");
//        }
//        //组合一个验证信息
//        System.out.println("token.getPrincipal()默认返回的username======" + token.getPrincipal());
//        System.out.println("getName()" + getName());
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(), password, getName());
//        return info;
    }
}
