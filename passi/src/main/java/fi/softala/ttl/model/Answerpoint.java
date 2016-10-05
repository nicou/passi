/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;

public class Answerpoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private int    answerWaypointID;
	private String answerWaypointText;
	private String answerWaypointInstructorComment;
	private String answerWaypointImageURL;
	private int    answerID; // => Answersheet
	private int    waypointID; // => Waypoint
	private int    optionID; // => Selected Option
	
	public Answerpoint() {
		super();
		this.answerWaypointID = 0;
		this.answerWaypointText = "Ei vastattu";
		this.answerWaypointInstructorComment = "Ei palautetta";
		this.answerWaypointImageURL = "URL";
		this.answerID = 0;
		this.waypointID = 0;
		this.optionID = 0;
	}

	public Answerpoint(int answerWaypointID, String answerWaypointText, String answerWaypointInstructorComment,
			String answerWaypointImageURL, int answerID, int waypointID, int optionID) {
		super();
		this.answerWaypointID = answerWaypointID;
		this.answerWaypointText = answerWaypointText;
		this.answerWaypointInstructorComment = answerWaypointInstructorComment;
		this.answerWaypointImageURL = answerWaypointImageURL;
		this.answerID = answerID;
		this.waypointID = waypointID;
		this.optionID = optionID;
	}

	public int getAnswerWaypointID() {
		return answerWaypointID;
	}

	public void setAnswerWaypointID(int answerWaypointID) {
		this.answerWaypointID = answerWaypointID;
	}

	public String getAnswerWaypointText() {
		return answerWaypointText;
	}

	public void setAnswerWaypointText(String answerWaypointText) {
		this.answerWaypointText = answerWaypointText;
	}

	public String getAnswerWaypointInstructorComment() {
		return answerWaypointInstructorComment;
	}

	public void setAnswerWaypointInstructorComment(String answerWaypointInstructorComment) {
		this.answerWaypointInstructorComment = answerWaypointInstructorComment;
	}

	public String getAnswerWaypointImageURL() {
		return answerWaypointImageURL;
	}

	public void setAnswerWaypointImageURL(String answerWaypointImageURL) {
		this.answerWaypointImageURL = answerWaypointImageURL;
	}

	public int getAnswerID() {
		return answerID;
	}

	public void setAnswerID(int answerID) {
		this.answerID = answerID;
	}

	public int getWaypointID() {
		return waypointID;
	}

	public void setWaypointID(int waypointID) {
		this.waypointID = waypointID;
	}

	public int getOptionID() {
		return optionID;
	}

	public void setOptionID(int optionID) {
		this.optionID = optionID;
	}
}
