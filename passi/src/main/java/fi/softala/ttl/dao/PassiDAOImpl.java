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
	
	public boolean addGroup(Group group) {
		final String sql = "INSERT INTO ryhma (ryhma_tunnus, ryhma_nimi) VALUES (?, ?) ON DUPLICATE KEY UPDATE ryhma_nimi=?";
		try {
			jdbcTemplate.update(sql, new Object[] {group.getGroupID(), group.getGroupName(), group.getGroupName()});
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean deleteGroup(String groupID) {
		final String sql = "DELETE FROM ryhma WHERE ryhma_tunnus = ?";
		try {
			jdbcTemplate.update(sql, new Object[] {groupID});
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean editGroup(String groupID, String groupName) {
		final String sql = "UPDATE ryhma SET ryhma_nimi = ? WHERE ryhma_tunnus = ?";
		try {
			jdbcTemplate.update(sql, new Object[] {groupName, groupID});
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean addStudent(Student student, String groupID) {
		final String sql1 = "INSERT INTO user (username, password) VALUES (?, ?)";		
		final String sql2 = "INSERT INTO opi (username, opi_etu, opi_suku, opi_koulu, opi_email) VALUES (?, ?, ?, ?, ?)";		
		final String sql3 = "INSERT INTO ryhma_opi (ryhma_tunnus, username) VALUES (?, ?)";
		try {
			jdbcTemplate.update(sql1, new Object[] {student.getUsername(), student.getPassword()});
			jdbcTemplate.update(sql2, new Object[] {student.getUsername(), student.getFirstname(), student.getLastname(), 
				student.getSchool(), student.getEmail()});
			jdbcTemplate.update(sql3, new Object[] {groupID, student.getUsername()});
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean deleteStudent(String studentID) {
		final String sql = "DELETE FROM user WHERE username = ?";
		try {
			jdbcTemplate.update(sql, new Object [] {studentID});
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public List<Group> getGroups() {
		final String sql1 = "SELECT ryhma_tunnus, ryhma_nimi FROM ryhma";
		final String sql2 = "SELECT COUNT(*) FROM ryhma_opi WHERE ryhma_tunnus = ?";
		RowMapper<Group> mapper = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(sql1, mapper);
		for (Group group : groups) {
			group.setNumGroupMembers(jdbcTemplate.queryForObject(sql2, new Object[] {group.getGroupID()}, Integer.class));
		}
		return groups;
	}
	
	public List<Student> getGroupStudents(String groupID) {
		final String sql = "SELECT username, opi_etu, opi_suku, opi_koulu, opi_email FROM opi WHERE username IN "
				+ "(SELECT username from ryhma_opi WHERE ryhma_tunnus = ?)";
		RowMapper<Student> mapper = new StudentRowMapper();
		List<Student> groupStudents = jdbcTemplate.query(sql, new Object[] {groupID}, mapper);
		return groupStudents;
	}
}
