package com.pqkj.service.impl;

import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.entity.ZjtTaskResult;
import com.pqkj.mapper.ZjtTaskResultMapper;
import com.pqkj.service.IZjtTaskResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 任务结果 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
@Service
public class ZjtTaskResultServiceImpl extends ServiceImpl<ZjtTaskResultMapper, ZjtTaskResult> implements IZjtTaskResultService {

    @Resource
    private ZjtTaskResultMapper zjtTaskResultMapper;
    @Override
    public void addTaskResult(ZjtTaskResult zjtTaskResult) {
        zjtTaskResult.setId(RandomGUID.getDatetUUID());
        zjtTaskResult.setCreateTime(DateTimeUI.getCurrentDateTime());
        zjtTaskResult.setIsFinish(0);
        zjtTaskResult.setStartTime(DateTimeUI.getCurrentDate()+" "+zjtTaskResult.getStartTime());
        zjtTaskResult.setEndTime(DateTimeUI.getCurrentDate()+" "+zjtTaskResult.getEndTime());
        int insert = zjtTaskResultMapper.insert(zjtTaskResult);
        if(insert != 1 ){
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }
}
