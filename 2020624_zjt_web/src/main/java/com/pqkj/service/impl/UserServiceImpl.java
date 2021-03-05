package com.pqkj.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.StringUtil;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.common.utils.*;
import com.pqkj.entity.SysRole;
import com.pqkj.entity.SysUser;
import com.pqkj.mapper.SysPropertyMapper;
import com.pqkj.mapper.SysUserMapper;
import com.pqkj.service.*;
import com.pqkj.vo.req.*;
import com.pqkj.vo.resp.LoginRespVO;
import com.pqkj.vo.resp.UserOwnRoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private TokenSettings tokenSettings;
    @Value("${spring.redis.allowMultipleLogin}")
    private Boolean allowMultipleLogin;
    @Override
    public String register(RegisterReqVO vo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insert(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        return sysUser.getId();
    }

    @Override
    public LoginRespVO login(LoginReqVO vo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if(!StringUtil.isNullorEmpty(vo.getUsername())){
            queryWrapper.eq("username", vo.getUsername());
        }else if (!StringUtil.isNullorEmpty(vo.getPhone())){
            queryWrapper.eq("phone", vo.getPhone());
        }else{
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.NOT_ACCOUNT);
        }
        if (sysUser.getStatus() == 2) {
            throw new BusinessException(BaseResponseCode.USER_LOCK);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(sysUser.getId()));
        claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(sysUser.getId()));
        claims.put(Constant.JWT_USER_NAME, sysUser.getUsername());
        String access_token = JwtTokenUtil.getAccessToken(sysUser.getId(), claims);
        String refresh_token;
        if(sysUser.getUserType() == 1){
            if (!PasswordUtils.matches(sysUser.getSalt(), vo.getPassword(), sysUser.getPassword())) {
                throw new BusinessException(BaseResponseCode.PASSWORD_ERROR);
            }
            refresh_token = JwtTokenUtil.getRefreshToken(sysUser.getId(), claims);
        }else{
            refresh_token = JwtTokenUtil.getRefreshAppToken(sysUser.getId(), claims);
            if (StringUtil.isNullorEmpty(sysUser.getVerificationCode())||sysUser.getIsVerification() == 1){
                throw new BusinessException(BaseResponseCode.NOT_VERIFICATION_CODE);
            }
            if (StringUtil.isNullorEmpty(vo.getVerificationCode())){
                throw new BusinessException(BaseResponseCode.NO_VERIFICATION_CODE);
            }
            boolean checkVerificationCode = checkVerificationCode(sysUser.getVerificationCode(), vo.getVerificationCode());
            if(!checkVerificationCode){
                throw new BusinessException(BaseResponseCode.NOT_CHECK_VERIFICATION_CODE);
            }
            sysUser.setIsVerification(1);
            sysUser.setVerificationTime(DateTimeUI.getCurrentDateTime());
            super.updateById(sysUser);

        }
        LoginRespVO respVO = new LoginRespVO();
        BeanUtils.copyProperties(sysUser, respVO);
        respVO.setAccessToken(access_token);
        respVO.setRefreshToken(refresh_token);
        //是否删除之前token， 此处控制是否支持多登陆端；
        // true:允许多处登陆; false:只能单处登陆，顶掉之前登陆
