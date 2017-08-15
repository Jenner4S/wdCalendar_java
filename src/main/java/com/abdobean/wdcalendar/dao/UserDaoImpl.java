/**
 * 
 */
package com.abdobean.wdcalendar.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.abdobean.wdcalendar.model.User;

/**
 * @author Jenner
 *
 */
@SuppressWarnings("unchecked")
@Repository
public class UserDaoImpl implements UserDao {

	private SessionFactory sessionFactory;

	@Autowired
	public UserDaoImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Transactional
	public List<User> findAll() {
		List<User> users = sessionFactory.getCurrentSession().createCriteria(User.class).list();
		return users;
	}

	@Override
	public User findById(long id) {
		User users = (User) sessionFactory.getCurrentSession().createCriteria(User.class).add(Restrictions.idEq(id)).uniqueResult();
		return users;
	}

}
