package fi.softala.ttl.dao;

/**
 * @author Mika Ropponen
 */

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PassiDAOImpl implements PassiDAO {

	@Inject
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public String getRole(String username) {
		String sql = "SELECT role FROM roles WHERE username=?";
		String role = jdbcTemplate.queryForObject(sql, new Object[] { username }, String.class);
		return role;
	}
}
