/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.util.List;

import fi.softala.ttl.model.Answersheet;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.User;
import fi.softala.ttl.model.Worksheet;

public interface PassiDAO {

	public boolean addGroup(Group group);
	public boolean delGroup(int groupID);
	public List<Group> getAllGroups();
	public List<User> getGroupMembers(Group group);
	public List<Worksheet> getWorksheets(Group group);
	public List<Answersheet> getAnswers(Group group, User user);
	public void saveFeedback(int waypointID, int instructorRating, String instructorComment);
	
}