//        if (!allowMultipleLogin) {
//            httpSessionService.abortUserByUserName(sysUser.getUsername());
//        }
//
//        String token = httpSessionService.createTokenAndUser(sysUser, getRolesByUserId(sysUser.getId()), getPermissionsByUserId(sysUser.getId()));
//        respVO.setAccessToken(token);
//
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(vo.getUsername(), sysUser.getPassword());
//        subject.login(usernamePasswordToken);
        return respVO;
    }

    private List<String> getRolesByUserId(String userId) {
        return roleService.getRoleNames(userId);
    }

    private Set<String> getPermissionsByUserId(String userId) {
        return permissionService.getPermissionsByUserId(userId);
    }

    @Override
    public String refreshToken(String refreshToken, String accessToken) {
        if (redisService.hasKey(Constant.JWT_ACCESS_TOKEN_BLACKLIST + refreshToken) || !JwtTokenUtil.validateToken(refreshToken)) {
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        String userId = JwtTokenUtil.getUserId(refreshToken);
        log.info("userId={}", userId);
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (null == sysUser) {
            throw new BusinessException(BaseResponseCode.TOKEN_PARSE_ERROR);
        }
        Map<String, Object> claims = null;

        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)) {
            claims = new HashMap<>();
            claims.put(Constant.JWT_ROLES_KEY, getRolesByUserId(userId));
            claims.put(Constant.JWT_PERMISSIONS_KEY, getPermissionsByUserId(userId));
        }
        String newAccessToken = JwtTokenUtil.refreshToken(refreshToken, claims);

        redisService.setifAbsen(Constant.JWT_REFRESH_STATUS + accessToken, userId, 1, TimeUnit.MINUTES);
        if (redisService.hasKey(Constant.JWT_REFRESH_KEY + userId)) {
            redisService.set(Constant.JWT_REFRESH_IDENTIFICATION + newAccessToken, userId, redisService.getExpire(Constant.JWT_REFRESH_KEY + userId, TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
        }
        return newAccessToken;
    }

    @Override
    public void updateUserInfo(UserUpdateReqVO vo, String operationId) {
        SysUser sysUser = sysUserMapper.selectById(vo.getId());
        if (null == sysUser) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setUpdateTime(new Date());
        //设置明文密码
        sysUser.setClearPwd(vo.getPassword());
        if (!StringUtils.isEmpty(vo.getPassword())) {
            String newPassword = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
            sysUser.setPassword(newPassword);
        } else {
            sysUser.setPassword(null);
        }
        sysUser.setUpdateId(operationId);
        int count = sysUserMapper.updateById(sysUser);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        setUserOwnRole(sysUser.getId(), vo.getRoleIds());
        if (vo.getStatus() == 2) {
            redisService.set(Constant.ACCOUNT_LOCK_KEY + sysUser.getId(), sysUser.getId());
        } else {
            redisService.delete(Constant.ACCOUNT_LOCK_KEY + sysUser.getId());
        }
    }

    @Override
    public IPage<SysUser> pageInfo(UserPageReqVO vo) {
        Page page = new Page(vo.getPageNum(), vo.getPageSize());
        IPage<SysUser> iPage = sysUserMapper.selectAll(page, vo);
        return iPage;
    }

    @Override
    public SysUser detailInfo(String userId) {
        return sysUserMapper.selectById(userId);
    }

    @Override
    public IPage<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize, List<String> deptIds) {
        Page page = new Page(pageNum, pageSize);
        IPage<SysUser> iPage = sysUserMapper.selectUserInfoByDeptIds(page, deptIds);
        return iPage;
    }

    @Override
    public void addUser(UserAddReqVO vo) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(vo, sysUser);
        sysUser.setClearPwd(vo.getPassword());
        sysUser.setSalt(PasswordUtils.getSalt());
        String encode = PasswordUtils.encode(vo.getPassword(), sysUser.getSalt());
        sysUser.setPassword(encode);
        sysUser.setCreateTime(new Date());
        int i = sysUserMapper.insert(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if (null != vo.getRoleIds() && !vo.getRoleIds().isEmpty()) {
            UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
            reqVO.setUserId(sysUser.getId());
            reqVO.setRoleIds(vo.getRoleIds());
            userRoleService.addUserRoleInfo(reqVO);
        }
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        log.info("subject.getPrincipals()={}", subject.getPrincipals());
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        String userId = JwtTokenUtil.getUserId(accessToken);
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);
        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

    @Override
    public void updatePwd(UpdatePasswordReqVO vo, String userId, String accessToken, String refreshToken) {
        SysUser sysUser = sysUserMapper.selectById(userId);
        if (sysUser == null) {
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if (!PasswordUtils.matches(sysUser.getSalt(), vo.getOldPwd(), sysUser.getPassword())) {
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        sysUser.setUpdateTime(new Date());
        sysUser.setPassword(PasswordUtils.encode(vo.getNewPwd(), sysUser.getSalt()));
        int i = sysUserMapper.updateById(sysUser);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + accessToken, userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST + refreshToken, userId, JwtTokenUtil.getRemainingTime(refreshToken), TimeUnit.MILLISECONDS);
        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

    @Override
    public List<SysUser> getUserListByDeptId(String deptId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("dept_id", deptId);
        return sysUserMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysUser> getUserListByDeptIds(List<String> deptIds) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("dept_id", deptIds);
        return sysUserMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedUsers(List<String> userIds, String operationId) {
        //删除用户， 删除redis的绑定的角色跟权限
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.select("username").in("id", userIds);
//        List<String> usernames = sysUserMapper.selectObjs(queryWrapper);
//        httpSessionService.abortUserByUserNames(usernames);

        SysUser sysUser = new SysUser();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        sysUser.setDeleted(0);
        int i = sysUserMapper.deletedUsers(sysUser, userIds);
        if (i == 0) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        for (String userId : userIds) {
            redisService.set(Constant.DELETED_USER_KEY + userId, userId, tokenSettings.getRefreshTokenExpireAppTime().toMillis(), TimeUnit.MILLISECONDS);
            //清空权鉴缓存
            redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
        }
    }

    @Override
    public UserOwnRoleRespVO getUserOwnRole(String userId) {
        List<String> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
        List<SysRole> list = roleService.selectAllRoles();
        UserOwnRoleRespVO vo = new UserOwnRoleRespVO();
        vo.setAllRole(list);
        vo.setOwnRoles(roleIdsByUserId);
        return vo;
    }

    @Override
    public void setUserOwnRole(String userId, List<String> roleIds) {
        userRoleService.removeByUserId(userId);
        if (null != roleIds && !roleIds.isEmpty()) {
            UserRoleOperationReqVO reqVO = new UserRoleOperationReqVO();
            reqVO.setUserId(userId);
            reqVO.setRoleIds(roleIds);
            userRoleService.addUserRoleInfo(reqVO);
        }
        redisService.set(Constant.JWT_REFRESH_KEY + userId, userId, tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
        //清空权鉴缓存
        redisService.delete(Constant.IDENTIFY_CACHE_KEY + userId);
    }

    /**
     * 测试多数据源
     * @return
     */
//    @DS("slave_1")
//    @Override
//    public List<SysProperty> selectByCondition() {
//        return sysPropertyMapper.selectList(new QueryWrapper<>());
//    }

    @Override
    public DataResult getVerificationCode(SysUser sysUser) {
        if (StrUtil.isNotEmpty(sysUser.getPhone())) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("phone", sysUser.getPhone());
            SysUser userBase = super.getOne(queryWrapper);
            if(userBase.getUserType() == 2){
                String verificationCode = VerificationCodeUtil.getVerificationCode(6,sysUser.getPhone());
                userBase.setIsVerification(0);
                userBase.setUpdateTime(new Date());
                userBase.setVerificationCodeTime(DateTimeUI.getCurrentDateTime());
                userBase.setVerificationCode(verificationCode);
                super.updateById(userBase);
                return DataResult.success(verificationCode);
            }else{
                return DataResult.fail("该手机号码不是APP用户");
            }

        } else {
            return DataResult.fail("请输入手机号码");
        }
    }

    public static boolean checkVerificationCode(String verificationCode,String verificationCodeBase) {
        if (verificationCode.equals(verificationCodeBase)) {
            return true;
        } else {
            return false;
        }
    }
}
