package fi.softala.ttl.model;

import java.io.Serializable;

public class Group implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int groupID;
	private String groupAbbr;
	private String groupName;
	private int groupLeadID;
	private String groupLeadName;
	private int numGroupMembers;

	public Group() {
		super();
		this.groupID = 0;
		this.groupAbbr = "";
		this.groupName = "";
		this.groupLeadID = 0;
		this.groupLeadName = "";
		this.numGroupMembers = 0;
	}

	public Group(int groupID, String groupAbbr, String groupName, int groupHead, int groupLeadID, String groupLeadName) {
		super();
		this.groupID = groupID;
		this.groupAbbr = groupAbbr;
		this.groupName = groupName;
		this.groupLeadID = groupLeadID;
		this.groupLeadName = groupLeadName;
	}
	
	public void reset() {
		this.groupID = 0;
		this.groupAbbr = "";
		this.groupName = "";
		this.groupLeadID = 0;
		this.groupLeadName = "";
		this.numGroupMembers = 0;
	}

	public int getNumGroupMembers() {
		return numGroupMembers;
	}

	public void setNumGroupMembers(int numGroupMembers) {
		this.numGroupMembers = numGroupMembers;
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getGroupAbbr() {
		return groupAbbr;
	}

	public void setGroupAbbr(String groupAbbr) {
		this.groupAbbr = groupAbbr;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getGroupLeadID() {
		return groupLeadID;
	}

	public void setGroupLeadID(int groupLeadID) {
		this.groupLeadID = groupLeadID;
	}
	
	public String getGroupLeadName() {
		return groupLeadName;
	}

	public void setGroupLeadName(String groupLeadName) {
		this.groupLeadName = groupLeadName;
	}
}
