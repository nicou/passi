/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

public class User {
	
	private String username;
	@SuppressWarnings("unused")
	private String password;
	private String role;
	
	public User() {
		super();
	}
	
	public User(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}