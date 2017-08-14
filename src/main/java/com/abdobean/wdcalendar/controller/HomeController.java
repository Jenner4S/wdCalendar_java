package com.abdobean.wdcalendar.controller;

import com.abdobean.wdcalendar.dao.JqCalendarDAO;
import com.abdobean.wdcalendar.model.Jqcalendar;
import com.abdobean.wdcalendar.model.Lists;
import com.abdobean.wdcalendar.model.Shop;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {

    @Autowired
    JqCalendarDAO jqCalendarDAO;
    @Autowired
    private HttpServletRequest context;
    @Autowired
    utilities utilities;

    @RequestMapping(value = "/index")
    public ModelAndView home() {
        List<Jqcalendar> listUsers = new ArrayList<Jqcalendar>();
        ModelAndView model = new ModelAndView("home");
        model.addObject("userList", listUsers);
        return model;
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit() {
        ModelAndView model = new ModelAndView("edit");
        String id = context.getParameter("id");
        if (id != null) {
            Jqcalendar jqcalendar = jqCalendarDAO.getcalendar(Integer.parseInt(id));
            String startDateTime = utilities.convertDateTimeToJS(jqcalendar.getStartTime());
            String endDateTime = utilities.convertDateTimeToJS(jqcalendar.getEndTime());
            String[] stDT = startDateTime.split(" ");
            String[] edDT = endDateTime.split(" ");
            model.addObject("event", jqcalendar);
            model.addObject("stpartdate", stDT[0]);
            model.addObject("stparttime", stDT[1]);
            model.addObject("etpartdate", edDT[0]);
            model.addObject("etparttime", edDT[1]);
        }

        return model;
    }

    @RequestMapping(value = "/calendar/rest", method = RequestMethod.POST)
    public @ResponseBody
    String getShopInJSON(@RequestParam("method") String method, @RequestParam(value = "showdate", required = false) String showdate, @RequestParam(value = "viewtype", required = false) String viewtype) {
        String res = "";

        System.out.println(method);
        if (method.equals("list")) {
            System.out.println(showdate);
            System.out.println(viewtype);
            res = list(showdate, viewtype);

        } else if (method.equals("add")) {
            res = addCalendar();
        } else if (method.equals("adddetails")) {
            res = addDetails();
        } else if (method.equals("update")) {
            res = update();
        } else if (method.equals("remove")) {
            res = remove();
        }

        return res;

    }

    public String list(String date, String viewType) {
        DateTime[] dateTimes = null;
        String res = "";

        if (viewType.equals("week")) {
            dateTimes = utilities.getWeekRange(date);

        } else if (viewType.equals("day")) {
            dateTimes = utilities.getDayRange(date);
        } else {
            dateTimes = utilities.getmonthRange(date);
        }


        List<Jqcalendar> jqcalendars = jqCalendarDAO.list(dateTimes[0], dateTimes[1]);
        System.out.println(jqcalendars.size());
        String json = utilities.createJqCalendarListJson(jqcalendars, dateTimes[0], dateTimes[1]);
        return json;
    }

    public String addCalendar() {
        Jqcalendar jqcalendar = new Jqcalendar();
        // System.out.println(context.getParameter("CalendarStartTime"));
        DateTime CalendarStartTime = utilities.getdateDateTime(context.getParameter("CalendarStartTime"));
        DateTime CalendarEndTime = utilities.getdateDateTime(context.getParameter("CalendarEndTime"));
        short IsAllDayEvent = Short.parseShort(context.getParameter("IsAllDayEvent"));
        String CalendarTitle = context.getParameter("CalendarTitle");
        jqcalendar.setStartTime(CalendarStartTime);
        jqcalendar.setEndTime(CalendarEndTime);
        jqcalendar.setIsAllDayEvent(IsAllDayEvent);
        jqcalendar.setSubject(CalendarTitle);

        //  System.out.print(json.get("CalendarStartTime"));
        int id = jqCalendarDAO.add(jqcalendar);
        if (id > 0) {
            return "{\"IsSuccess\":true,\"Msg\":\"add success\",\"Data\":" + id + "}";
        } else {
            return "{\"IsSuccess\":true,\"Msg\":\"add faild\"";
        }
    }

    public String addDetails() {
        String stpartdate = context.getParameter("stpartdate");
        String stparttime = context.getParameter("stparttime");
        String etpartdate = context.getParameter("etpartdate");
        String etparttime = context.getParameter("etparttime");

        String start = stpartdate + " " + stparttime;
        String end = etpartdate + " " + etparttime;


        String IsAllDayEvent = context.getParameter("IsAllDayEvent");
        String CalendarTitle = context.getParameter("Subject");
        String Description = context.getParameter("Description");
        String Location = context.getParameter("Location");
        String colorvalue = context.getParameter("colorvalue");
        String timezone = context.getParameter("timezone");


        Jqcalendar jqcalendar = new Jqcalendar();
        jqcalendar.setColor(colorvalue);
        jqcalendar.setDescription(Description);
        jqcalendar.setEndTime(utilities.getdateDateTime(end));
        jqcalendar.setStartTime(utilities.getdateDateTime(start));
        if (IsAllDayEvent != null) {
            jqcalendar.setIsAllDayEvent(new Short("1"));
        } else {
            jqcalendar.setIsAllDayEvent(new Short("0"));
        }

        jqcalendar.setLocation(Location);
        jqcalendar.setSubject(CalendarTitle);




        int ids = 0;

        String id = context.getParameter("id");
        if (id != null) {
            jqcalendar.setId(Integer.parseInt(id));
            ids = jqCalendarDAO.update(jqcalendar);
        } else {
            ids = jqCalendarDAO.add(jqcalendar);
        }


        if (ids > 0) {
            return "{\"IsSuccess\":true,\"Msg\":\"add success\",\"Data\":" + id + "}";
        } else {
            return "{\"IsSuccess\":true,\"Msg\":\"add faild\"";
        }

    }

    public String update() {

        String calendarId = context.getParameter("calendarId");
        String CalendarStartTime = context.getParameter("CalendarStartTime");
        String CalendarEndTime = context.getParameter("CalendarEndTime");

        int idCal = Integer.parseInt(calendarId);
        Jqcalendar jqcalendar = jqCalendarDAO.getcalendar(idCal);
        jqcalendar.setId(idCal);
        jqcalendar.setStartTime(utilities.getdateDateTime(CalendarStartTime));
        jqcalendar.setEndTime(utilities.getdateDateTime(CalendarEndTime));

        int id = jqCalendarDAO.update(jqcalendar);

        if (id > 0) {
            return "{\"IsSuccess\":true,\"Msg\":\"add success\",\"Data\":" + id + "}";
        } else {
            return "{\"IsSuccess\":true,\"Msg\":\"add faild\"";
        }


    }

    public String remove() {
        String calendarId = context.getParameter("calendarId");
        jqCalendarDAO.remove(Integer.parseInt(calendarId));
        return "{\"IsSuccess\":true,\"Msg\":\"add success\"}";
    }
}
