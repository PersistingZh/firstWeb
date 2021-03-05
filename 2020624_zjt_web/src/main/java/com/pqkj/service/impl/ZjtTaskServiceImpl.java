package com.pqkj.service.impl;

import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.entity.ZjtTask;
import com.pqkj.mapper.ZjtTaskMapper;
import com.pqkj.service.IZjtTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pqkj.vo.req.TaskAddReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 任务表 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Service
public class ZjtTaskServiceImpl extends ServiceImpl<ZjtTaskMapper, ZjtTask> implements IZjtTaskService {

    @Resource
    private ZjtTaskMapper zjtTaskMapper;

    @Override
    public void addTask(TaskAddReqVO vo) {
        ZjtTask task = new ZjtTask();
        BeanUtils.copyProperties(vo, task);
        task.setId(RandomGUID.getDatetUUID());
        task.setCreateTime(DateTimeUI.getCurrentDateTime());
        task.setUpdateTime(DateTimeUI.getCurrentDateTime());
        task.setTaskNum(DateTimeUI.getCurrentDateTime().replaceAll("-","").replaceAll(" ","").replaceAll(":",""));
        int i = zjtTaskMapper.insert(task);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }
}
