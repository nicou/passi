package fi.softala.ttl.model;


import java.io.Serializable;
import java.util.List;

public class Option implements Serializable {

	private static final long serialVersionUID = 1L;

	private int optionID;
	private String text;
	private int waypointID;
	private List<Waypoint> waypoints;	// is this needed?
	
	public Option() {
		super();
		this.optionID = 0;
		this.text = "";
		this.waypointID = 0;
		this.waypoints = null;
		
	}

	public Option(int optionID, String text, int waypointID, List<Waypoint> waypoints) {
		super();
		this.optionID = optionID;
		this.text = text;
		this.waypointID = waypointID;
		this.waypoints = waypoints;
	}

	public int getOptionID() {
		return optionID;
	}

	public void setOptionID(int optionID) {
		this.optionID = optionID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getWaypointID() {
		return waypointID;
	}

	public void setWaypointID(int waypointID) {
		this.waypointID = waypointID;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}
	
}
