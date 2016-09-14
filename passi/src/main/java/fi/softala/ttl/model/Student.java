package fi.softala.ttl.model;

import java.io.Serializable;

public class Student implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int studentID;
	private String username;
	private String firstname;
	private String lastname;
	private String school;
	private String email;

	public Student() {
		super();
		this.studentID = 0;
		this.username = "";
		this.firstname = "";
		this.lastname = "";
		this.school = "";
		this.email = "";
	}
	
	public Student(int studentID, String username, String firstname, String lastname, String school, String email) {
		super();
		this.studentID = studentID;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.school = school;
		this.email = email;
	}

	public int getStudentID() {
		return studentID;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
