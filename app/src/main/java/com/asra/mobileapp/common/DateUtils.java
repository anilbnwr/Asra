package com.asra.mobileapp.common;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

public class DateUtils {
    public static final String SIMPLE_DATE_NO_TIME = "yyyy-MM-dd";
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_MMMM_DD_YYYY = "MMMM dd, yyyy HH:mm:ss";

    public static final String SIMPLE_DATE_FORMAT_NO_TIME_ALLTOGETHER = "yyyyMMdd";

    public static final String SIMPLE_DATE_FORMAT_NO_SECONDS = "yyyy-MM-dd HH:mm";


    public static final String SIMPLE_TIME_FORMAT = "HH:mm:ss a";
    public static final String SIMPLE_TIME_FORMAT_NO_SECONDS = "HH:mm a";

    public static final String MMM_DD_YYYY_DATE_PATTERN = "MMM dd, yyyy";
    public static final String DD_MM_YYYY_DATE_PATTERN = "dd-MM-yyyy";
    public static final String DD_MMM_YYYY_DATE_PATTERN = "dd MMM yyyy";
    public static final String YYYY_MM_DD_DATE_PATTERN = "yyyy-MM-dd";

    public static final String MMYYYY_DATE_PATTERN = "MMyyyy";
    public static final String MMMM_YYYY_DATE_PATTERN = "MMMM yyyy";



    public static String getDateAsString(Date dateToParse, String pattern) {
        String date = (null != dateToParse) ? new SimpleDateFormat(pattern, Locale.CANADA).format(dateToParse) : "";
        return date;
    }


    public static String getToday(String pattern){
        Date today = new Date(System.currentTimeMillis());
        return new SimpleDateFormat(pattern, Locale.CANADA).format(today);

    }

    public static Date convertToDate(String dateToParse, String dateFormat) {
        Date date = null;
        try {
            SimpleDateFormat formattedDate = new SimpleDateFormat(dateFormat, Locale.CANADA);
            date = formattedDate.parse(dateToParse);

            return date;
        } catch (Exception e) {
            Timber.e("Could not parse " + dateToParse + " using the expression: " + dateFormat);
        }

        return new Date();
    }

    public static String getDateAsString(String dateToParse, String dateFormat, String newFormat) {
        Date date = null;
        try {
            SimpleDateFormat formattedDate = new SimpleDateFormat(dateFormat, Locale.CANADA);
            date = formattedDate.parse(dateToParse);

        } catch (Exception e) {
            Timber.e("Could not parse " + dateToParse + " using the expression: " + dateFormat);
        }

        return (null != date) ? new SimpleDateFormat(newFormat, Locale.CANADA).format(date) : "";
    }

    public static String getMonthYear(String dateToParse, String dateFormat) {
        Date date = null;
        String monthYear = "";
        try {
            SimpleDateFormat formattedDate = new SimpleDateFormat(dateFormat, Locale.CANADA);
            date = formattedDate.parse(dateToParse);

        } catch (Exception e) {
            Timber.e("Could not parse " + dateToParse + " using the expression: " + dateFormat);
        }



       if(date != null){
           Calendar calendar = Calendar.getInstance();
           calendar.setTime(date);
           CalendarMonth month = CalendarMonth.byValue(calendar.get(Calendar.MONTH));
           String monthOfYear = month.description();
           int year = calendar.get(Calendar.YEAR);
           monthYear = monthOfYear + " "+year;
       }

       return monthYear;
    }

    public static int getCurrentYear() {
        Date date = new Date();



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Date date = new Date();



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        return calendar.get(Calendar.MONTH);
    }

    public static int getDayOfMonth() {
        Date date = new Date();



        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public enum CalendarMonth {

        JANUARY("January", Calendar.JANUARY), FEBRUARY("February", Calendar.FEBRUARY), MARCH("March", Calendar.MARCH), APRIL("April", Calendar.APRIL), MAY("May", Calendar.MAY), JUNE("June", Calendar.JUNE), JULY("July", Calendar.JULY), AUGUST("August", Calendar.AUGUST), SEPTEMBER("September", Calendar.SEPTEMBER), OCTOBER("October", Calendar.OCTOBER), NOVEMBER("November", Calendar.NOVEMBER), DECEMBER("December", Calendar.DECEMBER);

        private String description;
        private int value;

        private CalendarMonth(String description, int value) {
            this.description = description;
            this.value = value;
        }

        public static CalendarMonth byDescription(String description) {
            for (CalendarMonth type : CalendarMonth.values()) {
                if (type.description.equalsIgnoreCase(description))
                    return type;
            }

            return null;
        }

        public static CalendarMonth byValue(int value) {
            for (CalendarMonth type : CalendarMonth.values()) {
                if (type.value == value)
                    return type;
            }

            return null;
        }

        public String description() {
            return description;
        }

        public int value() {
            return value;
        }
    }


    public static boolean isBeforeToday(String dateToParse, String datePattern){
        try {
            SimpleDateFormat formattedDate = new SimpleDateFormat(datePattern, Locale.CANADA);
            Date date = formattedDate.parse(dateToParse);
            Date today = new Date(System.currentTimeMillis());

            return date.before(today);
        }catch (Exception e){
            Timber.e(e, "Date Formatting failed");

            return false;

        }
    }


}
