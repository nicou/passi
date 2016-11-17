/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;
import java.util.Set;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int userID;
	private String username;
	private Set<Role> roles;
	private String password;
	private String confirmPassword;
	private String firstname;
	private String lastname;
	private String email;
	private String phone;

	public User() {
		super();
		this.userID = 0;
		this.username = "";
		this.roles = null;
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.phone = "";
	}

	public User(int userID, String username, Set<Role> roles, String password, String confirmPassword, String firstname, String lastname, String email, String phone) {
		super();
		this.userID = userID;
		this.username = username;
		this.roles = roles;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.phone = phone;
	}
	
	public void reset() {
		this.userID = 0;
		this.password = "";
		this.confirmPassword = "";
		this.firstname = "";
		this.lastname = "";
		this.email = "";
		this.phone = "";
	}
	
	public boolean isNew() {
		return (this.userID == 0);
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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
	
	// without roles, password, confirmPassword
	@Override
	public String toString() {
		return "User [userID=" + userID + ", username=" + username + ", firstname=" + firstname + ", lastname="
				+ lastname + ", email=" + email + ", phone=" + phone + "]";
	}
}