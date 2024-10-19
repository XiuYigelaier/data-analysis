package com.example.core.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class DateUtil {
    private static final List<String> formarts = new ArrayList(4);
    public static final String PATTERN_YEAR = "yyyy";
    public static final String PATTERN_YEARMOTH = "yyyy-MM";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_SDATE = "yyyyMMdd";
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_DATEALLTIME = "yyyy-MM-dd HH:mm:ss";
    public static final String[] NUMBERS = new String[]{"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};

    public DateUtil() {
    }

    public static Date convert(String source) {
        try {
            String value = source.trim();
            if ("".equals(value)) {
                return null;
            } else if (source.matches("^\\d{4}-\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(0));
            } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(1));
            } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(2));
            } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(3));
            } else if (source.matches("^\\d{4}/\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(0));
            } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(1));
            } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(2));
            } else if (source.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return parseDate(source, (String)formarts.get(3));
            } else {
                throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static Date parseDate(String dateStr, String format) {
        try {
            Date date = null;
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
            return date;
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static String parseString(Date date, String format) {
        if (null == date) {
            return null;
        } else {
            if (StringUtils.isEmpty(format)) {
                format = "yyyy-MM-dd HH:mm:ss";
            }

            try {
                String dateStr = null;
                DateFormat dateFormat = new SimpleDateFormat(format);
                dateStr = dateFormat.format(date);
                return dateStr;
            } catch (Exception var4) {
                var4.printStackTrace();
                return null;
            }
        }
    }

    public static Date getNow() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getStringNow() {
        return getStringNow((String)null);
    }

    public static String getStringNow(String format) {
        if (null == format) {
            format = "yyyy-MM-dd HH:mm:ss";
        }

        return parseString(getNow(), format);
    }

    public static void main(String[] args) {
        try {
            System.err.println(convert("2014-04"));
            System.err.println(convert("2014-04-02 16:00:01"));
            System.err.println(parseString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception var2) {
        }

    }

    public static String toChinaDateString(Date date) throws ParseException {
        String chinaDateStr = null;
        if (null != date) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = dateFormat.format(date);
            System.out.println(dateStr);
            String[] units = dateStr.split("-");
            StringBuffer chinaDate = new StringBuffer();

            for(int j = 0; j < units.length; ++j) {
                StringBuffer sb = new StringBuffer();

                for(int i = 0; i < units[j].length(); ++i) {
                    sb.append(convertNum(units[j].substring(i, i + 1)));
                    sb.append(j >= 1 && Integer.valueOf(units[j]) > 9 && i == 0 ? NUMBERS[10] : "");
                }

                chinaDate.append((CharSequence)(j >= 1 ? sb.toString().replaceAll("零", "") : sb));
                chinaDate.append(j == 0 ? "年" : (j == 1 ? "月" : "日"));
            }

            chinaDateStr = chinaDate.toString();
        }

        return chinaDateStr;
    }

    private static String convertNum(String str) {
        return NUMBERS[Integer.valueOf(str)];
    }

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
    }
}
