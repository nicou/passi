/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dao;

import java.util.List;

import fi.softala.ttl.model.AnswerWorksheet;
import fi.softala.ttl.model.Group;
import fi.softala.ttl.model.Member;
import fi.softala.ttl.model.Worksheet;

public interface PassiDAO {

	public List<Group> getAllGroups();
	public List<Member> getGroupMembers(Group group);
	public List<Worksheet> getWorksheets(Group group);
	public List<AnswerWorksheet> getAnswers(Group group, Member member);
}