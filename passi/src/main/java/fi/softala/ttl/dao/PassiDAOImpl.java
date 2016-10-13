/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

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

	public void JdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Autowired
	private PlatformTransactionManager platformTransactionManager;
	
	public boolean addGroup(Group group){
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		
		final String SQL1 = "INSERT INTO groups(group_name, group_key) VALUES (?, ?)";	
		try {
			 jdbcTemplate.update(SQL1, new Object[] {group.getGroupName(), group.getGroupKey()});
			 platformTransactionManager.commit(status);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return false;
		} 
		 return true;
	}

	public List<Group> getAllGroups() {
		final String SQL1 = "SELECT * FROM groups ORDER BY group_name";
		final String SQL2 = "SELECT users.* FROM users "
				+ "JOIN members ON members.user_id = users.user_id "
				+ "WHERE users.role_id = 2 AND users.user_id != 1 AND members.group_id = ?";
		RowMapper<Group> groupMapper = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(SQL1, groupMapper);
		RowMapper<Instructor> instructorMapper = new InstructorRowMapper();
		for (Group group : groups) {
			group.setInstructors(jdbcTemplate.query(SQL2, new Object[] { group.getGroupID() }, instructorMapper));
		}
		return groups;
	}

	public List<User> getGroupMembers(Group group) {
		final String SQL = "SELECT * FROM users JOIN members ON members.user_id = users.user_id WHERE users.role_id = 1 AND members.group_id = ?";
		RowMapper<User> userMapper = new UserRowMapper();
		List<User> members = jdbcTemplate.query(SQL, new Object[] { group.getGroupID() }, userMapper);
		return members;
	}
	
	public List<Worksheet> getWorksheets(Group group) {
		final String SQL1 = "SELECT * FROM worksheets JOIN distros ON distros.worksheet_id = distros.worksheet_id WHERE distros.group_id = ?";
		final String SQL2 = "SELECT * FROM waypoints WHERE worksheet_id = ?";
		List<Worksheet> worksheets = jdbcTemplate.query(SQL1, new Object[] { group.getGroupID() }, new RowMapper<Worksheet>() {
			
			@Override
			public Worksheet mapRow(ResultSet rs, int rowNum) throws SQLException {
				Worksheet worksheet = new Worksheet();
				worksheet.setWorksheetID(rs.getInt("worksheet_id"));
				worksheet.setWorksheetHeader(rs.getString("header"));
				worksheet.setWorksheetPreface(rs.getString("preface"));
				worksheet.setWorksheetPlanning(rs.getString("planning"));
				return worksheet;
			}
		});
		
		for (Worksheet worksheet : worksheets) {
			List<Waypoint> waypoints = jdbcTemplate.query(SQL2, new Object[] { worksheet.getWorksheetID() }, new RowMapper<Waypoint>() {
				
				@Override
				public Waypoint mapRow(ResultSet rs, int rowNum) throws SQLException {
					Waypoint waypoint = new Waypoint();
					waypoint.setWaypointID(rs.getInt("waypoint_id"));
					waypoint.setWaypointTask(rs.getString("task"));
					waypoint.setWaypointPhotoEnabled(rs.getBoolean("photo_enabled"));
					waypoint.setWorksheetID(rs.getInt("worksheet_id"));
					return waypoint;
				}
			});
			worksheet.setWaypoints(waypoints);
		}
		return worksheets;
	}

	public List<Answersheet> getAnswers(Group group, User user) {
		final String SQL1 = "SELECT * FROM answersheets WHERE group_id = ? AND user_id = ?";
		final String SQL2 = "SELECT answerpoints.*, options.option_text FROM answerpoints "
				+ "JOIN options ON answerpoints.option_id = options.option_id "
				+ "WHERE answersheet_id = ?";
		 List<Answersheet> answersheets = jdbcTemplate.query(SQL1, new Object[] { group.getGroupID(), user.getUserID() }, new RowMapper<Answersheet>() {
			
			@Override
			public Answersheet mapRow(ResultSet rs, int rowNum) throws SQLException {
				Answersheet answersheet = new Answersheet();
				answersheet.setAnswerID(rs.getInt("answersheet_id"));
				answersheet.setAnswerPlanning(rs.getString("planning"));
				answersheet.setAnswerInstructorComment(rs.getString("instructor_comment"));
				answersheet.setAnswerTimestamp(rs.getTimestamp("timestamp"));
				answersheet.setWorksheetID(rs.getInt("worksheet_id"));
				answersheet.setGroupID(rs.getInt("group_id"));
				answersheet.setUserID(rs.getInt("user_id"));
				return answersheet;
			}
		});
				
		for (Answersheet answersheet : answersheets) {
			List<Answerpoint> answerpoints = jdbcTemplate.query(SQL2, new Object[] { answersheet.getAnswerID() }, new RowMapper<Answerpoint>() {
			
				@Override
				public Answerpoint mapRow(ResultSet rs, int rowNum) throws SQLException {
					Answerpoint answerpoint = new Answerpoint();
					answerpoint.setAnswerWaypointID(rs.getInt("answerpoint_id"));
					answerpoint.setAnswerWaypointText(rs.getString("answer_text"));
					answerpoint.setAnswerWaypointInstructorComment(rs.getString("instructor_comment"));
					answerpoint.setAnswerWaypointImageURL(rs.getString("image_url"));
					answerpoint.setAnswerID(rs.getInt("answersheet_id"));
					answerpoint.setWaypointID(rs.getInt("waypoint_id"));
					answerpoint.setOptionID(rs.getInt("option_id"));
					answerpoint.setOptionText(rs.getString("option_text"));
					return answerpoint;
				}
			});
			answersheet.setWaypoints(answerpoints);
		}
		return answersheets; 
	}
}
