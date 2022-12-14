package com.indonesia.tunaifince.kt.utils;

public class GPSUtilcdp {
    //EARTH_RADIUS代表地球半径
    private static final double EARTH_RADIUS = 6378137.0;

    // 计算两点距离
    public static double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (lat_a * java.lang.Math.PI / 180.0);
        double radLat2 = (lat_b * java.lang.Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * java.lang.Math.PI / 180.0;
        double s = 2 * java.lang.Math.asin(java.lang.Math.sqrt(java.lang.Math.pow(java.lang.Math.sin(a / 2), 2)
                + java.lang.Math.cos(radLat1) * java.lang.Math.cos(radLat2)
                * java.lang.Math.pow(java.lang.Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = java.lang.Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 计算方位角pab，
     * 其中lat_a, lng_a是当前点的纬度和经度； lat_b, lng_b是目标点的纬度和经度
     */
    public static double gps2d(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        lat_a = lat_a * java.lang.Math.PI / 180;
        lng_a = lng_a * java.lang.Math.PI / 180;
        lat_b = lat_b * java.lang.Math.PI / 180;
        lng_b = lng_b * java.lang.Math.PI / 180;
        d = java.lang.Math.sin(lat_a) * java.lang.Math.sin(lat_b) + java.lang.Math.cos(lat_a) * java.lang.Math.cos(lat_b) * java.lang.Math.cos(lng_b - lng_a);
        d = java.lang.Math.sqrt(1 - d * d);
        d = java.lang.Math.cos(lat_b) * java.lang.Math.sin(lng_b - lng_a) / d;
        d = java.lang.Math.asin(d) * 180 / java.lang.Math.PI;
        return d;
    }
}
