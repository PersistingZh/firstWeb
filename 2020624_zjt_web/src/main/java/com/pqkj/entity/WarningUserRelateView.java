package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author zbc
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class WarningUserRelateView extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 预警任务id
     */
    private String warningId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 预警内容
     */
    private String warningContent;

    /**
     * 预警备注
     */
    private String warningDesc;

    /**
     * 预警纬度
     */
    private String warningLatitude;

    /**
     * 预警经度
     */
    private String warningLongitude;

    /**
     * 预警名称
     */
    private String warningName;

    /**
     * 预警时间
     */
    private String warningTime;

    /**
     * 预警类型，1.定位预警，2.离线预警
     */
    private Integer warningType;

    /**
     * 姓名
     */
    private String userName;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 联系地址（预留）
     */
    private String address;

    /**
     * 是否删除
     */
    private Integer isDel;

    /**
     * 证件号码
     */
    private String cardNumber;

    /**
     * 证件号码
     */
    private String cardType;

    /**
     * 管理单位
     */
    private String gldw;
    /**
     * 预警范围
     */
    private Integer warningRange;

}
