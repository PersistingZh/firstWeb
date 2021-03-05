package com.pqkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.ZjtTaskUserRecord;
import com.pqkj.entity.ZjtTaskUserRelate;
import com.pqkj.mapper.ZjtTaskUserRecordMapper;
import com.pqkj.mapper.ZjtTaskUserRelateMapper;
import com.pqkj.service.IZjtTaskUserRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户与任务记录表 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Service
public class ZjtTaskUserRecordServiceImpl extends ServiceImpl<ZjtTaskUserRecordMapper, ZjtTaskUserRecord> implements IZjtTaskUserRecordService {

    @Override
    public void saveTaskUserRecord(ZjtTaskUserRecord zjtTaskUserRecord){
        zjtTaskUserRecord.setCreateTime(DateTimeUI.getCurrentDateTime());
        zjtTaskUserRecord.setId(RandomGUID.getDatetUUID());
        super.save(zjtTaskUserRecord);
    }

}
