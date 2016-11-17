package fi.softala.ttl.service;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(dao.findRolesByUserID(user.getUserID()));
		dao.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
}