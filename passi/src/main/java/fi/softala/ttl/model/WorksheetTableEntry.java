package fi.softala.ttl.model;

import java.io.Serializable;

public class WorksheetTableEntry implements Serializable {

	private static final long serialVersionUID = 1L;

	String category;
	String worksheetHeader;
	int turnedInCount;
	int noFeedbackCount;

	public WorksheetTableEntry() {
		super();
	}

	public WorksheetTableEntry(String category, String worksheetHeader, int turnedInCount, int noFeedbackCount) {
		super();
		this.category = category;
		this.worksheetHeader = worksheetHeader;
		this.turnedInCount = turnedInCount;
		this.noFeedbackCount = noFeedbackCount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWorksheetHeader() {
		return worksheetHeader;
	}

	public void setWorksheetHeader(String worksheetHeader) {
		this.worksheetHeader = worksheetHeader;
	}

	public int getTurnedInCount() {
		return turnedInCount;
	}

	public void setTurnedInCount(int turnedInCount) {
		this.turnedInCount = turnedInCount;
	}

	public int getNoFeedbackCount() {
		return noFeedbackCount;
	}

	public void setNoFeedbackCount(int noFeedbackCount) {
		this.noFeedbackCount = noFeedbackCount;
	}

	@Override
	public String toString() {
		return "WorksheetTableEntry [category=" + category + ", worksheetHeader=" + worksheetHeader + ", turnedInCount="
				+ turnedInCount + ", noFeedbackCount=" + noFeedbackCount + "]";
	}

}
