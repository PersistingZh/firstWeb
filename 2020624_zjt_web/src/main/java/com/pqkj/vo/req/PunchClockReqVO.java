package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class PunchClockReqVO {
    @ApiModelProperty(value = "id")
    @NotBlank(message = "id不能为空")
    private String id;
    @ApiModelProperty(value = "打卡经度")
    @NotBlank(message = "打卡经度不能为空")
    private String clockLongitude;
    @ApiModelProperty(value = "打卡纬度")
    @NotBlank(message = "打卡纬度不能为空")
    private String clockLatitude;
    @ApiModelProperty(value = "打卡是否有效(0.有效，1.无效)")
    private Integer clockEffective;
    @ApiModelProperty(value = "打卡地址")
    @NotBlank(message = "打卡地址不能为空")
    private String clockAddress;
    @ApiModelProperty(value = "打卡备注")
    private String taskRemarks;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "Mac")
    private String macCode;
}
