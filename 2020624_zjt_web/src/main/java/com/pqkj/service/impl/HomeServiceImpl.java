package com.pqkj.service.impl;

import com.pqkj.entity.SysUser;
import com.pqkj.service.HomeService;
import com.pqkj.service.ISysPropertyService;
import com.pqkj.service.PermissionService;
import com.pqkj.service.UserService;
import com.pqkj.vo.resp.HomeRespVO;
import com.pqkj.vo.resp.PermissionRespNode;
import com.pqkj.vo.resp.UserInfoRespVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ISysPropertyService sysPropertyService;
    @Override
    public HomeRespVO getHomeInfo(String userId) {


        SysUser sysUser = userService.detailInfo(userId);
        UserInfoRespVO vo = new UserInfoRespVO();

        if (sysUser != null) {
            BeanUtils.copyProperties(sysUser, vo);
        }

        List<PermissionRespNode> menus = permissionService.permissionTreeList(userId);

        HomeRespVO respVO = new HomeRespVO();
        respVO.setMenus(menus);
        respVO.setUserInfo(vo);
        respVO.setSysProperty(sysPropertyService.getById("21333331"));
        return respVO;
    }
}
