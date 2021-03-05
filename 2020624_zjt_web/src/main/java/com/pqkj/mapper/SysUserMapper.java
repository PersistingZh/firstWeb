package com.pqkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.entity.SysUser;
import com.pqkj.vo.req.UserPageReqVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<SysUser> selectAll(Page page, @Param("vo") UserPageReqVO vo);

    IPage<SysUser> selectUserInfoByDeptIds (Page page, List<String> deptIds);

    int deletedUsers(@Param("sysUser") SysUser sysUser,@Param("list") List<String> list);
}