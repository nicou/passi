/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.util.List;

import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Student;

public interface PassiDAO {
	
	public void addGroup(Group group);
	public List<Group> getGroups();
	public List<Student> getGroupStudents(int groupID);
	public String getRole(String username);
}