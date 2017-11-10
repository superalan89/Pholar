package com.hooooong.pholar.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Android Hong on 2017-11-09.
 */

public class DateUtil {

    /**
     * 현재 년월일시분초를 가져오는 메소드
     * @return
     */
    public static String currentYMDHMSDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 현재 년월일을 가져오는 메소드
     * @return
     */
    public static String currentYMDDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }
}
