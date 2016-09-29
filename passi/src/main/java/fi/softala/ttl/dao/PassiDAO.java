/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.util.List;

import fi.softala.ttl.model.AnswerWorksheetDTO;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Student;
import fi.softala.ttl.model.WorksheetDTO;

public interface PassiDAO {

	public List<Group> getGroups();
	public List<Student> getGroupStudents(Group group);
	public List<WorksheetDTO> getWorksheets(String groupID);
	public List<AnswerWorksheetDTO> getAnswers(String groupID, String username);
	
	/*
	public boolean addGroup(Group group);
	public boolean addStudent(Student student, String groupID);
	public boolean deleteGroup(String groupID);
	public boolean deleteStudent(String studentID);
	public boolean editGroup(String groupID, String groupName);
	*/
}