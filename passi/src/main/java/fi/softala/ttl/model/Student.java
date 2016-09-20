package fi.softala.ttl.model;

import java.io.Serializable;

public class Student implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private String school;
	private String email;
	
	public Student() {
		super();
		this.username = "";
		this.password = "";
		this.firstname = "";
		this.lastname = "";
		this.school = "";
		this.email = "";
	}

	public Student(String username, String password, String firstname, String lastname, String school, String email) {
		super();
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.school = school;
		this.email = email;
	}
	
	public void reset() {
		this.username = "";
		this.password = "";
		this.firstname = "";
		this.lastname = "";
		this.school = "";
		this.email = "";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}