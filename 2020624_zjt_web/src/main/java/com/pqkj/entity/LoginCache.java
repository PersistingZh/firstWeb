package com.pqkj.entity;

import com.pqkj.vo.req.CheckMacVO;
import com.pqkj.vo.req.PageReqVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 登录缓存表
 * </p>
 *
 * @author zbc
 * @since 2020-07-04
 */
@Data
@Accessors(chain = true)
public class LoginCache {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * MAC码手机唯一标识
     */
    private String macCode;

    /**
     * 登录时间
     */
    private String loginTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否在线(0.离线，1.在线)
     */
    private Integer online;


}
