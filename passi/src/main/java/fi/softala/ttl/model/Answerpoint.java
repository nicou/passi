package fi.softala.ttl.model;

import java.io.Serializable;

public class Answerpoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private int answerpointID;
	private String answer;
	private String instructorComment;
	private String imageUrl;
	private int answersheetID;
	private int waypointID;
	private int optionID;
	
	public Answerpoint() {
		super();
		this.answerpointID = 0;
		this.answer = "Ei vastausta";
		this.instructorComment = "Ei palautetta";
		this.imageUrl = "URL";
		this.answersheetID = 0;
		this.waypointID = 0;
		this.optionID = 0;
	}

	public Answerpoint(int answerpointID, String answer, String instructorComment, String imageUrl, int answersheetID,
			int waypointID, int optionID) {
		super();
		this.answerpointID = answerpointID;
		this.answer = answer;
		this.instructorComment = instructorComment;
		this.imageUrl = imageUrl;
		this.answersheetID = answersheetID;
		this.waypointID = waypointID;
		this.optionID = optionID;
	}

	public int getAnswerpointID() {
		return answerpointID;
	}

	public void setAnswerpointID(int answerpointID) {
		this.answerpointID = answerpointID;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getInstructorComment() {
		return instructorComment;
	}

	public void setInstructorComment(String instructorComment) {
		this.instructorComment = instructorComment;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getAnswersheetID() {
		return answersheetID;
	}

	public void setAnswersheetID(int answersheetID) {
		this.answersheetID = answersheetID;
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
