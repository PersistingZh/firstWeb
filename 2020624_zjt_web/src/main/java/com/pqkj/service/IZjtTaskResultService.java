package com.pqkj.service;

import com.pqkj.entity.ZjtTaskResult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 任务结果 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
public interface IZjtTaskResultService extends IService<ZjtTaskResult> {

    void addTaskResult(ZjtTaskResult zjtTaskResult);
}
