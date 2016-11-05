/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import fi.softala.ttl.dto.CategoryDTO;
import fi.softala.ttl.dto.GroupDTO;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.model.Answerpoint;
import fi.softala.ttl.model.Answersheet;
import fi.softala.ttl.model.Category;
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

	public List<Group> getAllGroups() {
		final String SQL1 = "SELECT * FROM groups ORDER BY group_name";
		final String SQL2 = "SELECT users.* FROM users "
				+ "JOIN members ON members.user_id = users.user_id "
				+ "WHERE users.role_id = 2 AND users.user_id != 1 AND members.group_id = ?";
		RowMapper<Group> groupMapper = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(SQL1, groupMapper);
		RowMapper<Instructor> instructorMapper = new InstructorRowMapper();
		for (Group group : groups) {
			group.setInstructors((ArrayList<Instructor>) jdbcTemplate.query(SQL2, new Object[] { group.getGroupID() }, instructorMapper));
		}
		return groups;
	}

	public List<User> getGroupMembers(int groupID) {
		final String SQL = "SELECT users.* FROM users "
				+ "JOIN members ON members.user_id = users.user_id "
				+ "WHERE users.role_id = 1 AND members.group_id = ? "
				+ "ORDER BY users.firstname";
		RowMapper<User> userMapper = new UserRowMapper();
		List<User> members = jdbcTemplate.query(SQL, new Object[] { groupID }, userMapper);
		return members;
	}
	
	@Override
	public List<Category> getCategories() {
		final String SQL = "SELECT category_id, category_name FROM categories ORDER BY category_name";
		List<Category> categories = jdbcTemplate.query(SQL, new RowMapper<Category>() {
			
			@Override
			public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
				Category category = new Category();
				category.setCategoryID(rs.getInt("category_id"));
				category.setCategoryName(rs.getString("category_name"));
				return category;
			}			
		});
		return categories;
	}
	
	@Override
	public List<GroupDTO> getGroupsDTO() {
		final String SQL = "SELECT group_id, group_name FROM groups ORDER BY group_name";
		List<GroupDTO> groups = jdbcTemplate.query(SQL, new RowMapper<GroupDTO>() {
			
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
		
		System.out.println(worksheetID + " " + userID);
		
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
	public void saveFeedback(int waypointID, int instructorRating, String instructorComment) {
		final String SQL = "UPDATE answerpoints SET instructor_comment = ?, instructor_rating = ? WHERE waypoint_id = ?";
		jdbcTemplate.update(SQL, new Object[] {instructorComment, instructorRating, waypointID});
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
}
