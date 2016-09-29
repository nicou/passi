package fi.softala.ttl.model;

import java.io.Serializable;

public class WaypointDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int waypointID;
	private int worksheetID;
	private String assignment;
	
	public WaypointDTO() {
		super();
	}
	
	public WaypointDTO(int waypointID, int worksheetID, String assignment) {
		super();
		this.waypointID = waypointID;
		this.worksheetID = worksheetID;
		this.assignment = assignment;
	}

	public int getWaypointID() {
		return waypointID;
	}

	public void setWaypointID(int waypointID) {
		this.waypointID = waypointID;
	}

	public int getWorksheetID() {
		return worksheetID;
	}

	public void setWorksheetID(int worksheetID) {
		this.worksheetID = worksheetID;
	}

	public String getAssignment() {
		return assignment;
	}

	public void setAssignment(String assignment) {
		this.assignment = assignment;
	}
}
