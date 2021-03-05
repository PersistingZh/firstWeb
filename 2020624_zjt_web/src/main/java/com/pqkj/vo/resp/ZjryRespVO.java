package com.pqkj.vo.resp;

import lombok.Data;

@Data
public class ZjryRespVO {

    private String phone ;
    private String lastLongitude;
    private String lastLatitude;
    private String address ;
    private String lastRecordTime;
    private String dwfs = "GPS";
    private String dl = "";
    private String wglx = "";
}
