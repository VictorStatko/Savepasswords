package com.statkolibraries.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateTimeUtils {
    private static final String RFC_1123_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final String GMT = "GMT";

    private DateTimeUtils() {
    }

    public static String dateToHttpFormat(Date date) {
        DateFormat df = new SimpleDateFormat(RFC_1123_DATE_FORMAT, java.util.Locale.US);
        df.setTimeZone(TimeZone.getTimeZone(GMT));
        return df.format(date);
    }
}
