package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SysParameterAddVO {

    @ApiModelProperty(value = "参数名")
    @NotBlank(message = "参数名不能为空")
    private String parameterName;
    @ApiModelProperty(value = "参数值")
    @NotNull(message = "参数值不能为空")
    private Integer parameterVal;
}
