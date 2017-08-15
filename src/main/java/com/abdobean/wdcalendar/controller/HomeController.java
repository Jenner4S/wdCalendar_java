package com.abdobean.wdcalendar.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.abdobean.wdcalendar.dao.JqCalendarDAO;
import com.abdobean.wdcalendar.dao.UserDao;
import com.abdobean.wdcalendar.model.Jqcalendar;
import com.abdobean.wdcalendar.model.User;
import com.abdobean.wdcalendar.utils.DateTimeUtilities;

/**
 * Handles requests for the application home page.
 */
@SuppressWarnings("unchecked")
@Controller
public class HomeController {

	@Autowired  
	UserDao userDao;
    @Autowired
    JqCalendarDAO jqCalendarDAO;
    @Autowired
    private HttpServletRequest context;
    @Autowired
    DateTimeUtilities utilities;

    /**
     * 首页 home page
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView home() {
        List<Jqcalendar> listUsers = new ArrayList<Jqcalendar>();
        ModelAndView model = new ModelAndView("home");
        model.addObject("userList", listUsers);
        return model;
    }

    /**
     * edit or new event 
     * 编辑日程和新增日程界面
     * @return
     */
    @RequestMapping(value = "/edit")
    public ModelAndView edit() {
        ModelAndView model = new ModelAndView("edit");
        
        List<User> users =  userDao.findAll();
        String id = context.getParameter("id");
        if (id != null && !id.equals("0")) {
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
            model.addObject("uid", jqcalendar.getUser()==null?"":jqcalendar.getUser().getId());
        }
        model.addObject("users", users);

        return model;
    }

    @RequestMapping(value = "/calendar/rest", method = RequestMethod.POST)
    @ResponseBody
    public  JSONObject getShopInJSON(@RequestParam("method") String method, 
    		@RequestParam(value = "showdate", required = false) String showdate, 
    		@RequestParam(value = "viewtype", required = false) String viewtype) {
    	
        JSONObject ret = new JSONObject();
        System.out.println(method);
        if (method.equals("list")) {
            System.out.println(showdate);
            System.out.println(viewtype);
            ret = list(showdate, viewtype);

        } else if (method.equals("add")) {
        		ret = addCalendar();
        } else if (method.equals("adddetails")) {
        		ret = addDetails();
        } else if (method.equals("update")) {
        		ret = update();
        } else if (method.equals("remove")) {
        		ret = remove();
        }
        return ret;
    }
    /**
     * query list 
     * 查询列表数据
     * @param date
     * @param viewType
     * @return
     */
    public JSONObject list(String date, String viewType) {
        DateTime[] dateTimes = null;
        if (viewType.equals("week")) {
            dateTimes = utilities.getWeekRange(date);
        } else if (viewType.equals("day")) {
            dateTimes = utilities.getDayRange(date);
        } else {
            dateTimes = utilities.getmonthRange(date);
        }

        List<Jqcalendar> jqcalendars = jqCalendarDAO.list(dateTimes[0], dateTimes[1]);
        System.out.println(jqcalendars.size());
        return utilities.createJqCalendarListJson(jqcalendars, dateTimes[0], dateTimes[1]);
    }

    /**
     * quick add calendar event
     * 快速添加日程
     * @return
     */
    public JSONObject addCalendar() {
    		JSONObject ret = new JSONObject();
    		ret.put("IsSuccess", true);
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
        		ret.put("Msg", "add success");
        		ret.put("Data", id);
        } else {
        		ret.put("Msg", "add faild");
        }
        return ret;
    }

    /**
     *  add calendar event detail
     *  点击 NEW CALENDAR 添加日程详细
     * @return
     */
	public JSONObject addDetails() {
    		JSONObject ret = new JSONObject();
    		ret.put("IsSuccess", true);
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
        String userIds = context.getParameter("participant");
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
        
        if (userIds != null) {
        		jqcalendar.setUser(userDao.findById(Long.parseLong(userIds)));
		}

        int ids = 0;

        String id = context.getParameter("id");
        if (id != null) {
            jqcalendar.setId(Integer.parseInt(id));
            ids = jqCalendarDAO.update(jqcalendar);
        } else {
            ids = jqCalendarDAO.add(jqcalendar);
        }


        if (ids > 0) {
        		ret.put("Msg", "addDetails success");
        		ret.put("Data", id);
        } else {
        		ret.put("Msg", "addDetails faild");
        }
        
        return ret;

    }

	/**
	 * update calendar 
	 * 更新日程
	 * @return
	 */
    public JSONObject update() {
    		JSONObject ret = new JSONObject();
		ret.put("IsSuccess", true);
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
	    		ret.put("Msg", "update success");
	    		ret.put("Data", id);
	    } else {
	    		ret.put("Msg", "update faild");
	    }
        return ret;

    }
    
    /**
     * delete calendar
     * 删除日程
     * @return
     */
    public JSONObject remove() {
    		JSONObject ret = new JSONObject();
		ret.put("IsSuccess", true);
        String calendarId = context.getParameter("calendarId");
        jqCalendarDAO.remove(Integer.parseInt(calendarId));
        ret.put("Msg", "remove success");
        return ret;
    }
}
