package com.jbd.thymeleaf_start.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static long getTimeMillis() {

    	Instant instant = Instant.now();
    	long timeStampMillis = instant.toEpochMilli();
    	
        return timeStampMillis;
    }
    
    public static long getTimeMillisAfter30Days() {

    	Instant instant = Instant.now();
    	Instant value = instant.plus(30, ChronoUnit.DAYS);
    	long timeStampMillis = value.toEpochMilli();
    	
        return timeStampMillis;
    }
    
    public static Date getDateFromLong(final long date) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println(sdf.format(new Date(date)));
        
        try {
			return sdf.parse(sdf.format(new Date(date * 1000L)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        return null;
    }
    
   public static Date getFirstDayOfMonth() {
    	
	   Calendar cal = Calendar.getInstance();
       cal.setTime(new Date());
       cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
       return cal.getTime();
    }
   
   public static Date getLastDayOfMonth() {
   	
	   Calendar cal = Calendar.getInstance();
       cal.setTime(new Date());
       cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
       return cal.getTime();

   }
}
