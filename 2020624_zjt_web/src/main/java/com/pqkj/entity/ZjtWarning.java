package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtWarning extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 预警内容
     */
    private String warningContent;

    /**
     * 预警时间
     */
    private String warningTime;

    /**
     * 预警描述
     */
    private String warningDesc;

    /**
     * 预警经度
     */
    private String warningLongitude;

    /**
     * 预警纬度
     */
    private String warningLatitude;

    /**
     * 预警名称
     */
    private String warningName;

    /**
     * 生效时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 预警类型 1.定位预警，2.离线预警
     */
    private Integer warningType;
    /**
     * 预警范围
     */
    private Integer warningRange;

}
