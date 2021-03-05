package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 预警与用户关系表
 * </p>
 *
 * @author zbc
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtWarningUserRelate extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 预警任务id
     */
    private String warningId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;


}
