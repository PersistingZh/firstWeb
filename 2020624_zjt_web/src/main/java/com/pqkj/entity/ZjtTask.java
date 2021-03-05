package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务表
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtTask extends PageReqVO {

    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    private String id;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务编号
     */
    private String taskNum;

    /**
     * 任务描述
     */
    private String taskDesc;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 任务创建时间
     */
    private String createTime;

    /**
     * 任务修改时间
     */
    private String updateTime;

    /**
     * 周期（1.每日，2每月）
     */
    private Integer cycle;

    /**
     * 生效时间
     */
    private String effectiveTime;

    /**
     * 失效时间
     */
    private String failureTime;

    /**
     * 任务地址
     */
    private String taskAddress;
    /**
     * 任务经度
     */
    private String taskLongitude;
    /**
     * 任务纬度
     */
    private String taskLatitude;

    /**
     * 任务范围，单位米
     */
    private Integer taskRange;
}
