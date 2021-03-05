package com.pqkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.common.utils.Constant;
import com.pqkj.common.utils.TokenSettings;
import com.pqkj.entity.SysRole;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.mapper.SysRoleMapper;
import com.pqkj.mapper.SysUserRoleMapper;
import com.pqkj.service.*;
import com.pqkj.vo.req.RoleAddReqVO;
import com.pqkj.vo.req.RolePageReqVO;
import com.pqkj.vo.req.RolePermissionOperationReqVO;
import com.pqkj.vo.req.RoleUpdateReqVO;
import com.pqkj.vo.resp.PermissionRespNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private TokenSettings tokenSettings;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysRole addRole(RoleAddReqVO vo) {

        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(vo, sysRole);
        sysRole.setCreateTime(new Date());
        int count = sysRoleMapper.insert(sysRole);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        if (null != vo.getPermissions() && !vo.getPermissions().isEmpty()) {
            RolePermissionOperationReqVO reqVO = new RolePermissionOperationReqVO();
            reqVO.setRoleId(sysRole.getId());
            reqVO.setPermissionIds(vo.getPermissions());
            rolePermissionService.addRolePermission(reqVO);
        }

        return sysRole;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(RoleUpdateReqVO vo, String accessToken) {
        SysRole sysRole = sysRoleMapper.selectById(vo.getId());
        if (null == sysRole) {
            log.error("传入 的 id:{}不合法", vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        SysRole update = new SysRole();
        BeanUtils.copyProperties(vo, update);
        update.setUpdateTime(new Date());
        int count = sysRoleMapper.updateById(update);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        rolePermissionService.removeByRoleId(sysRole.getId());
        if (null != vo.getPermissions() && !vo.getPermissions().isEmpty()) {
            RolePermissionOperationReqVO reqVO = new RolePermissionOperationReqVO();
            reqVO.setRoleId(sysRole.getId());
            reqVO.setPermissionIds(vo.getPermissions());
            rolePermissionService.addRolePermission(reqVO);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("user_id").eq("role_id", vo.getId());

            List<String> userIds = sysUserRoleMapper.selectObjs(queryWrapper);
            if(!userIds.isEmpty()){
                for (String userId:userIds){
                    redisService.set(Constant.JWT_REFRESH_KEY +userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                    //清空权鉴缓存
                    redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
                }

            }

        }

    }

    @Override
    public SysRole detailInfo(String id) {
        SysRole sysRole = sysRoleMapper.selectById(id);
        if (sysRole == null) {
            log.error("传入 的 id:{}不合法", id);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        List<PermissionRespNode> permissionRespNodes = permissionService.selectAllByTree();
        Set<String> checkList = new HashSet<>(rolePermissionService.getPermissionIdsByRoleId(sysRole.getId()));
        setheckced(permissionRespNodes, checkList);
        sysRole.setPermissionRespNodes(permissionRespNodes);
        return sysRole;
    }


    private void setheckced(List<PermissionRespNode> list, Set<String> checkList) {

        for (PermissionRespNode node : list) {

            if (checkList.contains(node.getId()) && (node.getChildren() == null || node.getChildren().isEmpty())) {
                node.setChecked(true);
            }
            setheckced((List<PermissionRespNode>) node.getChildren(), checkList);

        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletedRole(String id) {
        SysRole sysRole = new SysRole();
        sysRole.setId(id);
        sysRole.setUpdateTime(new Date());
        sysRole.setDeleted(0);
        int count = sysRoleMapper.updateById(sysRole);
        if (count != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("user_id").eq("role_id", id);
        List<String> userIds = sysUserRoleMapper.selectObjs(queryWrapper);
        rolePermissionService.removeByRoleId(id);
        userRoleService.removeByRoleId(id);
        if(!userIds.isEmpty()){
            for (String userId:userIds){
                redisService.set(Constant.JWT_REFRESH_KEY +userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
            }
        }
    }

    @Override
    public IPage<SysRole> pageInfo(RolePageReqVO vo) {
        Page page = new Page(vo.getPageNum(), vo.getPageSize());
        return sysRoleMapper.selectAll(page, vo);
    }

    @Override
    public List<SysRole> getRoleInfoByUserId(String userId) {

        List<String> roleIds = userRoleService.getRoleIdsByUserId(userId);
        if (roleIds.isEmpty()) {
            return null;
        }
        return sysRoleMapper.selectBatchIds(roleIds);
    }

    @Override
    public List<String> getRoleNames(String userId) {

        List<SysRole> sysRoles = getRoleInfoByUserId(userId);
        if (null == sysRoles || sysRoles.isEmpty()) {
            return null;
        }
        List<String> list = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            list.add(sysRole.getName());
        }
        return list;
    }

    @Override
    public List<SysRole> selectAllRoles() {
        return sysRoleMapper.selectList(null);
    }

}
