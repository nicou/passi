/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Answersheet implements Serializable {

	private static final long serialVersionUID = 1L;

	private int answerID;
	private String answerPlanning;
	private String answerInstructorComment;
	private Timestamp answerTimestamp;
	private int worksheetID;
	private int groupID;
	private int userID;
	private ArrayList<Answerpoint> waypoints;

	public Answersheet() {
		super();
		this.answerID = 0;
		this.answerPlanning = "";
		this.answerInstructorComment = "";
		this.answerTimestamp = null;
		this.worksheetID = 0;
		this.groupID = 0;
		this.userID = 0;
		this.waypoints = null;
	}

	public Answersheet(int answerID, String answerPlanning, String answerInstructorComment, Timestamp answerTimestamp,
			int worksheetID, int groupID, int userID, ArrayList<Answerpoint> waypoints) {
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

	public ArrayList<Answerpoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(ArrayList<Answerpoint> waypoints) {
		this.waypoints = waypoints;
	}

	@Override
	public String toString() {
		return "Answersheet [answerID=" + answerID + ", answerPlanning=" + answerPlanning + ", answerInstructorComment="
				+ answerInstructorComment + ", answerTimestamp=" + answerTimestamp + ", worksheetID=" + worksheetID
				+ ", groupID=" + groupID + ", userID=" + userID + ", waypoints=" + waypoints + "]";
	}
}
