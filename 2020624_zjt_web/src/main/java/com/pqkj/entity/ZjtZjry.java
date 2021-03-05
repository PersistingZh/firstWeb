package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 矫正人员
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtZjry extends PageReqVO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 证件号码
     */
    private String cardType;

    /**
     * 照片路径
     */
    private String photoUrl;

    /**
     * 证件号码
     */
    private String cardNumber;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 密码（预留）
     */
    private String password;

    /**
     * 联系地址（预留）
     */
    private String address;

    /**
     * 最后一次位置经度
     */
    private String lastLongitude;

    /**
     * 最后一次位置纬度
     */
    private String lastLatitude;

    /**
     * 最后一次记录时间
     */
    private String lastRecordTime;

    /**
     * 是否在线（0:离线,1:在线）
     */
    private Integer isOnline;

    /**
     * 最后一次在线时间
     */
    private String lastOnlineTime;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 是否验证(0.未验证,1.已验证)
     */
    private String isVerification;

    /**
     * 获取验证码时间
     */
    private String verificationCodeTime;

    /**
     * 验证时间
     */
    private String verificationTime;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 是否删除（0.正常，1.删除）
     */
    private Integer isDel;

    /**
     * 管理单位
     */
    private String gldw;
    /**
     * 批次
     */
    private String batch;
}
