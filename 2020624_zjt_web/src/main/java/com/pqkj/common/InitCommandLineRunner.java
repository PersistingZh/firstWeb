package com.pqkj.common;

import com.alibaba.fastjson.JSONObject;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.StringUtil;
import com.pqkj.utils.HttpUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class InitCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("初始化成功。。。"+ DateTimeUI.getCurrentDateTime());
    }
}
