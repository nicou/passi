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
	
	public void addGroup(Group group) {
		final String sql = "INSERT INTO Ryhma (ryhma_tunnus, ryhma_nimi, ope_id) VALUES (?, ?, ?) "
				+ " ON DUPLICATE KEY UPDATE ryhma_nimi=?";
		jdbcTemplate.update(sql, new Object[] {group.getGroupAbbr(), group.getGroupName(), group.getGroupLeadID(), group.getGroupName()});
	}

	public List<Group> getGroups() {
		final String sql1 = "SELECT Ryhma.ryhma_id, Ryhma.ryhma_tunnus, Ryhma.ryhma_nimi, Ryhma.ope_id, Opettaja.ope_etunimi, Opettaja.ope_sukunimi FROM Ryhma JOIN Opettaja ON Ryhma.ope_id = Opettaja.ope_id WHERE ryhma_id > 20";
		final String sql2 = "SELECT COUNT(*) FROM RyhmanOpiskelija WHERE ryhma_id = ?";
		RowMapper<Group> mapper = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(sql1, mapper);
		for (Group group : groups) {
			group.setNumGroupMembers(jdbcTemplate.queryForObject(sql2, new Object[] {group.getGroupID()}, Integer.class));
		}
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
