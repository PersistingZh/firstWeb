package com.pqkj.service.impl;

import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.entity.ZjtTaskUserRelate;
import com.pqkj.entity.ZjtWarningUserRelate;
import com.pqkj.mapper.ZjtWarningUserRelateMapper;
import com.pqkj.service.IZjtWarningUserRelateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pqkj.vo.req.WarningRelateAddReqVO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 预警与用户关系表 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-07-03
 */
@Service
public class ZjtWarningUserRelateServiceImpl extends ServiceImpl<ZjtWarningUserRelateMapper, ZjtWarningUserRelate> implements IZjtWarningUserRelateService {

    @Override
    public void addWarningRelate(WarningRelateAddReqVO vo) {
        ZjtWarningUserRelate zjtWarningUserRelate = new ZjtWarningUserRelate();
        List<String> userIds = vo.getUserIds();
        List<String> warningIds = vo.getWarningIds();
        for (String userId : userIds) {
            zjtWarningUserRelate.setUserId(userId);
            for (String warningId : warningIds) {
                //删除之前绑定的任务
                HashMap<String,Object> deleteMap = new HashMap<>();
                deleteMap.put("user_id",userId);
                deleteMap.put("warning_id",warningId);
                super.removeByMap(deleteMap);
                //重新绑定任务
                zjtWarningUserRelate.setId(RandomGUID.getDatetUUID());
                zjtWarningUserRelate.setCreateTime(DateTimeUI.getCurrentDateTime());
                zjtWarningUserRelate.setUpdateTime(DateTimeUI.getCurrentDateTime());
                zjtWarningUserRelate.setWarningId(warningId);
                super.save(zjtWarningUserRelate);
            }
        }
    }
    @Override
    public void configWarning(WarningRelateAddReqVO vo) {
        ZjtWarningUserRelate zjtWarningUserRelate = new ZjtWarningUserRelate();
        List<String> userIds = vo.getUserIds();
        List<String> warningIds = vo.getWarningIds();
        for (String userId : userIds) {
            //删除之前绑定的任务
            HashMap<String,Object> deleteMap = new HashMap<>();
            deleteMap.put("user_id",userId);
            super.removeByMap(deleteMap);
            zjtWarningUserRelate.setUserId(userId);
            for (String warningId : warningIds) {
                //重新绑定任务
                zjtWarningUserRelate.setId(RandomGUID.getDatetUUID());
                zjtWarningUserRelate.setCreateTime(DateTimeUI.getCurrentDateTime());
                zjtWarningUserRelate.setUpdateTime(DateTimeUI.getCurrentDateTime());
                zjtWarningUserRelate.setWarningId(warningId);
                super.save(zjtWarningUserRelate);
            }
        }
    }
}
