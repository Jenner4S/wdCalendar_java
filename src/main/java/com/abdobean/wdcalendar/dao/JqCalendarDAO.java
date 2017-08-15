package com.abdobean.wdcalendar.dao;

import java.util.List;

import org.joda.time.DateTime;

import com.abdobean.wdcalendar.model.Jqcalendar;

public interface JqCalendarDAO {
	/**
	 * 查询日程列表数据
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Jqcalendar> list(DateTime start, DateTime end);

	/**
	 * 日程保存
	 * @param jqcalendar
	 * @return
	 */
	public int add(Jqcalendar jqcalendar);

	/**
	 * 日程更新
	 * @param jqcalendar
	 * @return
	 */
	public int update(Jqcalendar jqcalendar);

	/**
	 * 根据 ID 查询日程
	 * @param id
	 * @return
	 */
	public Jqcalendar getcalendar(int id);

	/**
	 * 删除日程
	 * @param id
	 */
	public void remove(int id);
}
