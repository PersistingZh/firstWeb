package com.pqkj.vo.resp;

import com.pqkj.entity.SysProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class HomeRespVO {
    @ApiModelProperty(value = "用户信息")
    private UserInfoRespVO userInfo;
    @ApiModelProperty(value = "目录菜单")
    private List<PermissionRespNode> menus;
    @ApiModelProperty(value = "图片访问路径")
    private SysProperty sysProperty;
}