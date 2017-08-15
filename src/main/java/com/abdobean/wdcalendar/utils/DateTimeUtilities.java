/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abdobean.wdcalendar.utils;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;

import com.abdobean.wdcalendar.model.Jqcalendar;
import com.abdobean.wdcalendar.model.jqcalendarSummary;

/**
 *
 * @author abdo.talaat
 */
public class DateTimeUtilities {
    public  DateTime[] getWeekRange(String date) {
    		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    		if(LocaleContextHolder.getLocale().getLanguage().equals("en")) {
    			formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
    		}

        DateTime[] dateTimes = new DateTime[2];
        
        DateTime dt = formatter.parseDateTime(date);
        DateTime weekStart = dt.withDayOfWeek(DateTimeConstants.MONDAY).withTimeAtStartOfDay();
        DateTime weekEnd = dt.withDayOfWeek(DateTimeConstants.SUNDAY).plusDays(1).withTimeAtStartOfDay();

        dateTimes[0] = weekStart;
        dateTimes[1] = weekEnd;
        System.out.println("now: " + dt);
        System.out.println("weekStart: " + weekStart);
        System.out.println("weekEnd: " + weekEnd);
        return dateTimes;
    }

    public  DateTime[] getmonthRange(String date) {
    		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    		if(LocaleContextHolder.getLocale().getLanguage().equals("en")) {
			formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		}
        DateTime[] dateTimes = new DateTime[2];
        DateTime dt = formatter.parseDateTime(date);
        DateTime start = dt.withDayOfMonth(1).withTimeAtStartOfDay();
        DateTime end = start.plusMonths(1).minusMillis(1);
        dateTimes[0] = start;
        dateTimes[1] = end;
        System.out.println("now: " + dt);
        System.out.println("weekStart: " + start);
        System.out.println("weekEnd: " + end);
        return dateTimes;
    }

    public  DateTime[] getDayRange(String date) {
    		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    		if(LocaleContextHolder.getLocale().getLanguage().equals("en")) {
			formatter = DateTimeFormat.forPattern("MM/dd/yyyy");
		}
        DateTime[] dateTimes = new DateTime[2];
        DateTime dt = formatter.parseDateTime(date);
        DateTime start = dt.withTimeAtStartOfDay();
        DateTime end = start.plusDays(1);
        dateTimes[0] = start;
        dateTimes[1] = end;
        System.out.println("now: " + dt);
        System.out.println("weekStart: " + start);
        System.out.println("weekEnd: " + end);
        return dateTimes;
    }

    public  DateTime getdateDateTime(String date) {
    		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        if(LocaleContextHolder.getLocale().getLanguage().equals("en")) {
			formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		}
        DateTime dt = dtf.parseDateTime(date);
        return dt;
    }
    
    public  String convertDateTimeToString(DateTime date) {
    		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        if(LocaleContextHolder.getLocale().getLanguage().equals("en")) {
			formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		}
        String dt = date.toString(dtf);
        return dt;
    }
    
    //m/d/Y H:i
    public  String convertDateTimeToJS(DateTime date) {
    		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        final DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        if(LocaleContextHolder.getLocale().getLanguage().equals("en")) {
			formatter = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm");
		}
        String dt = date.toString(dtf);
        return dt;
    }
    
    public  jqcalendarSummary convertFromJqcalendarTojqcalendarSummary(Jqcalendar jqcalendar)
    {
        jqcalendarSummary summary = new jqcalendarSummary();
        
        summary.setColor(jqcalendar.getColor());
        summary.setDescription(jqcalendar.getDescription());
        summary.setEndTime(convertDateTimeToString(jqcalendar.getEndTime()));
        summary.setId(jqcalendar.getId());
        summary.setIsAllDayEvent(jqcalendar.getIsAllDayEvent());
        summary.setLocation(jqcalendar.getLocation());
        summary.setRecurringRule(jqcalendar.getRecurringRule());
        summary.setStartTime(convertDateTimeToString(jqcalendar.getStartTime()));
        summary.setSubject(jqcalendar.getSubject());
        return summary;
    }
    
    
    public JSONObject createJqCalendarListJson(List<Jqcalendar> jqcalendars,DateTime start,DateTime end)
    {
        JSONArray jSONArray = new JSONArray();
        for (Jqcalendar jqcalendar : jqcalendars) {
            
            JSONArray array = new JSONArray();
            array.add(jqcalendar.getId());
            array.add(jqcalendar.getSubject());
            array.add(convertDateTimeToString(jqcalendar.getStartTime()));
            array.add(convertDateTimeToString(jqcalendar.getEndTime()));
            array.add(jqcalendar.getIsAllDayEvent());
            array.add(0);
            array.add(0);
            array.add(jqcalendar.getColor());
            array.add(1);
            array.add(jqcalendar.getLocation());
            
            array.add(jqcalendar.getUser() == null ? "''":jqcalendar.getUser().getUserName());//参与人
            jSONArray.add(array);
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("events", jSONArray);
        jSONObject.put("issort", true);
        jSONObject.put("start", convertDateTimeToString(start));
        jSONObject.put("end", convertDateTimeToString(end));
        jSONObject.put("error", null);
        return jSONObject;
    }
}
