package com.pqkj.utils;
public class LatLonUtil {
    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b){
        double pk = 180 / Math.PI;
        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;
        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6371000 * tt;
    }
    public static void main(String[] args){
        System.out.println(getDistance(22.27881712493085, 113.50750391355456, 22.124887929448974, 113.34036406018829));
    }
}