/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int userID;
	@JsonIgnore private String username;
	@JsonIgnore private Set<Role> roles;
	@JsonIgnore private String password;
	@JsonIgnore private String confirmPassword;
	private String firstname;
	private String lastname;
	@JsonIgnore private String email;
	@JsonIgnore private String phone;

	public User() {
		super();
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
	
	@JsonIgnore public boolean isNew() {
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
	
	// Setter with BCrypt encoding
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