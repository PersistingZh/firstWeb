package com.pqkj.vo.req;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: DeptPageReqVO
 */
@Data
public class PageReqVO {
    @ApiModelProperty(value = "第几页")
    @TableField(exist = false)
    private int page=1;

    @ApiModelProperty(value = "分页数量")
    @TableField(exist = false)
    private int limit=10;

    @ApiModelProperty(value = "手机唯一码")
    @TableField(exist = false)
    private String macCode;
}
