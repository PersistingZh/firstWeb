package com.pqkj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.entity.SysRolePermission;
import com.pqkj.vo.req.RolePermissionOperationReqVO;

import java.util.List;

public interface RolePermissionService extends IService<SysRolePermission> {

    int removeByRoleId(String roleId);

    List<String> getPermissionIdsByRoles(List<String> roleIds);

    void addRolePermission(RolePermissionOperationReqVO vo);

    int removeByPermissionId(String permissionId);

    List<String> getRoleIds(String permissionId);
    List<String> getPermissionIdsByRoleId(String roleId);

}
