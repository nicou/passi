package fi.softala.ttl.model;

public class Role {
	
    private int id;
    private String name;
    
    public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

	public void setName(String name) {
        this.name = name;
    }
}