/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;

public class Member implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int    userID;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;

	public Member() {
		super();
	}

	public Member(int userID, String firstname, String lastname, String email, String phone) {
		super();
		this.userID = userID;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
	}
	
	public void reset() {
		this.userID = 0;
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.phone = "";
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}