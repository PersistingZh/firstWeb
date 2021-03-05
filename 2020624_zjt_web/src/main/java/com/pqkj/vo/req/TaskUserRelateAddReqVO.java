package com.pqkj.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @ClassName: UserAddReqVO
 */
@Data
public class TaskUserRelateAddReqVO {
    @ApiModelProperty(value = "任务ID")
    @NotBlank(message = "任务ID不能为空")
    private List<String> taskIds;
    @ApiModelProperty(value = "用户ID")
    @NotBlank(message = "用户ID不能为空")
    private List<String> userIds;
}
