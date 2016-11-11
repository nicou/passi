/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.service;

import java.util.List;

import fi.softala.ttl.model.User;

public interface UserService {

	User findById(Integer id);
	
	List<User> findAll();

	void saveOrUpdate(User user);
	
	void delete(int id);
	
}