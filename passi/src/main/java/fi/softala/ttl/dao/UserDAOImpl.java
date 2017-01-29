/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
	public void saveUser(User user) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String SQL1 = "INSERT INTO users (username, password, firstname, lastname, email) "
				+ "VALUES ( :username, :password, :password, :firstname, :lastname, :email)";
		
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
	
	@Override
	public boolean isCorrectInstructorKey(String instructorKey) {
		String SQL = "SELECT COUNT(*) FROM configuration WHERE config_name = 'instructor_key' AND config_value = :instructor_key";
		Map<String, String> params = new HashMap<String, String>();
		params.put("instructor_key", instructorKey);
		return namedParameterJdbcTemplate.queryForObject(SQL, params, Integer.class) == 1;
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
	
	@Override
	public boolean setPasswordResetToken(String email, String token) {
		String SQL = "INSERT INTO password_reset (user_id, token, expiration_date) VALUES ((SELECT user_id FROM users WHERE email = :email), :token, :expiration)";
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.DATE, 1);
			Date expiration = cal.getTime();
			Map<String, Object> params = new HashMap<>();
			params.put("email", email);
			params.put("token", token);
			params.put("expiration", expiration);
			return namedParameterJdbcTemplate.update(SQL, params) == 1;
		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
	}
	
	@Override
	public int getUserIdWithToken(String token) {
		String SQL = "SELECT user_id FROM password_reset WHERE token = :token";
		Map<String, String> params = new HashMap<>();
		params.put("token", token);
		try {
			return namedParameterJdbcTemplate.queryForObject(SQL, params, Integer.class);
		} catch (Exception ex) {
			return 0;
		}
	}
	
	@Override
	public boolean resetUserPassword(String token, String password) {
		String SQL1 = "UPDATE users SET password = :password WHERE user_id = (SELECT user_id FROM password_reset WHERE token = :token AND expiration_date >= :date)";
		String SQL2 = "DELETE FROM password_reset WHERE token = :token";
		Map<String, Object> params = new HashMap<>();
		params.put("password", password);
		params.put("token", token);
		params.put("date", new Timestamp(new Date().getTime()));
		try {
			if (namedParameterJdbcTemplate.update(SQL1, params) == 1) {
				return namedParameterJdbcTemplate.update(SQL2, params) == 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
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