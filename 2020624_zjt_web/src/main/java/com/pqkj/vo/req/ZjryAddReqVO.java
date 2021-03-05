package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
public class ZjryAddReqVO {
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @ApiModelProperty(value = "证件号码")
    private String cardNumber;
    @ApiModelProperty(value = "证件类型")
    private String cardType;
    @ApiModelProperty(value = "照片路径")
    private String photoUrl;
    @ApiModelProperty(value = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String phone;
    @ApiModelProperty(value = "密码（预留字段）")
    private String password;
    @ApiModelProperty(value = "地址")
    private String address;
}
