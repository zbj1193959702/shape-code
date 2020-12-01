package com.biji.puppeteer.service.enums;

public enum RegionType {
	country(0, "国"),
	province(1, "省"),
	city(2, "市"),
	district(3, "区")
	;


	public final Integer value;
	public final String name;

	public int getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	private RegionType(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
