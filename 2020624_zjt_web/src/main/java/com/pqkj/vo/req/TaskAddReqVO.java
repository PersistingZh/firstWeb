package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: UserAddReqVO
 */
@Data
public class TaskAddReqVO {
    @ApiModelProperty(value = "任务名称")
    @NotBlank(message = "任务名称不能为空")
    private String taskName;
    @ApiModelProperty(value = "任务描述")
    private String taskDesc;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "周期（1.每日，2.每月）")
    private Integer cycle;
    @ApiModelProperty(value = "生效时间")
    private String effectiveTime;
    @ApiModelProperty(value = "失效时间")
    private String failureTime;
    @ApiModelProperty(value = "任务地址")
    private String taskAddress;
    @ApiModelProperty(value = "任务经度")
    private String taskLongitude;
    @ApiModelProperty(value = "任务纬度")
    private String taskLatitude;
    @ApiModelProperty(value = "任务范围")
    private Integer taskRange;
}
