package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskResultView extends PageReqVO {

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
    private String isFinish;

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
    private String clockEffective;

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
     * 生效时间
     */
    private String effectiveTime;

    /**
     * 失效时间
     */
    private String failureTime;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 联系地址（预留）
     */
    private String address;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 证件号码
     */
    private String cardNumber;

    /**
     * 证件号码
     */
    private String cardType;

    private String clockAddress;

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
     * 管理单位
     */
    private String gldw;
    /**
     * 打卡范围
     */
    private Integer taskRange;
}
