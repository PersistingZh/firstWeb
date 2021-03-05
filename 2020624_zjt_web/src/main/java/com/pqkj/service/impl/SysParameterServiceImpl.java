package com.pqkj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.exception.BusinessException;
import com.pqkj.common.exception.code.BaseResponseCode;
import com.pqkj.entity.SysParameter;
import com.pqkj.mapper.SysParameterMapper;
import com.pqkj.service.ISysParameterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pqkj.vo.req.SysParameterAddVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 系统公用参数 服务实现类
 * </p>
 *
 * @author zbc
 * @since 2020-07-09
 */
@Service
public class SysParameterServiceImpl extends ServiceImpl<SysParameterMapper, SysParameter> implements ISysParameterService {

    @Override
    public void addSysParameter(SysParameterAddVO vo) {
        QueryWrapper parameterWrapper = new QueryWrapper();
        parameterWrapper.eq("parameter_name",vo.getParameterName());
        SysParameter one = super.getOne(parameterWrapper);
        if (one == null){
            SysParameter sysParameter = new SysParameter();
            BeanUtils.copyProperties(vo,sysParameter);
            sysParameter.setId(RandomGUID.getDatetUUID());
            sysParameter.setCreateTime(DateTimeUI.getCurrentDateTime());
            sysParameter.setUpdateTime(DateTimeUI.getCurrentDateTime());
            super.save(sysParameter);
        }else{
            one.setParameterVal(vo.getParameterVal());
            super.updateById(one);
        }

    }
}
