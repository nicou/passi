package fi.softala.ttl.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import fi.softala.ttl.dao.PassiDAO;
import fi.softala.ttl.dto.GroupDTO;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.dto.CategoryDTO;
import fi.softala.ttl.model.Answersheet;
import fi.softala.ttl.model.Category;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.User;
import fi.softala.ttl.model.Worksheet;

@Service("passiService")
public class PassiServiceImpl implements PassiService {

	@Inject
	private PassiDAO dao;

	public PassiDAO getDao() {
		return dao;
	}

	public void setDao(PassiDAO dao) {
		this.dao = dao;
	}

	@Override
	public List<Category> getCategories() {
		return dao.getCategories();
	}
	
	@Override
	public ArrayList<Group> getGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GroupDTO> getGroupsDTO() {
		return (ArrayList<GroupDTO>) dao.getGroupsDTO();
	}
	
	@Override
	public ArrayList<CategoryDTO> getCategoriesDTO() {
		return (ArrayList<CategoryDTO>) dao.getCategoriesDTO();
	}
	
	@Override
	public ArrayList<WorksheetDTO> getWorksheetsDTO(int groupID, int categoryID) {
		return (ArrayList<WorksheetDTO>) dao.getWorksheetsDTO(groupID, categoryID);
	}
	
	@Override
	public Worksheet getWorksheetContent(int worksheetID) {
		return dao.getWorksheetContent(worksheetID).get(0); // get first index
	}

	@Override
	public ArrayList<User> getGroupMembers(int groupID) {
		return (ArrayList<User>) dao.getGroupMembers(groupID);
	}
	
	@Override
	public HashMap<Integer, Integer> getIsAnsweredMap(int worksheetID, ArrayList<User> groupMembers) {
		return dao.getIsAnsweredMap(worksheetID, groupMembers);
	}
	
	@Override
	public void saveFeadback(int waypointID, int instructorRating, String instructorComment) {
		dao.saveFeedback(waypointID, instructorRating, instructorComment);	
	}

	@Override
	public Answersheet getWorksheetAnswers(int worksheetID, int userID) {
		return dao.getWorksheetAnswers(worksheetID, userID).get(0);
	}
}
