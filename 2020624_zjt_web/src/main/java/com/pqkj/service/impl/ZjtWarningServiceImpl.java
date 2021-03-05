package com.pqkj.service.impl;

import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.entity.ZjtTaskUserRelate;
import com.pqkj.entity.ZjtWarning;
import com.pqkj.mapper.ZjtWarningMapper;
import com.pqkj.service.IZjtWarningService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pqkj.vo.req.WarningAddReqVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Service
public class ZjtWarningServiceImpl extends ServiceImpl<ZjtWarningMapper, ZjtWarning> implements IZjtWarningService {

    @Resource
    private ZjtWarningMapper zjtWarningMapper;
    @Override
    public void addWarning(WarningAddReqVO vo) {
        ZjtWarning warning = new ZjtWarning();
        BeanUtils.copyProperties(vo, warning);
        warning.setId(RandomGUID.getDatetUUID());
        warning.setWarningTime(DateTimeUI.getCurrentDateTime());
        int i = zjtWarningMapper.insert(warning);
        if (i != 1) {
            throw new BusinessException(BaseResponseCode.OPERATION_ERRO);
        }
    }

    public static void main(String[] args) {
        double lat1 = 39.910925,lat = 39.910925,lng1 = 116.413384, lng = 116.413384;
        int raduis = 15;
        double R = 6378137.0;
        double dLat = (lat1 - lat) * Math.PI / 180;
        double dLng = (lng1 - lng) * Math.PI / 180;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat * Math.PI / 180) * Math.cos(lat1 * Math.PI / 180) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        double dis = Math.round(d);

        if (dis <= raduis){
            //点在圆内
            System.out.println("点在圆内");
        }else {
            //点不在圆内
            System.out.println("点不在圆内");
        }
    }
}
