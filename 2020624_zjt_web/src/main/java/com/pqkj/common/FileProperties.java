package com.pqkj.common;

import com.pq.framework.common.FilePropertiesBase;


public class FileProperties extends FilePropertiesBase {

	private static FileProperties inst=null;
	private final String webService = "webservice.";
	private final String ALERT_STARTTIME = "alert.startTime";
	private final String TIMER = "alert.timer";
	private static final String MHS_CENTER_URL="site.zzry.center";

	public String getWebService(String subName){
		return getValue(webService.concat(subName));
	}
	public String getAlertStartTime(){
		return getValue(ALERT_STARTTIME);
	}
	public String getAlertTimer(){
		return getValue(TIMER);
	}
    public static String getMhsCenterUrl(){
    	return getValue(MHS_CENTER_URL);
    }
}
