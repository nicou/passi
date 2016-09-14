package fi.softala.ttl.model;

import java.io.Serializable;

public class Group implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int groupID;
	private String groupAbbr;
	private String groupName;
	private int groupTeacherID;

	public Group() {
		super();
		this.groupID = 0;
		this.groupAbbr = "";
		this.groupName = "";
		this.groupTeacherID = 0;
	}

	public Group(int groupID, String groupAbbr, String groupName, int groupHead) {
		super();
		this.groupID = groupID;
		this.groupAbbr = groupAbbr;
		this.groupName = groupName;
		this.groupTeacherID = groupHead;
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

	public int getGroupHeadID() {
		return groupTeacherID;
	}

	public void setGroupHead(int groupHeadID) {
		this.groupTeacherID = groupHeadID;
	}
}
