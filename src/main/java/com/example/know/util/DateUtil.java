package com.example.know.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Date工具类
 *
 * @author bookWorm
 */
public class DateUtil {
    public final static String FORMAT_STRING_TIME = "yyyy-MM-dd HH:mm:ss";

    public final static String FORMAT_STRING_DATE = "yyyy-MM-dd";

    public final static String[] REPLACE_STRING = new String[]{"GMT+0800", "GMT+08:00"};

    public final static String SPLIT_STRING = "(中国标准时间)";

    public final static String FORMAT_STRING2 = "EEE MMM dd yyyy HH:mm:ss z";

    /**
     * 时间解析 Sun Jan 05 2020 00:00:00 GMT 0800 (中国标准时间)
     *
     * @param dateString
     * @return
     */

    public static String timeCycle(String dateString) {
        try {
            dateString = dateString.split(Pattern.quote(SPLIT_STRING))[0].replace(REPLACE_STRING[0], REPLACE_STRING[1]);
            //转换为date
            SimpleDateFormat sf1 = new SimpleDateFormat(FORMAT_STRING2, Locale.ENGLISH);

            Date date = sf1.parse(dateString);

            return new SimpleDateFormat(FORMAT_STRING_TIME).format(date);

        } catch (Exception e) {
            throw new RuntimeException("时间转化格式错误" + "[dateString=" + dateString + "]" + "[FORMAT_STRING=" + FORMAT_STRING_TIME + "]");

        }

    }

    /**
     * 测试
     *
     * @param args
     */
    public static ThreadLocal<DateFormat> chinaDateSDF = new ThreadLocal<DateFormat>() {

        @Override
        protected DateFormat initialValue() {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return df;

        }
    };

    /**
     * 获取日期yyyy-MM-dd格式字符串
     *
     * @param date
     * @param locale
     * @return
     */
    public static String dateToStr(Date date, Locale locale) {
        if (locale == null) {

        }
        return chinaDateSDF.get().format(date);
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

}
