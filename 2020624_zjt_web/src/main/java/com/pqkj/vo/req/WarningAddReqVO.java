package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName: UserAddReqVO
 */
@Data
public class WarningAddReqVO {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "预警内容")
    @NotBlank(message = "预警内容不能为空")
    private String warningContent;
    @ApiModelProperty(value = "预警描述")
    private String warningDesc;
    @ApiModelProperty(value = "预警类型，1.定位预警，2.离线预警")
    private Integer warningType;
    @ApiModelProperty(value = "生效时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "预警名称")
    private String warningName;
    @ApiModelProperty(value = "预警经度")
    private String warningLongitude;
    @ApiModelProperty(value = "预警纬度")
    private String warningLatitude;
    @ApiModelProperty(value = "预警范围")
    private Integer warningRange;
}
