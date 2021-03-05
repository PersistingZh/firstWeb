package com.pqkj.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pqkj.entity.SysGenerator;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zbc
 * @since 2020-03-20
 */
public interface ISysGeneratorService extends IService<SysGenerator> {


    IPage<SysGenerator> selectAllTables(Page page, SysGenerator vo);

    void gen(SysGenerator sysGenerator);
}
