package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

@Getter
public enum FaultTypeEnum  implements CodeEnum{
	ELECTRICAL("ELECTRICAL","电气故障 "),
	SOFTWARE("SOFTWARE","软件故障"),
	OTHER("OTHER","其他"),
	MACHINE("MACHINE","机械故障"),
	DEFAULT("OTHER","其他"),
	;
	private String code;
	private String message;

	FaultTypeEnum(String code, String message) {
	this.code = code;
	this.message = message;
	}
}
