package org.duyi.rotator.logic;

/**
 * @Author Yi Du <duyi001@gmail.com>
 * @Date 2011-2-23
 * @Description: This is a class of Choose/Choice.
 */
public class Choose {
	public static final String CHOOSE_NAME = "name";
	public static final String CHOOSE_TYPE = "type";
	public static final String CHOOSE_LOCATION = "location";
	public static final String CHOOSE_ADDRESS = "address";
	private int type;
	private String name;
	private String location;
	private String address;
	
	public Choose(){
		name = "no";
		location = "no";
		address = "no";
		type = -1;
	}
	public Choose(String name) {
		this.name = name;
		location = "no";
		address = "no";
		type = -1;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	
}
