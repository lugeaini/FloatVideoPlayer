package com.chenxulu.video.util;

/**
 * Created by xulu on 16/7/14.
 */
public class TimeUtil {

    /**
     * millisecond format to "00:00" or "00:00:00"
     *
     * @param duration
     * @return
     */
    public static String intToStr(int duration) {
        StringBuilder mBuilder = new StringBuilder();
        duration = duration / 1000;

        int hour = duration / 60 / 60;
        int minute = (duration - hour * 60 * 60) / 60;
        int second = duration - hour * 60 * 60 - minute * 60;

        if (hour > 0) {
            mBuilder.append(formatNumber(hour)).append(":");
        }
        mBuilder.append(formatNumber(minute))
                .append(":")
                .append(formatNumber(second));

        return mBuilder.toString();
    }

    /**
     * number must > 0
     *
     * @param number
     * @return
     */
    public static String formatNumber(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        }
        return "0" + number;
    }
}
