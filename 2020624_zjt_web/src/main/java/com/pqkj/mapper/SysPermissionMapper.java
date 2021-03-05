package com.pqkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pqkj.entity.SysPermission;

import java.util.List;

public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    List<SysPermission> selectInfoByIds (List<String> ids);

}