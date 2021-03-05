package com.pqkj.common;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pq.framework.common.FilePropertiesBase;
import com.pq.framework.util.StringUtil;
import com.pqkj.entity.SysDict;
import com.pqkj.quartz.SpringContextUtils;
import com.pqkj.service.ISysDictService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Component
public class ExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * excel导入
     *
     * @param url  模板txt相对路径
     * @param url1 excel相对路径
     * @return
     */
    public static JSONObject importExcel(String url, String url1) {
        JSONObject result = new JSONObject();
        url = FilePropertiesBase.getRootPath() + url;
        url1 = FilePropertiesBase.getRootPath() + url1;
        String content = txtToString(new File(url));
        JSONObject obj = JSONObject.parseObject(content.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", ""));
        //导入参数对象
        JSONObject list = obj.getJSONObject("import");
        try {
            //读取excel文件
            String fileType = url1.substring(url1.lastIndexOf(".") + 1);
            InputStream is = new FileInputStream(url1);
            //获取工作薄
            Workbook wb = null;
            if ("xls".equals(fileType)) {
                wb = new HSSFWorkbook(is);
            } else if ("xlsx".equals(fileType)) {
                wb = new XSSFWorkbook(is);
            }
            //读取第一个工作页sheet
            Sheet sheet = wb.getSheetAt(0);
            //获取excel标题并储存起来
            List<String> title = new ArrayList<String>();
            for (Cell cell : sheet.getRow(0)) {
                //根据不同类型转化成字符串
                cell.setCellType(Cell.CELL_TYPE_STRING);
                title.add(cell.getStringCellValue());
            }
            //找出并保存不存在的列
            String nulTitle = "";
            for (String key : list.keySet()) {
                if (!title.contains(list.getJSONObject(key).getString("mc"))) {
                    nulTitle += list.getJSONObject(key).getString("mc");
                } else {
                    list.getJSONObject(key).put("index", title.indexOf(list.getJSONObject(key).getString("mc")));
                }
            }
            //判断是否存在不存在的列 如果存在直接跳出
            List<JSONObject> successList = new ArrayList<JSONObject>();
            List<JSONObject> errorList = new ArrayList<JSONObject>();
            if (StringUtil.isNullorEmpty(nulTitle)) {
                sheet.removeRow(sheet.getRow(0));
                //循环出数据并储存起来
                for (Row row : sheet) {
                    JSONObject data = new JSONObject();
                    //数据正确与否结构
                    Boolean reuslt = true;
                    //错误描述
                    String error = "";
                    for (String key : list.keySet()) {
                        JSONObject sj = list.getJSONObject(key);
                        //判断字段是否必填
                        if (sj.getInteger("bx") == 1) {
                            if (StringUtil.isNullorEmpty(row.getCell(sj.getInteger("index")))) {
                                error += sj.getString("mc") + "为空,";
                                data.put(key, "");
                                reuslt = false;
                            } else {
                                row.getCell(sj.getInteger("index")).setCellType(Cell.CELL_TYPE_STRING);
                                //判断字段的类型
                                JSONObject zhzobj = sj.getJSONObject("zhz");
                                if (zhzobj != null) {
                                    for (String key1 : zhzobj.keySet()) {
                                        if (row.getCell(sj.getInteger("index")).getStringCellValue().equals(key1)) {
                                            data.put(key, zhzobj.getString(key1));
                                        }
                                    }
                                } else {
                                    data.put(key, row.getCell(sj.getInteger("index")).getStringCellValue());
                                }
                            }
                        } else {
                            if (row.getCell(sj.getInteger("index")) != null) {
                                row.getCell(sj.getInteger("index")).setCellType(Cell.CELL_TYPE_STRING);
                                //判断字段的类型
                                JSONObject zhzobj = sj.getJSONObject("zhz");
                                if (zhzobj != null) {
                                    for (String key1 : zhzobj.keySet()) {
                                        if (row.getCell(sj.getInteger("index")).getStringCellValue().equals(key1)) {
                                            data.put(key, zhzobj.getString(key1));
                                        }
                                    }
                                } else {
                                    data.put(key, row.getCell(sj.getInteger("index")).getStringCellValue());
                                }
                            } else {
                                data.put(key, "");
                            }
                        }
                    }
                    if (reuslt) {
                        successList.add(data);
                    } else {
                        data.put("error", error);
                        errorList.add(data);
                    }
                }
                result.put("code", 1);
                result.put("msg", "导入成功");

            } else {
                result.put("code", 0);
                result.put("msg", "缺少" + nulTitle + "列");
            }
            result.put("successList", successList);
            result.put("errorList", errorList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static String txtToString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            InputStreamReader fReader = new InputStreamReader(new FileInputStream(file),"UTF-8");
            BufferedReader br = new BufferedReader(fReader);//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static JSONObject getDataByText(String url, String key) {
        url = FilePropertiesBase.getRootPath() + url;
        String content = txtToString(new File(url));
        JSONObject obj = JSONObject.parseObject(content);
        //导出参数对象
        return obj.getJSONObject(key);
    }

    /**
     * 将数据导出至Excel
     *
     * @param jsonObject
     * @return
     */
    public JSONObject exportExcel(JSONObject jsonObject) {
        JSONObject jsonReturn = new JSONObject();

        return jsonReturn;
    }


    public static JSONObject exportExcel(String url, String url1, String fileName, List<JSONObject> jsonObjects) {
        JSONObject result = new JSONObject();
        File dcFile = new File(FilePropertiesBase.getRootPath() + url1);
        if (!dcFile.exists()) {
            try {
                dcFile.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        url = FilePropertiesBase.getRootPath() + url;
        url1 = FilePropertiesBase.getRootPath() + url1+fileName;
        //将模板配置文件转成jsonobject
        JSONObject obj = JSONObject.parseObject(txtToString(new File(url)).replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", ""));
        //导入参数对象
        JSONObject list = obj.getJSONObject("export");
        //组装标题
        int length = 0;
        for (String key : list.keySet()) {
            length++;
        }
        String[] title = new String[length];
        for (String key : list.keySet()) {
            JSONObject val = list.getJSONObject(key);
            title[val.getInteger("order")] = val.getString("mc");
        }
        //组装后的数据
        String[][] dataList = new String[jsonObjects.size()][title.length];
        for (int i = 0, j = jsonObjects.size(); i < j; i++) {
            for (String key : list.keySet()) {
                JSONObject val = list.getJSONObject(key);
                JSONObject zhz = val.getJSONObject("zhz");
                if (zhz != null) {
                    dataList[i][val.getInteger("order")] = zhz.getString(jsonObjects.get(i).getString(key));
                } else {
                    dataList[i][val.getInteger("order")] = jsonObjects.get(i).getString(key);
                }
            }
        }
        HSSFWorkbook wb = getHSSFWorkbook(FileUtil.getFileNameSuff(url1), title, dataList, null);
        File file = new File(url1);
        try {
            FileOutputStream output = new FileOutputStream(file);
            wb.write(output);
            output.flush();
            output.close();
            result.put("code", 1);
            result.put("msg", "导出成功");
            result.put("path", url1);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 0);
            result.put("msg", "导出失败");
        }
        return result;
    }

    /**
     * 导出模板
     *
     * @param url  模板text文件路径
     * @param url1 生成后的excel相对路径
     * @return JSONObject
     */
    public static JSONObject exportModelExcel(String url, String url1, String fileName) {
        JSONObject result = new JSONObject();
        File dcFile = new File(FilePropertiesBase.getRootPath() + url1);
        if (!dcFile.exists()) {
            try {
                dcFile.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        url = FilePropertiesBase.getRootPath() + url;
        url1 = FilePropertiesBase.getRootPath() + url1 + fileName;
        //将模板配置文件转成jsonobject
        JSONObject obj = JSONObject.parseObject(txtToString(new File(url)).replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", ""));
        //导入参数对象
        JSONObject list = obj.getJSONObject("import");
        //组装标题
        int length = 0;
        for (String key : list.keySet()) {
            length++;
        }
        String[] title = new String[length];
        JSONObject isNess=new JSONObject();

        for (String key : list.keySet()) {
            JSONObject val = list.getJSONObject(key);
            title[val.getInteger("order")] = val.getString("mc");
            String bz="";
            if(val.getInteger("bx")==1){
                bz="必填";
                isNess.put(val.getString("mc"),bz);
            }
            if(!StringUtil.isNullorEmpty(val.get("dict"))){
                isNess.put(val.getString("mc"),bz+"--------"+dictList(val.getString("dict")));
            }
        }
        File file = new File(url1);

        HSSFWorkbook wb = getHSSFWorkbook(FileUtil.getFileNameSuff(url1), title, null,isNess);
        try {
            FileOutputStream output = new FileOutputStream(file);
            wb.write(output);
            output.flush();
            output.close();
            result.put("code", 1);
            result.put("msg", "导出成功");
            result.put("path", url1);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 0);
            result.put("msg", "导出失败");
        }
        return result;
    }
    public static String  dictList(String type){
        ISysDictService sysDictImpl = (ISysDictService) SpringContextUtils.getBean(ISysDictService.class);
        HashMap condition=new HashMap();
        QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("sd_level", 2);
        String dictList="";
        try {
            List<SysDict> sysdicts=sysDictImpl.list(queryWrapper);
            for(SysDict sysdict:sysdicts){
                dictList+=sysdict.getSdName()+",";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictList;
    }

    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, HSSFWorkbook wb, JSONObject isBx) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        // 创建绘图对象
        HSSFPatriarch p = sheet.createDrawingPatriarch();
        //声明列对象
        HSSFCell cell = null;
        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
            if(!StringUtil.isNullorEmpty(isBx.get(title[i]))){
                HSSFComment comment = p.createCellComment(new HSSFClientAnchor(0, 0, 0,0, (short) 3, 3, (short) 5, 6));
                // 输入批注信息
                comment.setString(new HSSFRichTextString(isBx.getString(title[i])));
                // 将批注添加到单元格对象中
                cell.setCellComment(comment);
            }
        }
        return wb;
    }

    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        //声明列对象
        HSSFCell cell = null;
        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }


    /**
     * @param url         模板text文件路径
     * @param url1        生成后的excel相对路径
     * @param jsonObjects 数据
     * @return
     */
    public static JSONObject exportExcel(String url, String url1, List<JSONObject> jsonObjects) {
        JSONObject result = new JSONObject();
        url = FilePropertiesBase.getRootPath() + url;
        url1 = FilePropertiesBase.getRootPath() + url1;
        //将模板配置文件转成jsonobject
        JSONObject obj = JSONObject.parseObject(txtToString(new File(url)));
        //导入参数对象
        JSONObject list = obj.getJSONObject("export");
        //组装标题
        int length = 0;
        for (String key : list.keySet()) {
            length++;
        }
        String[] title = new String[length];
        for (String key : list.keySet()) {
            JSONObject val = list.getJSONObject(key);
            title[val.getInteger("order")] = val.getString("mc");
        }
        //组装后的数据
        String[][] dataList = new String[jsonObjects.size()][title.length];
        for (int i = 0, j = jsonObjects.size(); i < j; i++) {
            for (String key : list.keySet()) {
                JSONObject val = list.getJSONObject(key);
                JSONObject zhz = val.getJSONObject("zhz");
                if (zhz != null) {
                    dataList[i][val.getInteger("order")] = zhz.getString(jsonObjects.get(i).getString(key));
                } else {
                    dataList[i][val.getInteger("order")] = jsonObjects.get(i).getString(key);
                }
            }
        }
        HSSFWorkbook wb = getHSSFWorkbook(FileUtil.getFileNameSuff(url1), title, dataList, null);
        File file = new File(url1);
        try {
            FileOutputStream output = new FileOutputStream(file);
            wb.write(output);
            output.flush();
            output.close();
            result.put("code", 1);
            result.put("msg", "导出成功");
            result.put("path", url1);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("code", 0);
            result.put("msg", "导出失败");
        }
        return result;
    }
}
