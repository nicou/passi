package fi.softala.ttl.service;

import javax.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import fi.softala.ttl.dao.UserDAO;
import fi.softala.ttl.model.User;

@Service
public class UserServiceImpl implements UserService {

	@Inject
	private UserDAO dao;

	public UserDAO getDao() {
		return dao;
	}

	public void setDao(UserDAO dao) {
		this.dao = dao;
	}

	private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public void save(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(dao.findRolesByUserID(user.getUserID()));
		dao.saveUser(user);
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
}