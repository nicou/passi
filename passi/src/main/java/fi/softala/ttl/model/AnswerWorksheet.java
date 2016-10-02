/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class AnswerWorksheet implements Serializable {

	private static final long serialVersionUID = 1L;

	private int    answerID;
	private String answerPlanning;
	private String answerInstructorComment;
	private Timestamp answerTimestamp;
	private int    worksheetID; // => Worksheet
	private int    groupID; // => Group
	private int    userID; // => Member
	
	// List of waypoint answers
	private List<AnswerWaypoint> waypoints;

	public AnswerWorksheet() {
		super();
	}

	public AnswerWorksheet(int answerID, String answerPlanning, String answerInstructorComment, Timestamp answerTimestamp,
			int worksheetID, int groupID, int userID, List<AnswerWaypoint> waypoints) {
		super();
		this.answerID = answerID;
		this.answerPlanning = answerPlanning;
		this.answerInstructorComment = answerInstructorComment;
		this.answerTimestamp = answerTimestamp;
		this.worksheetID = worksheetID;
		this.groupID = groupID;
		this.userID = userID;
		this.waypoints = waypoints;
	}

	public int getAnswerID() {
		return answerID;
	}

	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}

	public String getAnswerPlanning() {
		return answerPlanning;
	}

	public void setAnswerPlanning(String answerPlanning) {
		this.answerPlanning = answerPlanning;
	}

	public String getAnswerInstructorComment() {
		return answerInstructorComment;
	}

	public void setAnswerInstructorComment(String answerInstructorComment) {
		this.answerInstructorComment = answerInstructorComment;
	}

	public Timestamp getAnswerTimestamp() {
		return answerTimestamp;
	}

	public void setAnswerTimestamp(Timestamp answerTimestamp) {
		this.answerTimestamp = answerTimestamp;
	}

	public int getWorksheetID() {
		return worksheetID;
	}

	public void setWorksheetID(int worksheetID) {
		this.worksheetID = worksheetID;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public List<AnswerWaypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<AnswerWaypoint> waypoints) {
		this.waypoints = waypoints;
	}
}
