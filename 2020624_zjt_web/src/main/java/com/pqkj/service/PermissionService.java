package com.pqkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.entity.SysPermission;
import com.pqkj.vo.req.PermissionAddReqVO;
import com.pqkj.vo.req.PermissionPageReqVO;
import com.pqkj.vo.req.PermissionUpdateReqVO;
import com.pqkj.vo.resp.PermissionRespNode;

import java.util.List;
import java.util.Set;

public interface PermissionService extends IService<SysPermission> {

    List<SysPermission> getPermission(String userId);

    SysPermission addPermission(PermissionAddReqVO vo);

    SysPermission detailInfo(String permissionId);

    void updatePermission(PermissionUpdateReqVO vo);

    void delete(String permissionId);

    IPage<SysPermission> pageInfo(PermissionPageReqVO vo);

    List<SysPermission> selectAll();

    Set<String> getPermissionsByUserId(String userId);

    List<PermissionRespNode> permissionTreeList(String userId);

    List<PermissionRespNode> selectAllByTree();

    List<PermissionRespNode> selectAllMenuByTree(String permissionId);

}
