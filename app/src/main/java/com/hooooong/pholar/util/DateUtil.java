package com.hooooong.pholar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Android Hong on 2017-11-09.
 */

public class DateUtil {

    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;


    /**
     * 현재 년월일시분초를 가져오는 메소드
     *
     * @return
     */
    public static String currentYMDHMSDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 현재 년월일을 가져오는 메소드
     *
     * @return
     */
    public static String currentYMDDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    /**
     * 방금, 몇초, 몇분, 몇일, 몇시 전으로 표현
     *
     * @param postDate
     * @return
     * @throws ParseException
     */
    public static String calculateTime(String postDate) {
        String msg;

        try {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = transFormat.parse(postDate);

            long curTime = System.currentTimeMillis();
            long regTime = date.getTime();
            long diffTime = (curTime - regTime) / 1000;

            if (diffTime < SEC) {
                msg = diffTime + "초전";
            } else if ((diffTime /= DateUtil.SEC) < DateUtil.MIN) {
                msg = diffTime + "분전";
            } else if ((diffTime /= DateUtil.MIN) < DateUtil.HOUR) {
                msg = (diffTime) + "시간전";
            } else if ((diffTime /= DateUtil.HOUR) < DateUtil.DAY) {
                msg = (diffTime) + "일전";
            } else if ((diffTime /= DateUtil.DAY) < DateUtil.MONTH) {
                msg = (diffTime) + "달전";
            } else {
                msg = (diffTime) + "년전";
            }
        } catch (ParseException e) {
            return "오류";
        }

        return msg;
    }
}
