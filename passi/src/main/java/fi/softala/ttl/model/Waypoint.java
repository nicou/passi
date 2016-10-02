/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.model;

import java.io.Serializable;

public class Waypoint implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int     waypointID;
	private String  waypointTask;
	private boolean waypointPhotoEnabled;
	private int     worksheetID;

	public Waypoint() {
		super();
	}

	public Waypoint(int waypointID, String waypointTask, boolean waypointPhotoEnabled, int worksheetID) {
		super();
		this.waypointID = waypointID;
		this.waypointTask = waypointTask;
		this.waypointPhotoEnabled = waypointPhotoEnabled;
		this.worksheetID = worksheetID;
	}

	public int getWaypointID() {
		return waypointID;
	}

	public void setWaypointID(int waypointID) {
		this.waypointID = waypointID;
	}

	public String getWaypointTask() {
		return waypointTask;
	}

	public void setWaypointTask(String waypointTask) {
		this.waypointTask = waypointTask;
	}

	public boolean isWaypointPhotoEnabled() {
		return waypointPhotoEnabled;
	}

	public void setWaypointPhotoEnabled(boolean waypointPhotoEnabled) {
		this.waypointPhotoEnabled = waypointPhotoEnabled;
	}

	public int getWorksheetID() {
		return worksheetID;
	}

	public void setWorksheetID(int worksheetID) {
		this.worksheetID = worksheetID;
	}
}
