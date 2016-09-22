/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.util.List;

import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Student;

public interface PassiDAO {
	
	public boolean addGroup(Group group);
	public boolean addStudent(Student student, String groupID);
	public boolean deleteGroup(String groupID);
	public boolean deleteStudent(String studentID);
	public boolean editGroup(String groupID, String groupName);
	public List<Group> getGroups();
	public List<Student> getGroupStudents(String groupID);
}