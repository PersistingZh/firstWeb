package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class GzryAddReqVO {
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @ApiModelProperty(value = "证件号码")
    @NotBlank(message = "证件号码不能为空")
    private String cardNumber;
    @ApiModelProperty(value = "证件类型")
    @NotBlank(message = "证件类型不能为空")
    private String cardType;
    @ApiModelProperty(value = "单位")
    private String units;
    @ApiModelProperty(value = "部门")
    private String department;
    @ApiModelProperty(value = "职位")
    private String position;
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String phone;
    @ApiModelProperty(value = "密码（预留字段）")
    private String password;
}
