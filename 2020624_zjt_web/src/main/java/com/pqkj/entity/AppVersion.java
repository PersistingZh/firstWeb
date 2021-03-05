package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zbc
 * @since 2020-07-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AppVersion extends PageReqVO {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * APP名称
     */
    private String appName;

    /**
     * 版本
     */
    private Integer versionCode;

    /**
     * 版本名
     */
    private String versionName;

    /**
     * 包名
     */
    private String packages;

    /**
     * app类型
     */
    private String appType;

    /**
     * 版本路径
     */
    private String versionPath;

    /**
     * 下载次数
     */
    private Integer downCount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * APP名称
     */
    private String apkName;

    private Integer isOnline;


}
