package com.example.lazykitchen.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    private Calendar calendar;

    public DateUtil() {
        this.calendar = Calendar.getInstance(Locale.CHINA);
    }

    public DateUtil(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDate() {
        return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getNowDay() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHowManyDays() {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public int getWhichDay() {
        Calendar tmp = calendar;
        tmp.set(Calendar.DAY_OF_MONTH, tmp.getActualMinimum(Calendar.DAY_OF_MONTH));
        return tmp.get(Calendar.DAY_OF_WEEK);
    }


}
