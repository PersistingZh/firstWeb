package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReceiveGPSVO {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "经度")
    private double longitude;
    @ApiModelProperty(value = "纬度")
    private double latitude;
    @ApiModelProperty(value = "地址")
    private String address;
}
