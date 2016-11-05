/**
 * @author Mika Ropponen
 */
package fi.softala.ttl.dto;

import java.io.Serializable;

public class GroupDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int groupID;
	private String groupName;

	public GroupDTO() {
		super();
		this.groupID = 0;
		this.groupName = "";
	}
	
	public GroupDTO(int groupID, String groupName) {
		super();
		this.groupID = groupID;
		this.groupName = groupName;
	}
	
	public void reset() {
		this.groupID = 0;
		this.groupName = "";
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
