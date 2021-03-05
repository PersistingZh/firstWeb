package com.pqkj.service;

import com.pqkj.entity.ZjtTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.vo.req.TaskAddReqVO;

/**
 * <p>
 * 任务表 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
public interface IZjtTaskService extends IService<ZjtTask> {

    void addTask(TaskAddReqVO vo);
}
