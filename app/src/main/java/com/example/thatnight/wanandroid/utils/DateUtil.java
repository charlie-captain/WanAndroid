package com.example.thatnight.wanandroid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;


import com.example.thatnight.wanandroid.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ThatNight on 2017.6.22.
 */

public class DateUtil {

    private static boolean isShowTimes = false;

    public static String dateFormat(Date date) {
        if (date == null) {
            return "";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = format.format(date);
        return formatDate;
    }

    public static String dateFormatDay(Date date) {
        if (date == null) {
            return "";
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String dateFormatMinute(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String formatDate = format.format(date);
        return formatDate;
    }

    public static Date stringFormatDate(String s) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy - MM - dd");
        Date date = new Date();
        try {
            date = format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateFormatMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM - dd");
        String formatDate = format.format(date);
        return formatDate;
    }

    public static String dateFormathour(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH : mm");
        String formatDate = format.format(date);
        return formatDate;
    }

    /**
     * 将毫秒转规范时间 HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getCountTimeFormat(Long time) {
        int totalTime = (int) (time / 1000);
        int day = 0, hour = 0, minute = 0, second = 0;

        if (3600 <= totalTime) {
            hour = totalTime / 3600;
            totalTime -= 3600 * hour;
            if (24 <= hour) {
                day = hour / 24;
                hour %= 24;
            }
        }
        if (60 <= totalTime) {
            minute = totalTime / 60;
            totalTime -= minute * 60;
        }
        //        if (0 <= totalTime) {
        //            second = totalTime;
        //        }
        StringBuffer sb = new StringBuffer();
        //        sb.append("剩余");
        if (1 <= day) {
            sb.append(day + "天");
        }
        if (hour < 10) {
            sb.append("0");
        }
        sb.append(hour).append("时");
        if (minute < 10) {
            sb.append("0");
        }
        sb.append(minute + "分");
        //        if (second < 10) {
        //            sb.append("0");
        //        }
        //        sb.append(second);
        return sb.toString();
    }

    /**
     * 控制时间, 如果为当天就显示时间12:02, 否则显示日期如12-5
     *
     * @param dateString
     * @return
     */
    public static String dateFormatClear(String dateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date nowDate = new Date();
        String nowS = dateFormatDay(nowDate);
        String dateS = dateFormatDay(date);
        if (nowS.equals(dateS)) {
            return dateFormathour(date);
        }
        return dateFormatMonth(date);
    }

    /**
     * 显示今天距离还有多少天
     *
     * @param dateString 输入时间
     * @return
     */
    public static String dateFormatDays(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long days = -1;
        Date nowDate = getDate();
        try {
            Date preDate = sdf.parse(dateString);
            days = (nowDate.getTime() - preDate.getTime()) / (1000 * 3600 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果小于1天
        if (days <= 0) {
            long times = 0;
            try {
                Date preDate = sdf.parse(dateString);
                times = (nowDate.getTime() - preDate.getTime()) / (1000 * 60);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (times <= 0) {
                return getDateFixedString("刚刚");
            } else if (times >= 60) {
                times /= 60;
                return getDateFixedString(times + " 小时前");
            } else {
                return getDateFixedString(times + " 分钟前"); //如果是今天
            }
        }
        return getDateFixedString(days + " 天前");
    }

    public static String getDateFixedString(String s) {
        return s;
    }




    public static Date getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String date = sdf.format(calendar.getTime());
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTime(String dateTime) {
        String returnTime = "";
        if (isShowTimes) {
            returnTime = dateFormatDays(dateTime);
        } else {
            returnTime = dateFormatClear(dateTime);
        }
        return returnTime;
    }

    public static void setIsShowTimes(boolean details) {
        isShowTimes = details;
    }

}
