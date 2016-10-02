package fi.softala.ttl.model;

import java.io.Serializable;

public class Organization implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int    organizationID;
	private String organizationName;
	
	public Organization() {
		super();
	}

	public Organization(int organizationID, String organizationName) {
		super();
		this.organizationID = organizationID;
		this.organizationName = organizationName;
	}

	public int getOrganizationID() {
		return organizationID;
	}

	public void setOrganizationID(int organizationID) {
		this.organizationID = organizationID;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
}
