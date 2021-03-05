package com.pqkj.service;

import com.pqkj.entity.SysParameter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.vo.req.SysParameterAddVO;

/**
 * <p>
 * 系统公用参数 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-07-09
 */
public interface ISysParameterService extends IService<SysParameter> {

    void addSysParameter(SysParameterAddVO vo);
}
