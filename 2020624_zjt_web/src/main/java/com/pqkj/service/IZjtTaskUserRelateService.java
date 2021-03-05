package com.pqkj.service;

import com.pqkj.entity.ZjtTaskUserRelate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pqkj.vo.req.TaskUserRelateAddReqVO;

/**
 * <p>
 * 任务与用户关系表 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
public interface IZjtTaskUserRelateService extends IService<ZjtTaskUserRelate> {

    void addTaskUserRelate(TaskUserRelateAddReqVO vo);

    void configTask(TaskUserRelateAddReqVO vo);
}
