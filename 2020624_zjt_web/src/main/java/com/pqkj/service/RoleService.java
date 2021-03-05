package com.pqkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pqkj.entity.SysRole;
import com.pqkj.vo.req.RoleAddReqVO;
import com.pqkj.vo.req.RolePageReqVO;
import com.pqkj.vo.req.RoleUpdateReqVO;

import java.util.List;

public interface RoleService {

    SysRole addRole(RoleAddReqVO vo);

    void updateRole(RoleUpdateReqVO vo, String accessToken);

    SysRole detailInfo(String id);

    void deletedRole(String id);

    IPage<SysRole> pageInfo(RolePageReqVO vo);

    List<SysRole> getRoleInfoByUserId(String userId);

    List<String> getRoleNames(String userId);

    List<SysRole> selectAllRoles();
}
