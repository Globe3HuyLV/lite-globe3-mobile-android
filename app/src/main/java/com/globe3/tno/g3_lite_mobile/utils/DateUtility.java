package com.globe3.tno.g3_lite_mobile.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtility {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String DATE_FORMAT_ENQUIRY = "dd MMM yyyy";
    public static final String DATE_FORMAT_DOB = "MM/dd/yyyy";
    public static final String DATE_FORMAT_HOUR_MINUTES = "HH:mm";

    public static String getDateString(Date date) {
        if (date != null) {
            return df.format(date);
        } else {
            return null;
        }
    }

    public static String getDateString(Date date, String format) {
        if (date != null) {
            SimpleDateFormat newDf = new SimpleDateFormat(format);
            return newDf.format(date);
        } else {
            return null;
        }
    }

    public static Date getStringDate(String date_string) {
        if (date_string != null) {
            try {
                return df.parse(date_string);
            } catch (ParseException e) {
                //e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static Date getStringDate(String date_string, String format) {
        if (date_string != null) {
            try {
                SimpleDateFormat newDf = new SimpleDateFormat(format);
                return newDf.parse(date_string);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static int getIntMonth(String month_string) {
        if (month_string != null) {
            try {
                Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(month_string);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                return calendar.get(Calendar.MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }
}
