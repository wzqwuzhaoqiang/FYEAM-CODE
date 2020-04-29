package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

/**
 * 故障编码
 * 1:电气故障;3:机械故障；2:其他
 *
 */
@Getter
public enum  FaultStatusEnum implements CodeEnum{
	WFDZ("1","无法动作"),
	DZBL("2","动作不良"),
	LJBL("3","连接不良"),
	AZBL("4","安装不良"),
	RHBL("5","润滑不良"),
	DL("6","断裂"),
	TL("7","脱落"),
	PS("8","破损"),
	LH("9","老化"),
	PP("10","跑偏"),
	MS("11","磨损"),
	QGGZ("12","气缸故障"),
	XCPG("13","小车爬轨"),
	LZQGZ("14","联轴器故障"),
	XCDXCGZ("15","小车导向槽故障"),
	CZGZ("16","轴承故障"),
	;
	private String code;
	private String message;

	FaultStatusEnum(String code, String message) {
	this.code = code;
	this.message = message;
	}
	
	}