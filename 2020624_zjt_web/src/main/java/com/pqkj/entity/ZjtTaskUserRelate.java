package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 任务与用户关系表
 * </p>
 *
 * @author zbc
 * @since 2020-06-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtTaskUserRelate extends PageReqVO {

    private static final long serialVersionUID = 1L;

    /**
     * 关系id
     */
    private String id;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 用户id
     */
    private String userId;

    private String createTime;

    private String updateTime;
    /**
     * 是否生成
     */
    private Integer isGenerate;
}
