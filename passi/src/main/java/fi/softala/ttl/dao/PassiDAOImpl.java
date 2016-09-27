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
import fi.softala.ttl.model.Instructor;
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
		// All groups
		final String SQL1 = "SELECT ryh.ryhma_tunnus, ryh.ryhma_nimi FROM ryhma AS ryh";
		// Instructor (group teacher)
		final String SQL2 = "SELECT ope.username, ope.ope_etu, ope.ope_suku, ope.ope_email, kou.koulu FROM ope AS ope "
				+ "JOIN koulu AS kou ON ope.koulu_id = kou.koulu_id "
				+ "JOIN ryhma_ope AS rop ON rop.username = ope.username "
				+ "WHERE ryhma_tunnus = ?";
		// Count number of students in a group
		final String SQL3 = "SELECT COUNT(*) FROM ryhma_opi WHERE ryhma_tunnus = ?";
		RowMapper<Group> mapperGroup = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(SQL1, mapperGroup);
		RowMapper<Instructor> mapperInstructor = new InstructorRowMapper();
		for (Group group : groups) {
			group.setInstructor(jdbcTemplate.queryForObject(SQL2, new Object[] {group.getGroupID()}, mapperInstructor));
			group.setNumGroupMembers(jdbcTemplate.queryForObject(SQL3, new Object[] {group.getGroupID()}, Integer.class));
		}
		return groups;
	}
	
	public List<Student> getGroupStudents(String groupID) {
		final String sql = "SELECT opi.username, opi.opi_etu, opi.opi_suku, opi.opi_email, kou.koulu FROM opi AS opi "
				+ "JOIN koulu AS kou ON opi.koulu_id = kou.koulu_id "
				+ "JOIN ryhma_opi AS rop ON rop.username = opi.username "
				+ "WHERE ryhma_tunnus = ?";
		RowMapper<Student> mapper = new StudentRowMapper();
		List<Student> groupStudents = jdbcTemplate.query(sql, new Object[] {groupID}, mapper);
		return groupStudents;
	}
	
	/*
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
		final String sql1 = "INSERT INTO user (username, password) VALUES (?, ?) ON DUPLICATE KEY UPDATE password = ?";
		final String sql2 = "INSERT IGNORE INTO koulu (koulu) VALUES (?)";
		final String sql3 = "INSERT INTO opi (username, opi_etu, opi_suku, opi_email) VALUES (?, ?, ?, ?) "
				+ "ON DUPLICATE KEY UPDATE opi_etu = ?, opi_suku = ?, opi_email = ?";		
		final String sql4 = "INSERT IGNORE INTO ryhma_opi (ryhma_tunnus, username) VALUES (?, ?)";
		try {
			jdbcTemplate.update(sql1, new Object[] {student.getUsername(), student.getPassword()});
			jdbcTemplate.update(sql2, new Object[] {student.getSchool(), student.getUsername(), student.getFirstname(), student.getLastname(), 
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
	*/
}
