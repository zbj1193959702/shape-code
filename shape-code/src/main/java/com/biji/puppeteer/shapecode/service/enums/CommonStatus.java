package com.biji.puppeteer.shapecode.service.enums;

public enum CommonStatus {
	is_delete(0, "无效"),
	no_delete(1, "有效");

	public final String name;
	public final Integer value;

	public int getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	private CommonStatus(int value, String name) {
		this.value = value;
		this.name = name;
	}
}
