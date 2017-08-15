package com.abdobean.wdcalendar.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.abdobean.wdcalendar.model.Jqcalendar;

public interface JqCalendarDAO {
	public List<Jqcalendar> list(DateTime start,DateTime end);
       public int  add(Jqcalendar jqcalendar);
       public int  update(Jqcalendar jqcalendar);
       public Jqcalendar getcalendar(int id);
       public void remove(int id);
}
