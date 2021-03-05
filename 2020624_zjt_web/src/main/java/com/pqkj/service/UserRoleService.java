package com.pqkj.service;

import com.pqkj.vo.req.UserRoleOperationReqVO;

import java.util.List;

public interface UserRoleService {

    int removeByRoleId(String roleId);

    List<String> getRoleIdsByUserId(String userId);


    void addUserRoleInfo(UserRoleOperationReqVO vo);

    int removeByUserId(String userId);


    List<String> getUserIdsByRoleIds(List<String> roleIds);

}
