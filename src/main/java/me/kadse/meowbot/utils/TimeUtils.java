package me.kadse.meowbot.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    public static String timestampToString(long timestamp) {
        Date date = new Date(timestamp*1000);
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Vienna"));
        return formatter.format(date);
    }
}