package com.pqkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.entity.SysUser;
import com.pqkj.entity.ZjtTaskUserRelate;
import com.pqkj.mapper.ZjtTaskUserRelateMapper;
import com.pqkj.service.IZjtTaskUserRelateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pqkj.vo.req.TaskUserRelateAddReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 任务与用户关系表 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Service
public class ZjtTaskUserRelateServiceImpl extends ServiceImpl<ZjtTaskUserRelateMapper, ZjtTaskUserRelate> implements IZjtTaskUserRelateService {

    @Resource
    private ZjtTaskUserRelateMapper zjtTaskUserRelateMapper;
    @Override
    public void addTaskUserRelate(TaskUserRelateAddReqVO vo) {
        ZjtTaskUserRelate taskUserRelate = new ZjtTaskUserRelate();
        List<String> userIds = vo.getUserIds();
        List<String> taskIds = vo.getTaskIds();
        for (String userId : userIds) {
            taskUserRelate.setUserId(userId);
            for (String taskId : taskIds) {
                //删除之前绑定的任务
                HashMap<String,Object> deleteMap = new HashMap<>();
                deleteMap.put("user_id",userId);
                deleteMap.put("task_id",taskId);
                taskUserRelate.setUserId(userId);
                super.removeByMap(deleteMap);
                //重新绑定任务
                taskUserRelate.setId(RandomGUID.getDatetUUID());
                taskUserRelate.setCreateTime(DateTimeUI.getCurrentDateTime());
                taskUserRelate.setUpdateTime(DateTimeUI.getCurrentDateTime());
                taskUserRelate.setTaskId(taskId);
                super.save(taskUserRelate);
            }
        }
    }

    @Override
    public void configTask(TaskUserRelateAddReqVO vo) {
        ZjtTaskUserRelate taskUserRelate = new ZjtTaskUserRelate();
        List<String> userIds = vo.getUserIds();
        List<String> taskIds = vo.getTaskIds();
        for (String userId : userIds) {
            //删除之前绑定的任务
            HashMap<String,Object> deleteMap = new HashMap<>();
            deleteMap.put("user_id",userId);
            taskUserRelate.setUserId(userId);
            super.removeByMap(deleteMap);
            taskUserRelate.setUserId(userId);
            for (String taskId : taskIds) {
                //重新绑定任务
                taskUserRelate.setId(RandomGUID.getDatetUUID());
                taskUserRelate.setCreateTime(DateTimeUI.getCurrentDateTime());
                taskUserRelate.setUpdateTime(DateTimeUI.getCurrentDateTime());
                taskUserRelate.setTaskId(taskId);
                super.save(taskUserRelate);
            }
        }
    }
}
