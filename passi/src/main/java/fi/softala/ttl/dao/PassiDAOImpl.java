/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import fi.softala.ttl.model.AnswerWaypointDTO;
import fi.softala.ttl.model.AnswerWorksheetDTO;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Instructor;
import fi.softala.ttl.model.Student;
import fi.softala.ttl.model.WaypointDTO;
import fi.softala.ttl.model.WorksheetDTO;

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
				+ "JOIN ryhma_ope AS rop ON rop.username = ope.username " + "WHERE ryhma_tunnus = ?";
		// Count number of students in a group
		final String SQL3 = "SELECT COUNT(*) FROM ryhma_opi WHERE ryhma_tunnus = ?";
		RowMapper<Group> mapperGroup = new GroupRowMapper();
		List<Group> groups = jdbcTemplate.query(SQL1, mapperGroup);
		RowMapper<Instructor> mapperInstructor = new InstructorRowMapper();
		for (Group group : groups) {
			group.setInstructor(
					jdbcTemplate.queryForObject(SQL2, new Object[] { group.getGroupID() }, mapperInstructor));
			group.setNumGroupMembers(
					jdbcTemplate.queryForObject(SQL3, new Object[] { group.getGroupID() }, Integer.class));
		}
		System.out.println(groups.get(0).getInstructor().getLastname());
		return groups;
	}

	public List<Student> getGroupStudents(Group group) {
		final String SQL = "SELECT opi.username, opi.opi_etu, opi.opi_suku, opi.opi_email, kou.koulu FROM opi AS opi "
				+ "JOIN koulu AS kou ON opi.koulu_id = kou.koulu_id "
				+ "JOIN ryhma_opi AS rop ON rop.username = opi.username " + "WHERE ryhma_tunnus = ?";
		RowMapper<Student> mapper = new StudentRowMapper();
		List<Student> groupStudents = jdbcTemplate.query(SQL, new Object[] { group.getGroupID() }, mapper);
		return groupStudents;
	}
	
	public List<WorksheetDTO> getWorksheets(String groupID) {
		final String SQL1 = "SELECT teh.tk_id, teh.tk_nimi, teh.tk_johdanto, teh.tk_suunnitelma "
				+ "FROM tehtavakortti AS teh "
				+ "JOIN ryhma_tk AS ryh ON ryh.tk_id = teh.tk_id "
				+ "WHERE ryh.ryhma_tunnus = ?";
		
		final String SQL2 = "SELECT eta.etappi_id, eta.tk_id, eta.etappi_tehtava "
				+ "FROM etappi AS eta "
				+ "JOIN tehtavakortti AS teh ON eta.tk_id = teh.tk_id "
				+ "WHERE eta.tk_id = ?";
		
		List<WorksheetDTO> worksheets = new ArrayList<>();
		worksheets = jdbcTemplate.query(SQL1, new Object[] {groupID}, new RowMapper<WorksheetDTO>() {
			
			@Override
			public WorksheetDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				WorksheetDTO worksheet = new WorksheetDTO();
				worksheet.setWorksheetID(rs.getInt("tk_id"));
				worksheet.setHeader(rs.getString("tk_nimi"));
				worksheet.setPreface(rs.getString("tk_johdanto"));
				worksheet.setPlanning(rs.getString("tk_suunnitelma"));
				return worksheet;
			}
		});
		
		for (WorksheetDTO worksheet : worksheets) {
			ArrayList<WaypointDTO> waypoints = new ArrayList<>();
			waypoints = (ArrayList<WaypointDTO>) jdbcTemplate.query(SQL2, new Object[] { worksheet.getWorksheetID() }, new RowMapper<WaypointDTO>() {
				
				@Override
				public WaypointDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					WaypointDTO waypoint = new WaypointDTO();
					waypoint.setWaypointID(rs.getInt("etappi_id"));
					waypoint.setWorksheetID(rs.getInt("tk_id"));
					waypoint.setAssignment(rs.getString("etappi_tehtava"));
					return waypoint;
				}
			});
			worksheet.setWaypoints(waypoints);
		}
		
		return worksheets;
		
	}

	public List<AnswerWorksheetDTO> getAnswers(String groupID, String username) {
		
		final String SQL1 = "SELECT vas.vastaus_id, vas.username, vas.tk_id, vas.v_suunnitelma, vas.ope_kommentti "
				+ "FROM vastaus AS vas "
				+ "JOIN tehtavakortti AS teh ON vas.tk_id = teh.tk_id "
				+ "JOIN ryhma_tk AS ryh ON ryh.tk_id = teh.tk_id "
				+ "WHERE ryh.ryhma_tunnus = ? AND vas.username = ? ";
		
		final String SQL2 = "SELECT eta.etappi_vast_id, eta.vastaus_id, eta.etappi_id, val.valinta_text, eta.ope_kommentti, eta.tekstikentta, eta.kuva_url "
				+ "FROM etappi_valinta AS eta " + "JOIN vastaus AS vas ON eta.vastaus_id = vas.vastaus_id "
				+ "JOIN tehtavakortti AS teh ON vas.tk_id = teh.tk_id "
				+ "JOIN ryhma_tk AS ryh ON ryh.tk_id = teh.tk_id "
				+ "JOIN valinta AS val ON eta.valinta_id = val.valinta_id "
				+ "WHERE eta.vastaus_id = ? AND vas.username = ?";
		
		List<AnswerWorksheetDTO> worksheets = new ArrayList<>();
		worksheets = jdbcTemplate.query(SQL1, new Object[] {groupID, username}, new RowMapper<AnswerWorksheetDTO>() {
			
			@Override
			public AnswerWorksheetDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				AnswerWorksheetDTO worksheet = new AnswerWorksheetDTO();
				worksheet.setAnswerID(rs.getInt("vastaus_id"));
				worksheet.setWorksheetID(rs.getInt("tk_id"));
				worksheet.setUsername(rs.getString("username"));
				worksheet.setPlanningText(rs.getString("v_suunnitelma"));
				worksheet.setInstructorComment(rs.getString("ope_kommentti"));
				return worksheet;
			}
		});
		
		ArrayList<AnswerWaypointDTO> answerWaypoints = new ArrayList<>();
		
		for (AnswerWorksheetDTO worksheet : worksheets) {
			answerWaypoints = (ArrayList<AnswerWaypointDTO>) jdbcTemplate.query(SQL2, new Object[] {worksheet.getAnswerID(), username}, new RowMapper<AnswerWaypointDTO>() {
			
				@Override
				public AnswerWaypointDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
					AnswerWaypointDTO answer = new AnswerWaypointDTO();
					answer.setAnswerWaypointID(rs.getInt("etappi_vast_id"));
					answer.setAnswerID(rs.getInt("vastaus_id"));
					answer.setWaypointID(rs.getInt("etappi_id"));
					answer.setSelectedOption(rs.getString("valinta_text"));
					answer.setInstructorComment(rs.getString("ope_kommentti"));
					answer.setAnswerText(rs.getString("tekstikentta"));
					answer.setImageURL(rs.getString("kuva_url"));
					return answer;
				}
			});
			worksheet.setWaypoints(answerWaypoints);
		}
		
		return worksheets; 
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
	 * public boolean addStudent(Student student, String groupID) { final String
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
