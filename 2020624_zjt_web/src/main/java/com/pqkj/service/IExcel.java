package com.pqkj.service;

import com.alibaba.fastjson.JSONObject;
import com.pqkj.common.utils.DataResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

public interface IExcel {

    Object[] outportExcelModule(String type) throws Exception;

    @Transactional
    JSONObject getExcelData(String filePath, String type) throws Exception;

}
