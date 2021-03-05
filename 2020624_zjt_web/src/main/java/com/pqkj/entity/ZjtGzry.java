package com.pqkj.entity;

import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工作人员
 * </p>
 *
 * @author zbc
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ZjtGzry extends PageReqVO {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件类型
     */
    private String cardType;

    /**
     * 证件号码
     */
    private String cardNumber;

    /**
     * 所属单位
     */
    private String units;

    /**
     * 部门
     */
    private String department;

    /**
     * 职位
     */
    private String position;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 密码（预留字段）
     */
    private String password;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 是否验证(0.未验证,1.已验证)
     */
    private Integer isVerification;

    /**
     * 获取验证码时间
     */
    private String verificationCodeTime;

    /**
     * 验证时间
     */
    private String verificationTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 是否删除（0.正常，1删除）
     */
    private Integer isDel;


}
