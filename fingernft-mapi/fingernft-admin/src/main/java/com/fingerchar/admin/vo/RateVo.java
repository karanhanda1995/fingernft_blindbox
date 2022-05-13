package com.fingerchar.admin.vo;

public class RateVo {

	private String name;
	
	private String value;
	
	public RateVo(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "RateVo { name="
				+ name + ", value=" + value + "}";
	}
}
