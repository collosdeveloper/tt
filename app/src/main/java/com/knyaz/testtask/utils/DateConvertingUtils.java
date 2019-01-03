package com.knyaz.testtask.utils;

import android.content.Context;
import android.text.format.DateUtils;
import com.knyaz.testtask.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateConvertingUtils {
    private static final String DATE_FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    public static String formatDateToVideoItemStyle(Date date, Context context) {
        long incomeTimeMillis = date.getTime();
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceInMillis = currentTimeMillis - incomeTimeMillis;
        if(DateUtils.isToday(incomeTimeMillis)) {
            if(timeDifferenceInMillis < DateUtils.HOUR_IN_MILLIS) {
                return String.format(context.getString(R.string.format_date_with_minutes),
                        (timeDifferenceInMillis / DateUtils.MINUTE_IN_MILLIS));
            } else {
                return String.format(context.getString(R.string.format_date_with_hours),
                        (timeDifferenceInMillis / DateUtils.HOUR_IN_MILLIS));
            }
        } else {
            if(isYesterday(date)) {
                return context.getString(R.string.yesterday);
            } else {
                if(isInLastSevenDays(date)) {
                    return getUKShortDayName(date);
                } else {
                    return getStringDateFromDateWithPattern(date, "MMM dd");
                }
            }
        }
    }

    private static boolean isYesterday(Date date) {
        return DateUtils.isToday(date.getTime() + DateUtils.DAY_IN_MILLIS);
    }

    private static boolean isInLastSevenDays(Date date) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -7);
        return date.getTime() >= cal.getTime().getTime();
    }

    private static String getUKShortDayName(Date date) {
        return new SimpleDateFormat("EEE").format(date);
    }

    private static String getStringDateFromDateWithPattern(Date date, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }

    public static String getStringVideoPostedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_YMD_HMS, Locale.ENGLISH);
        return simpleDateFormat.format(date);
    }
}