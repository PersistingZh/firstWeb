package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务结果
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtTaskResult extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否已完成（0、未完成，1、已完成）
     */
    private Integer isFinish;

    /**
     * 完成时间
     */
    private String finishTime;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 打卡经度
     */
    private String clockLongitude;

    /**
     * 打卡纬度
     */
    private String clockLatitude;

    /**
     * 打卡是否有效(0.有效，1.无效)
     */
    private Integer clockEffective;

    /**
     * 打卡地点
     */
    private String clockAddress;
}
