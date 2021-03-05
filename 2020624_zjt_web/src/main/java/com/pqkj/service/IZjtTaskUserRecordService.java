package com.pqkj.service;

import com.alibaba.fastjson.JSONObject;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.ZjtTaskUserRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户与任务记录表 服务类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
public interface IZjtTaskUserRecordService extends IService<ZjtTaskUserRecord> {

    void saveTaskUserRecord(ZjtTaskUserRecord zjtTaskUserRecord);

}
