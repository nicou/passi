/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int userID;
	private String username;
	private String password;
	private String role;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;
	
	public User() {
		super();
		this.userID = 0;
		this.username = "";
		this.password = "";
		this.role = "";
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.phone = "";
	}

	public User(int userID, String username, String password, String role, String firstname, String lastname, String email, String phone) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.role = role;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
	}
	
	public void reset() {
		this.userID = 0;
		this.username = "";
		this.password = "";
		this.role = "";
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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