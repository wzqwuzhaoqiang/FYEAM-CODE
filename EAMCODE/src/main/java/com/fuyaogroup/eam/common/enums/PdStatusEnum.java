package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

@Getter
public enum PdStatusEnum implements CodeEnum{
	 ASSET_PD_WAITING(0,"未盘点"),
		ASSET_PD_FINISH(1,"盘点完成"),
		ASSET_PD_FAIL(2,"盘点失败"),

	;
	private Integer code;
	private String message;

	PdStatusEnum(Integer code, String message) {
	this.code = code;
	this.message = message;
	}
}
