/**
 * 
 */
package com.abdobean.wdcalendar.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.abdobean.wdcalendar.model.User;

/**
 * @author Jenner
 *
 */
public interface UserDao {

	List<User> findAll();

	User findById(long id);
}
