package com.pqkj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pq.framework.util.DateTimeUI;
import com.pq.framework.util.RandomGUID;
import com.pqkj.common.utils.DataResult;
import com.pqkj.entity.WarningUserRelateView;
import com.pqkj.entity.ZjtWarningRecord;
import com.pqkj.entity.ZjtZjry;
import com.pqkj.service.IWarningUserRelateViewService;
import com.pqkj.service.IZjtWarningRecordService;
import com.pqkj.service.IZjtZjryService;
import com.pqkj.utils.LatLonUtil;
import com.pqkj.vo.req.ReceiveGPSVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * GPS相关接口
 */
@Api(tags = "GPS相关接口")
@RestController
@RequestMapping("/phone")
public class GPSController {

    @Autowired
    private IWarningUserRelateViewService warningUserRelateViewService;
    @Autowired
    private IZjtZjryService zjtZjryService;
    @Autowired
    private IZjtWarningRecordService zjtWarningRecordService;
    /**
     * 手机获取GPS定位信息发送到后台
     */
    @RequestMapping(value = "gps/receivePhoneGPS",method = RequestMethod.POST)
    public DataResult receivePhoneGPS(@RequestBody ReceiveGPSVO vo){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",vo.getUserId());
        List<WarningUserRelateView> list = warningUserRelateViewService.list(queryWrapper);
        ZjtZjry zjry = zjtZjryService.getById(vo.getUserId());
        zjry.setLastLatitude(vo.getLatitude()+"");
        zjry.setLastLongitude(vo.getLongitude()+"");
        zjry.setLastRecordTime(DateTimeUI.getCurrentDateTime());
        zjry.setIsOnline(1);
        zjry.setUpdateTime(DateTimeUI.getCurrentDateTime());
        zjry.setAddress(vo.getAddress());
        zjtZjryService.updateById(zjry);
        int warningCount = 0;
        if(list.size()>0){
            for (WarningUserRelateView warningUserRelateView : list) {
                double lat = Double.parseDouble(warningUserRelateView.getWarningLatitude());
                double lng = Double.parseDouble(warningUserRelateView.getWarningLongitude());
                double distance = LatLonUtil.getDistance(vo.getLatitude(), vo.getLongitude(),lat, lng);
                System.out.println(distance);
                if(distance>=warningUserRelateView.getWarningRange()){
                    ZjtWarningRecord zjtWarningRecord = new ZjtWarningRecord();
                    zjtWarningRecord.setId(RandomGUID.getDatetUUID());
                    zjtWarningRecord.setUserId(vo.getUserId());
                    zjtWarningRecord.setCreateLatitude(vo.getLatitude()+"");
                    zjtWarningRecord.setCreateLongitude(vo.getLongitude()+"");
                    zjtWarningRecord.setCreateTime(DateTimeUI.getCurrentDateTime());
                    zjtWarningRecord.setWarningId(warningUserRelateView.getWarningId());
                    zjtWarningRecordService.save(zjtWarningRecord);
                    warningCount++;
                }

            }
        }
        String msg = zjry.getUserName()+" : 触发 "+warningCount+" 条预警";
        return DataResult.success(msg);
    }

    public static double pi = Math.PI;
    public static double x_pi = Math.PI * 3000.0 / 180.0;
    public static double a = 6378245.0;
    public static double ee = 0.00669342162296594323;

    public static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
    public static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat,mgLon};
    }
    /**
     * 判断是否在中国
     * @param lat
     * @param lon
     * @return
     */
    public static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004 || lon > 137.8347)
            return true;
        if (lat < 0.8293 || lat > 55.8271)
            return true;
        return false;
    }
    /**
     * 84 ==》 高德
     * @param lat
     * @param lon
     * @return
     */
    public static double[] gps84_To_Gcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat,lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * 高德 ==》 84
     * @param lon * @param lat * @return
     * */
    public static double[] gcj02_To_Gps84(double lat, double lon) {
        double[] gps = transform(lat, lon);
        double lontitude = lon * 2 - gps[1];
        double latitude = lat * 2 - gps[0];
        return new double[]{latitude, lontitude};
    }
    /**
     * 高德 == 》 百度
     * @param lat
     * @param lon
     */
    public static double[] gcj02_To_Bd09(double lat, double lon) {
        double x = lon, y = lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta) + 0.0065;
        double tempLat = z * Math.sin(theta) + 0.006;
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**
     * 百度 == 》 高德
     * @param lat
     * @param lon
     */
    public static double[] bd09_To_Gcj02(double lat, double lon) {
        double x = lon - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double tempLon = z * Math.cos(theta);
        double tempLat = z * Math.sin(theta);
        double[] gps = {tempLat,tempLon};
        return gps;
    }

    /**
     * 百度 == 》 84
     * @param lat
     * @param lon
     * @return
     */
    public static double[] bd09_To_gps84(double lat,double lon){
        double[] gcj02 = bd09_To_Gcj02(lat, lon);
        double[] gps84 = gcj02_To_Gps84(gcj02[0], gcj02[1]);
        //保留小数点后六位
        return gps84;
    }

    /*
     * 保留小数点后六位
     * @param num
     * @return
     */
    private static double retain6(double num){
        String result = String .format("%.6f", num);
        return Double.valueOf(result);
    }


    public static void main(String[] args) {
        double WGS_84_LNG = bd09_To_gps84(22.129751,113.347505)[1];
        double WGS_84_LAT = bd09_To_gps84(22.129751,113.347505)[0];

        System.out.println("WGS_84_LNG = "+WGS_84_LNG);
        System.out.println("WGS_84_LAT = "+WGS_84_LAT);

        System.out.println("HX_LNG = "+gps84_To_Gcj02(WGS_84_LAT, WGS_84_LNG)[0]);
        System.out.println("HX_LAT = "+gps84_To_Gcj02(WGS_84_LAT, WGS_84_LNG)[1]);
    }
}


