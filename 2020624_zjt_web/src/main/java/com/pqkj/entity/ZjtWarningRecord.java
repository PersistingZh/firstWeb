package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zbc
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtWarningRecord extends PageReqVO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 预警id
     */
    private String warningId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 触发预警经度
     */
    private String createLongitude;

    /**
     * 触发预警纬度
     */
    private String createLatitude;


}
