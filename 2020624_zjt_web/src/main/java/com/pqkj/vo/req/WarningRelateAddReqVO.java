package com.pqkj.vo.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class WarningRelateAddReqVO {
    @ApiModelProperty(value = "预警ID")
    @NotBlank(message = "预警ID不能为空")
    private List<String> warningIds;
    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private List<String> userIds;
}
