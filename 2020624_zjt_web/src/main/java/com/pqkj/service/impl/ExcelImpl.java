package com.pqkj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pq.framework.common.FilePropertiesBase;
import com.pqkj.common.ExcelUtil;
import com.pqkj.common.FileUtil;
import com.pqkj.entity.SysFileFjxx;
import com.pqkj.service.IExcel;
import com.pqkj.service.ISysFileFjxxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service("excelImpl")
public class ExcelImpl implements IExcel {
    private static final Logger logger = LoggerFactory
            .getLogger(ExcelImpl.class);
    @Resource
    private ISysFileFjxxService sysFileFjxxService;

    /**
     * 导出模板
     *
     * @param type 模板类型名称
     * @return
     * @throws Exception
     */
    @Override
    public Object[] outportExcelModule(String type) throws Exception {
        Object[] objects = new Object[3];
        objects[0] = false;
        objects[1] = "导出失败";
        String filePath = FilePropertiesBase.getValue("file.sysFile");
        String fileName = type + "导入导出模板.xls";
        File dir = new File(FilePropertiesBase.getRootPath() + filePath);
        FileUtil.judeDirExists(dir);
        QueryWrapper<SysFileFjxx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ms", type + "导入导出模板");
        List<SysFileFjxx> allByProperty = sysFileFjxxService.list(queryWrapper);
        JSONObject returnJson = ExcelUtil.exportModelExcel(allByProperty.get(0).getFilePath(), filePath, fileName);
        if (returnJson.getString("code").equals("1")) {
            objects[0] = true;
            objects[1] = fileName;
            objects[2] = filePath + fileName;
        }
        return objects;
    }

    @Override
    @Transactional
    public JSONObject getExcelData(String filePath, String type) throws Exception {
        JSONObject returnJSON = new JSONObject();
        returnJSON.put("code", 0);
        returnJSON.put("msg", "导入失败");
        QueryWrapper<SysFileFjxx> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ms", type + "导入导出模板");
        List<SysFileFjxx> sysFileFjxxes = sysFileFjxxService.list(queryWrapper);
        if (sysFileFjxxes.size() == 0) {
            returnJSON.put("msg", "文本模板不存在");
            return returnJSON;
        } else {
            JSONObject resultobj = ExcelUtil.importExcel(sysFileFjxxes.get(0).getFilePath(), filePath);
            if (resultobj.getInteger("code") == 1) {

                returnJSON = resultobj;
            }
        }
        return returnJSON;
    }
}
