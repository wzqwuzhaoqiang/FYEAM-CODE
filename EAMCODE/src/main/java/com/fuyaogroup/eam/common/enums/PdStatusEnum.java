package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

@Getter
public enum PdStatusEnum implements CodeEnum{
	 ASSET_PD_WAITING(0,"未盘点"),
		ASSET_PD_FINISH(1,"盘点完成"),
		ASSET_PD_FAIL(2,"盘点失败"),
		ASSET_PD_FAIL_NOCODE(3,"资产无码"),
		ASSET_PD_FAIL_TRANSFERASSERT(4,"转移"),
		ASSET_PD_FAIL_PANKUI(5,"盘亏"),
		
		
	;
	private Integer code;
	private String message;

	PdStatusEnum(Integer code, String message) {
	this.code = code;
	this.message = message;
	}
}
