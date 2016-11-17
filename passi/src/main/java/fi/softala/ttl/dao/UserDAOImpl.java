/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import fi.softala.ttl.model.Role;
import fi.softala.ttl.model.User;

@Repository
public class UserDAOImpl implements UserDAO {

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DataAccessException {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@Override
	public User findById(int id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String sql = "SELECT * FROM users WHERE user_id = :id";
		User result = null;
		try {
			result = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return result;
	}

	@Override
	public User findByUsername(String username) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		String sql = "SELECT * FROM users WHERE username = :username";
		User result = null;
		try {
			result = namedParameterJdbcTemplate.queryForObject(sql, params, new UserMapper());
		} catch (EmptyResultDataAccessException e) {
			// do nothing, return null
		}
		return result;
	}

	@Override
	public List<User> findAll() {
		String sql = "SELECT * FROM users";
		List<User> result = namedParameterJdbcTemplate.query(sql, new UserMapper());
		return result;
	}

	@Override
	public void save(User user) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String SQL1 = "INSERT INTO users (username, password, firstname, lastname, email, phone) "
				+ "VALUES ( :username, :password, :password, :firstname, :lastname, :email, :phone)";
		
		namedParameterJdbcTemplate.update(SQL1, getSqlParameterByModel(user), keyHolder);
		user.setUserID(keyHolder.getKey().intValue());
		
		final String SQL2 = "INSERT INTO user_role VALUES user_id = :uid, role_id = :rid";
		
		for (Role role : user.getRoles()) {
			namedParameterJdbcTemplate.update(SQL2, new MapSqlParameterSource("uid", user.getUserID()).addValue("rid", role.getId()));
		}
	}
	
	private SqlParameterSource getSqlParameterByModel(User user) {
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", user.getUserID());
		paramSource.addValue("password", user.getPassword());
		paramSource.addValue("firstname", user.getFirstname());
		paramSource.addValue("lastname", user.getLastname());
		paramSource.addValue("email", user.getEmail());
		paramSource.addValue("phone", user.getPhone());
		return paramSource;
	}

	@Override
	public void update(User user) {
		String sql = "UPDATE users SET password = :password, firstname = :firstname, lastname = :lastname, email = :email "
				+ "WHERE user_id = :id";
		namedParameterJdbcTemplate.update(sql, getSqlParameterByModel(user));
	}

	@Override
	public void delete(int id) {
		String sql = "DELETE FROM users WHERE user_id = :id";
		namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
	}

	private static final class UserMapper implements RowMapper<User> {

		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserID(rs.getInt("user_id"));
			user.setUsername(rs.getString("username"));
			user.setPassword(rs.getString("password"));
			user.setFirstname(rs.getString("firstname"));
			user.setLastname(rs.getString("lastname"));
			user.setEmail(rs.getString("email"));
			user.setPhone(rs.getString("phone"));
			return user;
		}
	}
	
	@Override
	public Set<Role> findRolesByUserID(int userID) {
		
		final String SQL = "SELECT role_id, role_name FROM roles "
				+ "JOIN user_role ON user_role.role_id = roles.role_id "
				+ "WHERE user_role.user_id = :id";
		
		List<Role> result = namedParameterJdbcTemplate.query(SQL, new MapSqlParameterSource("id", userID), new RowMapper<Role>(){
			
			@Override
			public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
				Role role = new Role();
				role.setId(rs.getInt("role_id"));
				role.setName(rs.getString("role_name"));
				return role;
			}
		});
		
		Set<Role> roles = new HashSet<Role>(result);
		
		return roles;
	}

	@SuppressWarnings("unused")
	private static List<String> convertDelimitedStringToList(String delimitedString) {
		List<String> result = new ArrayList<String>();
		if (!StringUtils.isEmpty(delimitedString)) {
			result = Arrays.asList(StringUtils.delimitedListToStringArray(delimitedString, ","));
		}
		return result;
	}

	@SuppressWarnings("unused")
	private String convertListToDelimitedString(List<String> list) {
		String result = "";
		if (list != null) {
			result = StringUtils.arrayToCommaDelimitedString(list.toArray());
		}
		return result;
	}
}