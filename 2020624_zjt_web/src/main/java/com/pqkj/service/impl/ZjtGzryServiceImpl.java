package com.pqkj.service.impl;

import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.entity.ZjtGzry;
import com.pqkj.entity.ZjtZjry;
import com.pqkj.mapper.ZjtGzryMapper;
import com.pqkj.service.IZjtGzryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pqkj.vo.req.GzryAddReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 工作人员 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
@Service
public class ZjtGzryServiceImpl extends ServiceImpl<ZjtGzryMapper, ZjtGzry> implements IZjtGzryService {

    @Resource
    private ZjtGzryMapper zjtGzryMapper;

    @Override
    public void addGzry(GzryAddReqVO vo) {
        ZjtGzry zjtGzry = new ZjtGzry();
        BeanUtils.copyProperties(vo, zjtGzry);
        zjtGzry.setId(RandomGUID.getDatetUUID());
        zjtGzry.setCreateTime(DateTimeUI.getCurrentDateTime());
        zjtGzry.setUpdateTime(DateTimeUI.getCurrentDateTime());
        zjtGzry.setIsDel(0);
        int i = zjtGzryMapper.insert(zjtGzry);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }
}
