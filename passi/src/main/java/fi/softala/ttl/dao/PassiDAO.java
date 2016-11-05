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
import fi.softala.ttl.model.Category;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.User;
import fi.softala.ttl.model.Worksheet;

public interface PassiDAO {

	public boolean addGroup(Group group);

	public boolean delGroup(int groupID);

	public List<User> getGroupMembers(int groupID);

	public List<Category> getCategories();

	public List<GroupDTO> getGroupsDTO();

	public List<CategoryDTO> getCategoriesDTO();
	
	public List<WorksheetDTO> getWorksheetsDTO(int groupID, int categoryID);
	
	public List<Answersheet> getWorksheetAnswers(int worksheetID, int userID);

	public List<Worksheet> getWorksheetContent(int worksheetID);
	
	public HashMap<Integer, Integer> getIsAnsweredMap(int worksheetID, ArrayList<User> groupMembers);

	public void saveFeedback(int waypointID, int instructorRating, String instructorComment);
	
	public User getMemberDetails(int userID);

}