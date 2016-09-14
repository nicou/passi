/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Student;

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

	public List<Group> getGroups() {
		final String sql = "SELECT ryhma_id, ryhma_tunnus, ryhma_nimi, ope_id FROM Ryhma WHERE ryhma_id > 20";
		RowMapper<Group> mapper = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(sql, mapper);
		return groups;
	}
	
	public List<Student> getGroupStudents(int groupID) {
		final String sql = "SELECT opi_id, username, opi_etunimi, opi_sukunimi, opi_koulu, opi_email FROM Opiskelija WHERE opi_id IN "
				+ "(SELECT opi_id from RyhmanOpiskelija WHERE ryhma_id = ?)";
		RowMapper<Student> mapper = new StudentRowMapper();
		List<Student> members = jdbcTemplate.query(sql, new Object[] {groupID}, mapper);
		return members;
	}
	
	public String getRole(String username) {
		String sql = "SELECT role FROM roles WHERE username=?";
		String role = jdbcTemplate.queryForObject(sql, new Object[] { username }, String.class);
		return role;
	}
}
