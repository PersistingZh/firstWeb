package com.pqkj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.vo.req.SysLogPageReqVO;
import com.pqkj.entity.SysLog;
import org.apache.ibatis.annotations.Param;

public interface SysLogMapper extends BaseMapper<SysLog> {


    IPage<SysLog> selectAll(Page page, @Param(value = "vo") SysLogPageReqVO vo);
}