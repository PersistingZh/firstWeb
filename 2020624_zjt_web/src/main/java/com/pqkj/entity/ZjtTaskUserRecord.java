package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户与任务记录表
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtTaskUserRecord extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 创建时间（做任务时间）
     */
    private String createTime;

    /**
     * 任务备注（打卡地点）
     */
    private String taskRemarks;

    /**
     * 任务状态（0、正常，1、异常）
     */
    private Integer taskState;


}
