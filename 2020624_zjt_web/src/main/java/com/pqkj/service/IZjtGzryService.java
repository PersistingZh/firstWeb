package com.pqkj.service;

import com.pqkj.entity.ZjtGzry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.vo.req.GzryAddReqVO;

/**
 * <p>
 * 工作人员 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
public interface IZjtGzryService extends IService<ZjtGzry> {

    void addGzry(GzryAddReqVO vo);
}
