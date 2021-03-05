package com.pqkj.service;

import com.pqkj.vo.resp.HomeRespVO;

public interface HomeService {

    HomeRespVO getHomeInfo(String userId);
}
