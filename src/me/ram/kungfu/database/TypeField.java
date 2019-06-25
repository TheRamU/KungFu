package me.ram.kungfu.database;

public class TypeField {

	private String field;
	private String type;
	
	public TypeField(String field, String type) {
		this.field = field;
		this.type = type;
	}
	
	public String getField() {
		return field;
	}
	
	public String getType() {
		return type;
	}
}
