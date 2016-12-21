/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fi.softala.ttl.dto.CategoryDTO;
import fi.softala.ttl.dto.GroupDTO;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.model.Answersheet;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.User;
import fi.softala.ttl.model.Worksheet;

public interface PassiDAO {
	
	public void saveUser(User user);

	public boolean addGroup(Group group, User instructor);
	
	public boolean addGroupInstructor(int groupID, String newSupervisor, String username);
	
	public boolean editGroup(Group group);

	public boolean delGroup(int groupID);
	
	public boolean delGroupMember(int userID, int groupID);
	
	public boolean delGroupInstructor(int userID, int groupID);

	public List<User> getGroupMembers(int groupID);

	public List<GroupDTO> getGroupsDTO(String username);
	
	public Group getGroup(int groupID);

	public List<CategoryDTO> getCategoriesDTO();
	
	public List<WorksheetDTO> getWorksheetsDTO(int groupID, int categoryID);
	
	public List<Answersheet> getWorksheetAnswers(int worksheetID, int userID);

	public List<Worksheet> getWorksheetContent(int worksheetID);
	
	public HashMap<Integer, Integer> getIsAnsweredMap(int worksheetID, ArrayList<User> groupMembers);

	public boolean saveFeedback(int waypointID, int instructorRating, String instructorComment);
	
	public boolean saveInstructorComment(int answersheetID, String instructorComment);
	
	public boolean setFeedbackComplete(int answersheetID, boolean feedbackComplete);
	
	public User getMemberDetails(int userID);
	
	public List<User> getInstructorsDetails(int groupID);
	
	public List<Group> getAllGroups(String username);
	
	public boolean isUsernameExists(String username);
	
	public boolean isEmailExists(String email);

}