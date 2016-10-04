/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import fi.softala.ttl.model.Answerpoint;
import fi.softala.ttl.model.Answersheet;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Instructor;
import fi.softala.ttl.model.User;
import fi.softala.ttl.model.Waypoint;
import fi.softala.ttl.model.Worksheet;

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

	public List<Group> getAllGroups() {
		final String SQL1 = "SELECT * FROM groups ORDER BY group_id";
		final String SQL2 = "SELECT * FROM instructors JOIN group_instructors ON group_instructors.user_id = instructors.user_id WHERE group_instructors.group_id = ? ORDER BY instructors.user_id";
		RowMapper<Group> groupMapper = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(SQL1, groupMapper);
		RowMapper<Instructor> instructorMapper = new InstructorRowMapper();
		for (Group group : groups) {
			group.setInstructors(jdbcTemplate.query(SQL2, new Object[] { group.getGroupID() }, instructorMapper));
		}
		return groups;
	}

	public List<User> getGroupUsers(Group group) {
		final String SQL = "SELECT * FROM Users JOIN group_Users ON group_Users.user_id = Users.user_id WHERE group_Users.group_id = ?";
		RowMapper<User> UserMapper = new UserRowMapper();
		List<User> groupUsers = jdbcTemplate.query(SQL, new Object[] { group.getGroupID() }, UserMapper);
		return groupUsers;
	}
	
	public List<Worksheet> getWorksheets(Group group) {
		final String SQL1 = "SELECT * FROM worksheets JOIN group_worksheets ON group_worksheets.worksheet_id = worksheets.worksheet_id WHERE group_worksheets.group_id = ?";
		final String SQL2 = "SELECT * FROM waypoints WHERE worksheet_id = ?";
		List<Worksheet> worksheets = jdbcTemplate.query(SQL1, new Object[] { group.getGroupID() }, new RowMapper<Worksheet>() {
			
			@Override
			public Worksheet mapRow(ResultSet rs, int rowNum) throws SQLException {
				Worksheet worksheet = new Worksheet();
				worksheet.setWorksheetID(rs.getInt("worksheet_id"));
				worksheet.setWorksheetHeader(rs.getString("worksheet_header"));
				worksheet.setWorksheetPreface(rs.getString("worksheet_preface"));
				worksheet.setWorksheetPlanning(rs.getString("worksheet_planning"));
				return worksheet;
			}
		});
		
		for (Worksheet worksheet : worksheets) {
			List<Waypoint> waypoints = jdbcTemplate.query(SQL2, new Object[] { worksheet.getWorksheetID() }, new RowMapper<Waypoint>() {
				
				@Override
				public Waypoint mapRow(ResultSet rs, int rowNum) throws SQLException {
					Waypoint waypoint = new Waypoint();
					waypoint.setWaypointID(rs.getInt("waypoint_id"));
					waypoint.setWaypointTask(rs.getString("waypoint_task"));
					waypoint.setWaypointPhotoEnabled(rs.getBoolean("waypoint_photo_enabled"));
					waypoint.setWorksheetID(rs.getInt("worksheet_id"));
					return waypoint;
				}
			});
			worksheet.setWaypoints(waypoints);
		}
		return worksheets;
	}

	public List<Answersheet> getAnswers(Group group, User User) {
		final String SQL1 = "SELECT * FROM answer_worksheet WHERE group_id = ? AND user_id = ?";
		final String SQL2 = "SELECT * FROM answer_waypoint WHERE answer_id = ?";
		 List<Answersheet> Answersheets = jdbcTemplate.query(SQL1, new Object[] { group.getGroupID(), User.getUserID() }, new RowMapper<Answersheet>() {
			
			@Override
			public Answersheet mapRow(ResultSet rs, int rowNum) throws SQLException {
				Answersheet worksheet = new Answersheet();
				worksheet.setAnswersheetID(rs.getInt("answer_id"));
				worksheet.setPlanning(rs.getString("answer_planning"));
				worksheet.setInstructorComment(rs.getString("answer_instructor_comment"));
				worksheet.setTimestamp(rs.getTimestamp("answer_timestamp"));
				worksheet.setWorksheetID(rs.getInt("worksheet_id"));
				worksheet.setGroupID(rs.getInt("group_id"));
				worksheet.setUserID(rs.getInt("user_id"));
				return worksheet;
			}
		});
				
		for (Answersheet Answersheet : Answersheets) {
			List<Answerpoint> Answerpoints = jdbcTemplate.query(SQL2, new Object[] { Answersheet.getAnswersheetID() }, new RowMapper<Answerpoint>() {
			
				@Override
				public Answerpoint mapRow(ResultSet rs, int rowNum) throws SQLException {
					Answerpoint answer = new Answerpoint();
					answer.setAnswerpointID(rs.getInt("answer_wp_id"));
					answer.setAnswer(rs.getString("answer_wp_text"));
					answer.setInstructorComment(rs.getString("answer_wp_instructor_comment"));
					answer.setImageUrl(rs.getString("answer_wp_image_url"));
					answer.setAnswersheetID(rs.getInt("answer_id"));
					answer.setWaypointID(rs.getInt("waypoint_id"));
					answer.setOptionID(rs.getInt("option_id"));
					return answer;
				}
			});
			Answersheet.setWaypoints(Answerpoints);
		}
		return Answersheets; 
	}

	/*
	 * public boolean addGroup(Group group) { final String sql =
	 * "INSERT INTO ryhma (ryhma_tunnus, ryhma_nimi) VALUES (?, ?) ON DUPLICATE KEY UPDATE ryhma_nimi=?"
	 * ; try { jdbcTemplate.update(sql, new Object[] {group.getGroupID(),
	 * group.getGroupName(), group.getGroupName()}); } catch (Exception e) {
	 * return false; } return true; }
	 * 
	 * public boolean deleteGroup(String groupID) { final String sql =
	 * "DELETE FROM ryhma WHERE ryhma_tunnus = ?"; try {
	 * jdbcTemplate.update(sql, new Object[] {groupID}); } catch (Exception e) {
	 * return false; } return true; }
	 * 
	 * public boolean editGroup(String groupID, String groupName) { final String
	 * sql = "UPDATE ryhma SET ryhma_nimi = ? WHERE ryhma_tunnus = ?"; try {
	 * jdbcTemplate.update(sql, new Object[] {groupName, groupID}); } catch
	 * (Exception e) { return false; } return true; }
	 * 
	 * public boolean addStudent(User student, String groupID) { final String
	 * sql1 =
	 * "INSERT INTO user (username, password) VALUES (?, ?) ON DUPLICATE KEY UPDATE password = ?"
	 * ; final String sql2 = "INSERT IGNORE INTO koulu (koulu) VALUES (?)";
	 * final String sql3 =
	 * "INSERT INTO opi (username, opi_etu, opi_suku, opi_email) VALUES (?, ?, ?, ?) "
	 * + "ON DUPLICATE KEY UPDATE opi_etu = ?, opi_suku = ?, opi_email = ?";
	 * final String sql4 =
	 * "INSERT IGNORE INTO ryhma_opi (ryhma_tunnus, username) VALUES (?, ?)";
	 * try { jdbcTemplate.update(sql1, new Object[] {student.getUsername(),
	 * student.getPassword()}); jdbcTemplate.update(sql2, new Object[]
	 * {student.getSchool(), student.getUsername(), student.getFirstname(),
	 * student.getLastname(), student.getSchool(), student.getEmail()});
	 * jdbcTemplate.update(sql3, new Object[] {groupID, student.getUsername()});
	 * } catch (Exception e) { return false; } return true; }
	 * 
	 * public boolean deleteStudent(String studentID) { final String sql =
	 * "DELETE FROM user WHERE username = ?"; try { jdbcTemplate.update(sql, new
	 * Object [] {studentID}); } catch (Exception e) { return false; } return
	 * true; }
	 */
}
