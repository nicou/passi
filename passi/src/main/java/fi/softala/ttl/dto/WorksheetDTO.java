package fi.softala.ttl.dto;

import java.io.Serializable;

public class WorksheetDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int worksheetID;
	private String worksheetHeader;

	public WorksheetDTO() {
		super();
		this.worksheetID = 0;
		this.worksheetHeader = "";
	}
	
	public WorksheetDTO(int worksheetID, String worksheetHeader) {
		super();
		this.worksheetID = worksheetID;
		this.worksheetHeader = worksheetHeader;
	}

	public int getWorksheetID() {
		return worksheetID;
	}

	public void setWorksheetID(int worksheetID) {
		this.worksheetID = worksheetID;
	}

	public String getWorksheetHeader() {
		return worksheetHeader;
	}

	public void setWorksheetHeader(String worksheetHeader) {
		this.worksheetHeader = worksheetHeader;
	}

	@Override
	public String toString() {
		return "WorksheetDTO [worksheetID=" + worksheetID + ", worksheetHeader=" + worksheetHeader + "]";
	}
	
}
