package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 字典管理
 * </p>
 *
 * @author zbc
 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysDict extends PageReqVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableField("ID")
    private String id;

    /**
     * 代码
     */
    @TableField("SD_CODE")
    private String sdCode;

    /**
     * 名称
     */
    @TableField("SD_NAME")
    private String sdName;

    /**
     * 父ID
     */
    @TableField("SD_PID")
    private String sdPid;

    /**
     * 层级
     */
    @TableField("SD_LEVEL")
    private Integer sdLevel;

    /**
     * 排序
     */
    @TableField("ORDERR")
    private Integer orderr;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 时间戳
     */
    @TableField("SYNC_TIME")
    private String syncTime;

    /**
     * 路径
     */
    @TableField("PATH")
    private String path;

    /**
     * 路径名称
     */
    @TableField("PATH_NAME")
    private String pathName;

    /**
     * 是否末节点
     */
    @TableField("IS_END")
    private Integer isEnd;

    /**
     * 是否系统字典
     */
    @TableField("IS_SYS_DIC")
    private Integer isSysDic;

    /**
     * 值
     */
    @TableField("SD_VALUE")
    private String sdValue;

    /**
     * 是否删除
     */
    @TableField("IS_DEL")
    private Integer isDel;

    /**
     * 类型
     */
    @TableField("TYPE")
    private String type;

    /**
     * 备注
     */
    @TableField("BZ")
    private String bz;


}
