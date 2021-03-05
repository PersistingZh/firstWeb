package com.pqkj.service;

import com.pqkj.entity.ZjtWarningUserRelate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.vo.req.WarningRelateAddReqVO;

/**
 * <p>
 * 预警与用户关系表 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-07-03
 */
public interface IZjtWarningUserRelateService extends IService<ZjtWarningUserRelate> {

    void addWarningRelate(WarningRelateAddReqVO vo);

    void configWarning(WarningRelateAddReqVO vo);
}
