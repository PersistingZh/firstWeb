package com.pqkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.SysUser;
import com.pqkj.vo.req.*;
import com.pqkj.vo.resp.LoginRespVO;
import com.pqkj.vo.resp.UserOwnRoleRespVO;

import java.util.List;

public interface UserService extends IService<SysUser> {

    String register(RegisterReqVO vo);

    LoginRespVO login(LoginReqVO vo);

    String refreshToken(String refreshToken, String accessToken);

    void updateUserInfo(UserUpdateReqVO vo, String operationId);


    IPage<SysUser> pageInfo(UserPageReqVO vo);

    SysUser detailInfo(String userId);

    IPage<SysUser> selectUserInfoByDeptIds(int pageNum, int pageSize, List<String> deptIds);

    void addUser(UserAddReqVO vo);

    void logout(String accessToken, String refreshToken);

    void updatePwd(UpdatePasswordReqVO vo, String userId, String accessToken, String refreshToken);

    List<SysUser> getUserListByDeptId(String deptId);

    List<SysUser> getUserListByDeptIds(List<String> deptIds);

    void deletedUsers(List<String> userIds, String operationId);

    UserOwnRoleRespVO getUserOwnRole(String userId);

    void setUserOwnRole(String userId, List<String> roleIds);
//    @DS("slave_1")
//    @Override
//    public List<SysProperty> selectByCondition() {
//        return sysPropertyMapper.selectList(new QueryWrapper<>());
//    }

    //    @DS("slave_1")
//    @Override
//    public List<SysProperty> selectByCondition() {
//        return sysPropertyMapper.selectList(new QueryWrapper<>());
//    }

    DataResult getVerificationCode(SysUser sysUser);

    /**
     * 测试多数据源
     * @return
     */
//    List<SysProperty> selectByCondition();
}
