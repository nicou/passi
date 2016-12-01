/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import fi.softala.ttl.dto.CategoryDTO;
import fi.softala.ttl.dto.GroupDTO;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.model.Answerpoint;
import fi.softala.ttl.model.Answersheet;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.User;
import fi.softala.ttl.model.Waypoint;
import fi.softala.ttl.model.Worksheet;

@Component
public class PassiDAOImpl implements PassiDAO {
	
	private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

	@Override
	public void saveUser(User user) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String SQL1 = "INSERT INTO users (username, password, firstname, lastname, email) "
				+ "VALUES (?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SQL1, new String[] { "user_id" });
				ps.setString(1, user.getUsername());
				ps.setString(2, passwordEncoder.encode(user.getPassword()));
				ps.setString(3, user.getFirstname());
				ps.setString(4, user.getLastname());
				ps.setString(5, user.getEmail());
				return ps;
			}
		}, keyHolder);
		
		user.setUserID(keyHolder.getKey().intValue());
		
		final String SQL2 = "INSERT INTO user_role (user_id, role_id) VALUES (?, 2)";
		
		jdbcTemplate.update(SQL2, new Object[] { user.getUserID() });
		
	}
	
	public boolean addGroup(Group group, User instructor){
		
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		final String SQL1 = "INSERT INTO groups (group_name, group_key) VALUES (?, ?)";
		final String SQL2 = "SELECT worksheet_id FROM worksheets;";
		final String SQL3 = "INSERT INTO distros VALUES (?, ?)";
		final String SQL4 = "INSERT INTO members (user_id, group_id) VALUES (?, ?)";
		
		try {
			
			// Add new group into group table
			jdbcTemplate.update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(SQL1, new String[] { "group_id" });
					ps.setString(1, group.getGroupName());
					ps.setString(2, group.getGroupKey());
					return ps;
				}
			}, keyHolder);
			
			group.setGroupID(keyHolder.getKey().intValue());
			
			// Get all worksheet IDs from worksheet table
			List<Integer> ids = jdbcTemplate.query(SQL2, new RowMapper<Integer>() {
				
				@Override
				public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
					Integer id = rs.getInt("worksheet_id");
					return id;
				}
			});
			
			// Add worksheet IDs of the new group into distros table
			for (Integer id : ids) {
				jdbcTemplate.update(SQL3, new Object[] { group.getGroupID(), id });
			}
			
			// Add instructor into members table
			jdbcTemplate.update(SQL4, new Object[] { instructor.getUserID(), group.getGroupID() });
					
			platformTransactionManager.commit(status);
			
		} catch (Exception e) {
			
			platformTransactionManager.rollback(status);
			return false;
			
		} 
		return true;
	}
	
	public boolean delGroup(int groupID){
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		
		final String SQL1 = "DELETE FROM groups WHERE group_id = ?";
		int numRows = 0;
		try {
			numRows = jdbcTemplate.update(SQL1, new Object[] { groupID });
			platformTransactionManager.commit(status);
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return false;
		} finally {
			if (numRows == 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean delGroupMember(int userID, int groupID) {
		final String SQL1 = "DELETE FROM members WHERE user_id = ? AND group_id = ?";
		try {
			return jdbcTemplate.update(SQL1, new Object[] { userID, groupID }) == 1;
		} catch (Exception ex) {
			return false;
		}
	}
	
	public boolean editGroup(Group group) {
		final String SQL1 = "UPDATE groups SET group_name = ?, group_key = ? WHERE group_id = ?";
		try {
			return jdbcTemplate.update(SQL1,
					new Object[] { group.getGroupName(), group.getGroupKey(), group.getGroupID() }) == 1;
		} catch (Exception ex) {
			return false;
		}
		
	}

	public List<Group> getAllGroups(String username) {
		final String SQL1 = "SELECT groups.* FROM groups JOIN members USING (group_id) JOIN user_role USING (user_id) WHERE user_id = (SELECT user_id FROM users WHERE username = ?) AND user_role.role_id = 2 ORDER BY group_name";
		final String SQL2 = "SELECT users.* FROM users "
				+ "JOIN members ON members.user_id = users.user_id "
				+ "JOIN user_role ON users.user_id = user_role.user_id "
				+ "WHERE user_role.role_id = 2 AND users.user_id != 1 AND members.group_id = ?";
		RowMapper<Group> groupMapper = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(SQL1, new Object[] { username }, groupMapper);
		RowMapper<User> userRowMapper = new UserRowMapper();
		for (Group group : groups) {
			group.setInstructors((ArrayList<User>) jdbcTemplate.query(SQL2, new Object[] { group.getGroupID() }, userRowMapper));
		}
		return groups;
	}
	
	public Group getGroup(int groupID) {
		Group group = new Group();
		final String SQL1 = "SELECT group_id, group_name, group_key FROM groups WHERE group_id = ?";
		try {
			return jdbcTemplate.query(SQL1, new Object[] { groupID }, new GroupRowMapper()).get(0);
		} catch (Exception ex) {
			return group;
		}
	}

	public List<User> getGroupMembers(int groupID) {
		final String SQL = "SELECT users.* FROM users "
				+ "JOIN members ON members.user_id = users.user_id "
				+ "JOIN user_role ON users.user_id = user_role.user_id "
				+ "WHERE user_role.role_id = 1 AND members.group_id = ? "
				+ "ORDER BY users.firstname";
		RowMapper<User> userMapper = new UserRowMapper();
		List<User> members = jdbcTemplate.query(SQL, new Object[] { groupID }, userMapper);
		return members;
	}
	
	@Override
	public List<GroupDTO> getGroupsDTO(String username) {
		final String SQL = "SELECT g.group_id, g.group_name FROM groups g JOIN members USING (group_id) JOIN user_role USING (user_id) WHERE user_id = (SELECT user_id FROM users WHERE username = ?) AND user_role.role_id = 2";
		List<GroupDTO> groups = jdbcTemplate.query(SQL, new Object[] { username }, new RowMapper<GroupDTO>() {
			
			@Override
			public GroupDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				GroupDTO group = new GroupDTO();
				group.setGroupID(rs.getInt("group_id"));
				group.setGroupName(rs.getString("group_name"));
				return group;
			}
		});
		return groups;
	}
	
	@Override
	public List<CategoryDTO> getCategoriesDTO() {
		final String SQL = "SELECT category_id, category_name FROM categories ORDER BY category_name";
		List<CategoryDTO> categories = jdbcTemplate.query(SQL,  new RowMapper<CategoryDTO>() {
			
			@Override
			public CategoryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				CategoryDTO category = new CategoryDTO();
				category.setCategoryID(rs.getInt("category_id"));
				category.setCategoryName(rs.getString("category_name"));
				return category;
			}
		});
		return categories;
	}
	
	@Override
	public List<WorksheetDTO> getWorksheetsDTO(int groupID, int categoryID) {
		final String SQL = "SELECT worksheets.worksheet_id, worksheets.header FROM worksheets "
				+ "JOIN distros ON distros.worksheet_id = worksheets.worksheet_id "
				+ "JOIN categories ON worksheets.category_id = categories.category_id "
				+ "WHERE distros.group_id = ? AND categories.category_id = ? "
				+ "ORDER BY worksheets.header";
		List<WorksheetDTO> worksheets = jdbcTemplate.query(SQL, new Object[] { groupID, categoryID }, new RowMapper<WorksheetDTO>() {
			
			@Override
			public WorksheetDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorksheetDTO worksheet = new WorksheetDTO();
				worksheet.setWorksheetID(rs.getInt("worksheet_id"));
				worksheet.setWorksheetHeader(rs.getString("header"));
				return worksheet;
			}
		});
		return worksheets;
	}
	
	@Override
	public List<Answersheet> getWorksheetAnswers(int worksheetID, int userID) {
		
		final String SQL1 = "SELECT * FROM answersheets WHERE worksheet_id = ? AND user_id = ?";
		
		final String SQL2 = "SELECT * FROM answerpoints "
				+ "JOIN options ON answerpoints.option_id = options.option_id "
				+ "WHERE answersheet_id = ?";
		
		List<Answersheet> answersheets = jdbcTemplate.query(SQL1, new Object[] { worksheetID, userID }, new RowMapper<Answersheet>() {
			
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
					answerpoint.setAnswerWaypointInstructorRating(rs.getInt("instructor_rating"));
					answerpoint.setAnswerWaypointImageURL(rs.getString("image_url"));
					answerpoint.setAnswerID(rs.getInt("answersheet_id"));
					answerpoint.setWaypointID(rs.getInt("waypoint_id"));
					answerpoint.setOptionID(rs.getInt("option_id"));
					answerpoint.setOptionText(rs.getString("option_text"));
					return answerpoint;
				}
			});
			answersheet.setWaypoints((ArrayList<Answerpoint>) answerpoints);
		}
		return answersheets; 
	}

	@Override
	public List<Worksheet> getWorksheetContent(int worksheetID) {
		
		final String SQL1 = "SELECT * FROM worksheets WHERE worksheet_id = ?";
		
		final String SQL2 = "SELECT * FROM waypoints WHERE worksheet_id = ?";
		
		List<Worksheet> worksheets = jdbcTemplate.query(SQL1, new Object[] { worksheetID }, new RowMapper<Worksheet>() {
			
			@Override
			public Worksheet mapRow(ResultSet rs, int rowNum) throws SQLException {
				Worksheet worksheet = new Worksheet();
				worksheet.setWorksheetID(rs.getInt("worksheet_id"));
				worksheet.setWorksheetHeader(rs.getString("header").trim());
				worksheet.setWorksheetPreface(rs.getString("preface").trim());
				worksheet.setWorksheetPlanning(rs.getString("planning").trim());
				return worksheet;
			}
		});
		
		if (worksheets.isEmpty()) {
			return null;
		}
		
		for (Worksheet worksheet : worksheets) {
			List<Waypoint> waypoints = jdbcTemplate.query(SQL2, new Object[] { worksheet.getWorksheetID() }, new RowMapper<Waypoint>() {
				
				@Override
				public Waypoint mapRow(ResultSet rs, int rowNum) throws SQLException {
					Waypoint waypoint = new Waypoint();
					waypoint.setWaypointID(rs.getInt("waypoint_id"));
					waypoint.setWaypointTask(rs.getString("task").trim());
					waypoint.setWaypointPhotoEnabled(rs.getBoolean("photo_enabled"));
					waypoint.setWorksheetID(rs.getInt("worksheet_id"));
					return waypoint;
				}
			});
			
			worksheet.setWaypoints(waypoints);
		}
		
		return worksheets;
	}

	@Override
	public boolean saveFeedback(int answerWaypointID, int instructorRating, String instructorComment) {
		final String SQL = "UPDATE answerpoints SET instructor_comment = ?, instructor_rating = ? WHERE answerpoint_id = ?";
		return jdbcTemplate.update(SQL, new Object[] {instructorComment, instructorRating, answerWaypointID}) == 1;
	}
	
	@Override public boolean saveInstructorComment(int answersheetID, String instructorComment) {
		final String SQL = "UPDATE answersheets SET instructor_comment = ? WHERE answersheet_id = ?";
		return jdbcTemplate.update(SQL, new Object[] { instructorComment, answersheetID }) == 1;
	}

	@Override
	public HashMap<Integer, Integer> getIsAnsweredMap(int worksheetID, ArrayList<User> groupMembers) {
		
		final String SQL = "SELECT EXISTS (SELECT 1 FROM answersheets WHERE worksheet_id = ? AND user_id = ?)";
		
		HashMap<Integer, Integer> isAnsweredMap = new HashMap<>();
		for (User member : groupMembers) {
			isAnsweredMap.put(member.getUserID(), jdbcTemplate.queryForObject(SQL, new Object[] { worksheetID, member.getUserID() }, Integer.class));
		}
		return isAnsweredMap;
	}

	@Override
	public User getMemberDetails(int userID) {
		final String SQL = "SELECT * FROM users WHERE user_id = ?";
		RowMapper<User> userMapper = new UserRowMapper();
		return jdbcTemplate.queryForObject(SQL, new Object[] { userID }, userMapper);
	}

	@Override
	public List<User> getInstructorsDetails(int groupID) {
		final String SQL = "SELECT users.* FROM users "
				+ "JOIN members ON members.user_id = users.user_id "
				+ "JOIN user_role ON users.user_id = user_role.user_id "
				+ "WHERE members.group_id = ? AND user_role.role_id = 2";
		RowMapper<User> userMapper = new UserRowMapper();
		return jdbcTemplate.query(SQL, new Object[] { groupID }, userMapper);
	}

	@Override
	public boolean isUsernameExists(String username) {
		final String SQL = "SELECT EXISTS (SELECT 1 FROM users WHERE username = ?)";
		int userExists = jdbcTemplate.queryForObject(SQL, new Object[] { username }, Integer.class);
		if (userExists == 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isEmailExists(String email) {
		final String SQL = "SELECT EXISTS (SELECT 1 FROM users WHERE email = ?)";
		int emailExists = jdbcTemplate.queryForObject(SQL, new Object[] { email }, Integer.class);
		if (emailExists == 1) {
			return true;
		}
		return false;
	}
}
