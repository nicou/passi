/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AnswerWorksheetDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int answerID;
	private int worksheetID;
	private String username;
	private String planningText;
	private ArrayList<AnswerWaypointDTO> waypoints;
	private String instructorComment;
	
	public AnswerWorksheetDTO() {
		super();
		this.answerID = 0; // new answer
		this.worksheetID = 0;
		this.username = "";
		this.planningText = "";
		this.waypoints = null;
		this.instructorComment = "";
	}
	
	public AnswerWorksheetDTO(int answerID, int worksheetID, String username, String planningText,
			ArrayList<AnswerWaypointDTO> waypoints, String instructorComment) {
		super();
		this.answerID = answerID;
		this.worksheetID = worksheetID;
		this.username = username;
		this.planningText = planningText;
		this.waypoints = waypoints;
		this.instructorComment = instructorComment;
	}

	public int getAnswerID() {
		return answerID;
	}

	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}

	public int getWorksheetID() {
		return worksheetID;
	}

	public void setWorksheetID(int worksheetID) {
		this.worksheetID = worksheetID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlanningText() {
		return planningText;
	}

	public void setPlanningText(String planningText) {
		this.planningText = planningText;
	}

	public ArrayList<AnswerWaypointDTO> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(ArrayList<AnswerWaypointDTO> waypoints) {
		this.waypoints = waypoints;
	}

	public String getInstructorComment() {
		return instructorComment;
	}

	public void setInstructorComment(String instructorComment) {
		this.instructorComment = instructorComment;
	}
}
