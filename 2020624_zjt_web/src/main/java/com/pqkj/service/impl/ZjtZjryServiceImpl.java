package com.pqkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.entity.ZjtTask;
import com.pqkj.entity.ZjtZjry;
import com.pqkj.mapper.ZjtZjryMapper;
import com.pqkj.service.IZjtTaskUserRelateService;
import com.pqkj.service.IZjtWarningRecordService;
import com.pqkj.service.IZjtWarningUserRelateService;
import com.pqkj.service.IZjtZjryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pqkj.vo.req.ZjryAddReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 矫正人员 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
@Service
public class ZjtZjryServiceImpl extends ServiceImpl<ZjtZjryMapper, ZjtZjry> implements IZjtZjryService {

    @Resource
    private ZjtZjryMapper zjtZjryMapper;
    @Resource
    private IZjtWarningUserRelateService zjtWarningUserRelateService;
    @Resource
    private IZjtTaskUserRelateService zjtTaskUserRelateService;
    @Override
    public void addZjry(ZjryAddReqVO vo) {
        ZjtZjry zjtZjry = new ZjtZjry();
        BeanUtils.copyProperties(vo, zjtZjry);
        zjtZjry.setId(RandomGUID.getDatetUUID());
        zjtZjry.setCreateTime(DateTimeUI.getCurrentDateTime());
        zjtZjry.setUpdateTime(DateTimeUI.getCurrentDateTime());
        zjtZjry.setIsDel(0);
        int i = zjtZjryMapper.insert(zjtZjry);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    @Override
    public void deleteByIds(List<String> ids) {
        for (String id : ids) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id",id);
            zjtWarningUserRelateService.remove(queryWrapper);
            zjtTaskUserRelateService.remove(queryWrapper);
            UpdateWrapper<ZjtZjry> updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id",id).set("is_del",1);
            super.update(updateWrapper);
        }
    }
}
