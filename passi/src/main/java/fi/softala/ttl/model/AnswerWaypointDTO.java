/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;

public class AnswerWaypointDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int answerWaypointID;
	private int answerID; // => AnswerWorksheetDTO
	private int waypointID;
	private int selectedOptionID; // => AnswerOption
	private String imageURL;
	private String answerText;
	private String instructorComment;

	public AnswerWaypointDTO() {
		super();
		this.answerWaypointID = 0;
		this.answerID = 0;
		this.waypointID = 0;
		this.selectedOptionID = 0;
		this.imageURL = "";
		this.answerText = "";
		this.instructorComment = "";
	}

	public AnswerWaypointDTO(int answerWaypointID, int answerID, int waypointID, int selectedOptionID, String imageURL,
			String answerText, String instructorComment) {
		super();
		this.answerWaypointID = answerWaypointID;
		this.answerID = answerID;
		this.waypointID = waypointID;
		this.selectedOptionID = selectedOptionID;
		this.imageURL = imageURL;
		this.answerText = answerText;
		this.instructorComment = instructorComment;
	}

	public int getAnswerWaypointID() {
		return answerWaypointID;
	}

	public void setAnswerWaypointID(int answerWaypointID) {
		this.answerWaypointID = answerWaypointID;
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
	
	public int getSelectedOptionID() {
		return selectedOptionID;
	}

	public void setSelectedOptionID(int selectedOptionID) {
		this.selectedOptionID = selectedOptionID;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	public String getAnswerText() {
		return answerText;
	}
	
	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public String getInstructorComment() {
		return instructorComment;
	}

	public void setInstructorComment(String instructorComment) {
		this.instructorComment = instructorComment;
	}
}
