package fi.softala.ttl.service;

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

public interface PassiService {
	
	public ArrayList<CategoryDTO> getCategoriesDTO();
	
	public ArrayList<GroupDTO> getGroupsDTO();
	
	public ArrayList<WorksheetDTO> getWorksheetsDTO(int groupID, int categoryID);

	public Worksheet getWorksheetContent(int worksheetID);
	
	public Answersheet getWorksheetAnswers(int worksheetID, int userID);
	
	public ArrayList<User> getGroupMembers(int groupID);
	
	public HashMap<Integer, Integer> getIsAnsweredMap(int worksheetID, ArrayList<User> groupMembers);
	
	public List<Category> getCategories();
		
	public void saveFeadback(int answerWaypointID, int instructorRating, String instructorComment);
	
	public User getMemberDetails(int userID);
	
	public ArrayList<User> getInstructorsDetails(int groupID);
	
	public ArrayList<Group> getAllGroups();

}
