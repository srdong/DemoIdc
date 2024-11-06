package com.teenet.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @ClassName DateUtil
 * @Description TODO
 * @Author twm17
 * @Date 2021/8/4 9:57
 * @Version 1.0
 */
public class DateUtil {

    public static final String FORMAT1 = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT2 = "yyyy-MM-dd HH:mm:00";

    public static final String FORMAT3 = "yyyyMMddHHmmss";

    public static final String FORMAT4 = "yyyy-MM-dd";

    public static String localDateTimeToString(LocalDateTime dateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return dateTime.format(dateTimeFormatter);
    }

    public static String now() {
        LocalDateTime now = LocalDateTime.now();
        return localDateTimeToString(now, FORMAT1);
    }

    public static String now00Str() {
        LocalDateTime now = LocalDateTime.now();
        return localDateTimeToString(now, FORMAT2);
    }

    public static LocalDateTime now00() {
        LocalDateTime now = LocalDateTime.now();
        String now1 = localDateTimeToString(now, FORMAT2);
        return stringToLocalDateTime(now1, FORMAT1);
    }

    public static LocalDateTime stringToLocalDateTime(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(date, formatter);
    }

    public static String addDay(String date, String format, long day) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDate newDate = localDate.plusDays(day);
        return newDate.format(formatter);
    }

}
