/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Worksheet implements Serializable{

	private static final long serialVersionUID = 1L;

	private int    worksheetID;
	private String worksheetHeader;
	private String worksheetPreface;
	private String worksheetPlanning;
	private List<Waypoint> waypoints;

	public Worksheet() {
		super();
		this.worksheetID = 0;
		this.worksheetHeader = "";
		this.worksheetPreface = "";
		this.worksheetPlanning = "";
		this.waypoints = new ArrayList<Waypoint>();
	}

	public Worksheet(int worksheetID, String worksheetHeader, String worksheetPreface, String worksheetPlanning,
			List<Waypoint> waypoints) {
		super();
		this.worksheetID = worksheetID;
		this.worksheetHeader = worksheetHeader;
		this.worksheetPreface = worksheetPreface;
		this.worksheetPlanning = worksheetPlanning;
		this.waypoints = waypoints;
	}
	
	public void reset() {
		this.worksheetID = 0;
		this.worksheetHeader = "";
		this.worksheetPreface = "";
		this.worksheetPlanning = "";
		this.waypoints.clear();
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

	public String getWorksheetPreface() {
		return worksheetPreface;
	}

	public void setWorksheetPreface(String worksheetPreface) {
		this.worksheetPreface = worksheetPreface;
	}

	public String getWorksheetPlanning() {
		return worksheetPlanning;
	}

	public void setWorksheetPlanning(String worksheetPlanning) {
		this.worksheetPlanning = worksheetPlanning;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}
}
