/**
 * 
 */
package com.abdobean.wdcalendar.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author Jenner
 *
 */
@Entity
@Table(name="user" ,catalog="jqcalendar")
public class User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5424821426003070933L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
    private String userName;
    private String userRole;
    @OneToMany
    List<Jqcalendar> jqcalendars;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public List<Jqcalendar> getJqcalendars() {
		return jqcalendars;
	}
	public void setJqcalendars(List<Jqcalendar> jqcalendars) {
		this.jqcalendars = jqcalendars;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", userRole=" + userRole + "]";
	}
    
    
	
}
