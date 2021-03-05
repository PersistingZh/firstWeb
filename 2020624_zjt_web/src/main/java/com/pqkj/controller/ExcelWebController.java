//package com.pqkj.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.pq.framework.util.ObjectUtil;
//import com.pqkj.common.utils.DataResult;
//import com.pqkj.service.IExcel;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import java.util.HashMap;
//import java.util.List;
//
///**
// * @author zbc
// * @since 2020年3月27日
// */
//@Api(tags = {"导入导出接口"})
//@Slf4j
//@Controller
//@RequestMapping("/excelWeb")
//public class ExcelWebController {
//    private static final Logger logger = LoggerFactory.getLogger(ExcelWebController.class);
//
//    @Resource
//    IExcel excelImpl;
//    @ApiOperation(value = "导入房屋数据")
//    @PostMapping(value = "/importHouses")
//    @ResponseBody
//    public DataResult drData(@RequestBody JSONObject jsonObject) {
//        try {
//            //获取excel数据
//            JSONObject data=excelImpl.getExcelData(jsonObject.getString("excelPath"),jsonObject.getString("type"));
//           logger.info(data.toJSONString());
//            if(data.getJSONArray("errorList").size()>0){
//                List<HashMap> list = JSONObject.parseArray(data.getJSONArray("errorList").toJSONString(), HashMap.class);
//                return DataResult.fail(list.get(0).get("error")+"");
//            }
//            if(data.getInteger("code")==1){
//                JSONObject resultJson = excelImpl.importHousesData(data);
//                int code = resultJson.getIntValue("code");
//                if(code == 1){
//                    return DataResult.success(resultJson.getString("msg"));
//                }
//            }
//            return DataResult.fail(data.getString("msg"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DataResult.fail("系统异常");
//        }
//    }
//    @ApiOperation(value = "导入房屋房管数据")
//    @PostMapping(value = "/importBatchHousesExcel")
//    @ResponseBody
//    public DataResult importBatchHousesExcel(@RequestBody JSONObject jsonObject) {
//        DataResult dataResult = DataResult.fail("导入失败");
//        try {
//            //获取excel数据
//            JSONObject data = excelImpl.getExcelData(jsonObject.getString("excelPath"),jsonObject.getString("type"));
//            logger.info(data.toJSONString());
//            if(data.getJSONArray("errorList").size()>0){
//                List<HashMap> list = JSONObject.parseArray(data.getJSONArray("errorList").toJSONString(), HashMap.class);
//                return DataResult.fail(list.get(0).get("error")+"");
//            }
//            if(data.getInteger("code")==1){
//                JSONObject returnJSON=excelImpl.importBatchHousesExcel(data);
//                if(returnJSON.getIntValue("code") == 1){
//                    dataResult = DataResult.success(returnJSON.getString("msg"));
//                }else{
//                    dataResult = DataResult.fail(returnJSON.getString("msg"));
//                }
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            dataResult = DataResult.fail("系统异常");
//        }
//        return dataResult;
//    }
//    @ApiOperation(value = "导出房屋房管数据")
//    @PostMapping(value = "/exportBatchHouses")
//    @ResponseBody
//    public DataResult exportBatchHouses(@RequestBody JSONObject jsonObject) {
//        try {
//            return excelImpl.exportBatchHouses(jsonObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DataResult.fail("导出失败");
//        }
//    }
//    @ApiOperation(value = "导出房屋用户数据")
//    @PostMapping(value = "/exportFwUser")
//    @ResponseBody
//    public DataResult exportFwUser(@RequestBody JSONObject jsonObject) {
//        try {
//            return excelImpl.exportFwUser(ObjectUtil.jsonToMap(jsonObject));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DataResult.fail("导出失败");
//        }
//    }
//
//    @ApiOperation(value = "导入房管数据")
//    @PostMapping(value = "/importFgUser")
//    @ResponseBody
//    public DataResult importFgUser(@RequestBody JSONObject jsonObject) {
//        try {
//            //获取excel数据
//            JSONObject data=excelImpl.getExcelData(jsonObject.getString("excelPath"),jsonObject.getString("type"));
//            logger.info(data.toJSONString());
//            if(data.getJSONArray("errorList").size()>0){
//                List<HashMap> list = JSONObject.parseArray(data.getJSONArray("errorList").toJSONString(), HashMap.class);
//                return DataResult.fail(list.get(0).get("error")+"");
//            }
//            if(data.getInteger("code")==1){
//                JSONObject resultJson = excelImpl.importFgUser(data);
//                int code = resultJson.getIntValue("code");
//                if(code == 1){
//                   return DataResult.success(resultJson.getString("msg"));
//                }
//            }
//            return DataResult.fail(data.getString("msg"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DataResult.fail("系统异常");
//        }
//    }
//    @ApiOperation(value = "导入房管数据")
//    @PostMapping(value = "/importFwUser")
//    @ResponseBody
//    public DataResult importFwUser(@RequestBody JSONObject jsonObject) {
//        DataResult dataResult = DataResult.fail("导入失败");
//        try {
//            //获取excel数据
//            JSONObject data=excelImpl.getExcelData(jsonObject.getString("excelPath"),jsonObject.getString("type"));
//            logger.info(data.toJSONString());
//            if(data.getJSONArray("errorList").size()>0){
//                List<HashMap> list = JSONObject.parseArray(data.getJSONArray("errorList").toJSONString(), HashMap.class);
//                return DataResult.fail(list.get(0).get("error")+"");
//            }
//            if(data.getInteger("code")==1){
//                JSONObject resultJson = excelImpl.importFwUser(data);
//                int code = resultJson.getIntValue("code");
//                if(code == 1){
//                    return DataResult.success(resultJson.getString("msg"));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            dataResult = DataResult.fail("系统异常");
//        }
//        return dataResult;
//    }
//    @ApiOperation(value = "导出房屋数据")
//    @PostMapping("/exportHouses")
//    @ResponseBody
//    public DataResult exportHouses(@RequestBody JSONObject jsonObject) {
//        try {
//            return excelImpl.exportHouses(ObjectUtil.jsonToMap(jsonObject));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DataResult.fail("导出失败");
//        }
//    }
//    @ApiOperation(value = "导出租户数据")
//    @PostMapping("/exportZhUser")
//    @ResponseBody
//    public DataResult exportZhUser(@RequestBody JSONObject jsonObject) {
//        try {
//            return excelImpl.exportZhUser(ObjectUtil.jsonToMap(jsonObject));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DataResult.fail("导出失败");
//        }
//    }
//    @ApiOperation(value = "导出模板")
//    @PostMapping("/outportExcelModule")
//    @ResponseBody
//    public DataResult outportExcelModule(@RequestParam(value = "type") String type) {
//        try {
//            Object[] resultObject = excelImpl.outportExcelModule(type);
//            return DataResult.success(resultObject);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return DataResult.fail("系统异常");
//        }
//    }
//    @ApiOperation(value = "导入App超链接")
//    @ResponseBody
//    @PostMapping(value = "/importAppHyperlinkExcel")
//    public DataResult importAppHyperlinkExcel(@RequestBody JSONObject jsonObject) {
//        DataResult dataResult = DataResult.fail("导入失败");
//        try {
//            //获取excel数据
//            JSONObject data = excelImpl.getExcelData(jsonObject.getString("excelPath"),jsonObject.getString("type"));
//            logger.info(data.toJSONString());
//            if(data.getInteger("code")==1){
//                return excelImpl.importAppHyperlinkExcel(data);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return dataResult;
//    }
//}
