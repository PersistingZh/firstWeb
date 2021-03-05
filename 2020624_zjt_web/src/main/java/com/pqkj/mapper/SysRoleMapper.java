package com.pqkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.vo.req.RolePageReqVO;
import com.pqkj.entity.SysRole;
import org.apache.ibatis.annotations.Param;

public interface SysRoleMapper extends BaseMapper<SysRole> {

    IPage<SysRole> selectAll(Page page, @Param(value = "vo") RolePageReqVO vo);

}