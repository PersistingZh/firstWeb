package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件附加信息表
 * </p>
 *
 * @author zbc
 * @since 2020-07-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysFileFjxx extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 关联id
     */
    private String glId;

    /**
     * 描述
     */
    private String ms;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 更新时间
     */
    private String updateDate;

    /**
     * 是否为首页展示图片0不是首页展示1是首页展示
     */
    private Integer isSy;

    private Integer type;

    /**
     * 上传文件名
     */
    private String fileName;


}
