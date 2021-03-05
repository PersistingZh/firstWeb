package com.pqkj.service;

import com.pqkj.entity.ZjtWarning;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.vo.req.WarningAddReqVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
public interface IZjtWarningService extends IService<ZjtWarning> {

    void addWarning(WarningAddReqVO vo);
}
