package com.pqkj.service;

import com.pqkj.entity.ZjtZjry;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.vo.req.ZjryAddReqVO;

import java.util.List;

/**
 * <p>
 * 矫正人员 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
public interface IZjtZjryService extends IService<ZjtZjry> {

    void addZjry(ZjryAddReqVO vo);

    void deleteByIds(List<String> ids);
}
