package com.pqkj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysUser implements Serializable {
    @TableId(type = IdType.UUID)
    private String id;

    private String username;

    private String salt;

    private String password;

    private String phone;

    private String deptId;

    @TableField(exist = false)
    private String deptName;

    private String realName;

    private String nickName;

    private String email;

    private Integer status;

    private Integer sex;

    @TableLogic
    private Integer deleted;

    private String createId;

    private String updateId;

    private Integer createWhere;

    private Date createTime;

    private Date updateTime;

    private String xqId;
    //明文密码
    private String clearPwd;
    /**
     * 用户类型 （1、后台用户(密码登录)，2、前端用户（验证码登录））
     */
    private Integer userType;
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
     * 所在单位
     */
    private String units;
}