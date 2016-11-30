package fi.softala.ttl.service;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fi.softala.ttl.dao.PassiDAO;
import fi.softala.ttl.dto.GroupDTO;
import fi.softala.ttl.dto.WorksheetDTO;
import fi.softala.ttl.dto.CategoryDTO;
import fi.softala.ttl.model.Answersheet;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.User;
import fi.softala.ttl.model.Worksheet;

@Service("passiService")
@Transactional
public class PassiServiceImpl implements PassiService {

	private ArrayList<Answersheet> assistAnswersheet = new ArrayList<>();
	private ArrayList<Worksheet> assistWorksheet = new ArrayList<>();

	@Inject
	private PassiDAO dao;

	public PassiDAO getDao() {
		return dao;
	}

	public void setDao(PassiDAO dao) {
		this.dao = dao;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveUser(User user) {
		dao.saveUser(user);
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
		assistWorksheet = (ArrayList<Worksheet>) dao.getWorksheetContent(worksheetID);
		if (!assistWorksheet.isEmpty()) {
			return assistWorksheet.get(0);
		} else {
			return new Worksheet();
		}
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
	@Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void saveFeadback(int answerWaypointID, int instructorRating, String instructorComment) {
		dao.saveFeedback(answerWaypointID, instructorRating, instructorComment);
	}

	@Override
	public Answersheet getWorksheetAnswers(int worksheetID, int userID) {
		assistAnswersheet = (ArrayList<Answersheet>) dao.getWorksheetAnswers(worksheetID, userID);
		if (!assistAnswersheet.isEmpty()) {
			return assistAnswersheet.get(0);
		} else {
			return new Answersheet();
		}
	}

	@Override
	public User getMemberDetails(int userID) {
		return dao.getMemberDetails(userID);
	}

	@Override
	public ArrayList<User> getInstructorsDetails(int groupID) {
		return (ArrayList<User>) dao.getInstructorsDetails(groupID);
	}

	@Override
	public ArrayList<Group> getAllGroups() {
		return (ArrayList<Group>) dao.getAllGroups();
	}

	@Override
	public boolean isUsernameExists(String username) {
		return dao.isUsernameExists(username);
	}

	@Override
	public boolean isEmailExists(String email) {
		return dao.isEmailExists(email);
	}
}
