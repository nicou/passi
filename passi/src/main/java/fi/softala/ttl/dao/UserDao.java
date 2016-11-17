package fi.softala.ttl.dao;

import java.util.List;

import fi.softala.ttl.model.User;

public interface UserDao {

	User findById(Integer id);

	List<User> findAll();

	void save(User user);

	void update(User user);

	void delete(Integer id);

}