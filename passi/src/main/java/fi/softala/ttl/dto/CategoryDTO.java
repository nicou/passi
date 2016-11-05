package fi.softala.ttl.dto;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int categoryID;
	private String categoryName;
	
	public CategoryDTO() {
		super();
		categoryID = 0;
		categoryName = "";
	}

	public CategoryDTO(int categoryID, String categoryName) {
		super();
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}
	
	public void reset() {
		categoryID = 0;
		categoryName = "";
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return "CategoryDTO [categoryID=" + categoryID + ", categoryName=" + categoryName + "]";
	}
}
