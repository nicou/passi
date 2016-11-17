package fi.softala.ttl.dao;

import java.util.List;
import java.util.Set;

import fi.softala.ttl.model.Role;
import fi.softala.ttl.model.User;

public interface UserDAO {

	public User findById(int id);
	
	public User findByUsername(String username);

	public List<User> findAll();

	public void save(User user);

	public void update(User user);

	public void delete(int id);
	
	public Set<Role> findRolesByUserID(int userID);

}