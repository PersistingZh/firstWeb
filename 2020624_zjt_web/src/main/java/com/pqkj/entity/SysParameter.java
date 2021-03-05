package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统公用参数
 * </p>
 *
 * @author zbc
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysParameter extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 参数名称
     */
    private String parameterName;

    /**
     * 参数值
     */
    private Integer parameterVal;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;


}
