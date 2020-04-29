package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

@Getter
public enum CenterEnum implements CodeEnum{
	SHJCED("S1","S1ED"),
	SHGH1ED("S2","S2ED"),
	SHBBED("S3","S3ED"),
	SHGH2ED("S8","S8ED"),

	;
	private String code;
	private String message;

	CenterEnum(String code, String message) {
	this.code = code;
	this.message = message;
	}
}
