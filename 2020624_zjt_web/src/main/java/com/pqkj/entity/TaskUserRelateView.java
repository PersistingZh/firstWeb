package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TaskUserRelateView extends PageReqVO {

    private static final long serialVersionUID = 1L;

    /**
     * 关系id
     */
    private String id;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 用户id
     */
    private String userId;

    private String createTime;

    private String updateTime;

    /**
     * app用户名称
     */
    private String userName;

    /**
     * app用户电话号码
     */
    private String phone;

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
     * 是否生成
     */

    private Integer isGenerate;
    /**
     * 管理单位
     */
    private String gldw;
    /**
     * 是否删除
     */
    private String isDel;
    /**
     * 打卡范围
     */
    private Integer taskRange;
}
