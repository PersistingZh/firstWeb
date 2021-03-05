package com.pqkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pqkj.entity.SysLog;
import com.pqkj.vo.req.SysLogPageReqVO;

import java.util.List;

public interface LogService {

    IPage<SysLog> pageInfo(SysLogPageReqVO vo);

    void delete(List<String> logIds);
}
