package fi.softala.ttl.model;

import java.io.Serializable;

public class Group implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String groupID;
	private String groupName;
	private Instructor instructor;
	private int numGroupMembers;

	public Group() {
		super();
		this.groupID = "";
		this.groupName = "";
		this.instructor = null;
		this.numGroupMembers = 0;
	}

	public Group(String groupID, String groupName, Instructor instructor, int numGroupMembers) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
		this.instructor = instructor;
		this.numGroupMembers = numGroupMembers;
	}
	
	public void reset() {
		this.groupID = "";
		this.groupName = "";
		this.instructor = null;
		this.numGroupMembers = 0;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public Instructor getLeader() {
		return instructor;
	}
	
	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}
	
	public int getNumGroupMembers() {
		return numGroupMembers;
	}

	public void setNumGroupMembers(int numGroupMembers) {
		this.numGroupMembers = numGroupMembers;
	}
}
