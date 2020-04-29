package com.fuyaogroup.eam.common.enums;

import lombok.Getter;


@Getter
public enum FactoryEnum implements CodeEnum{
	//TODO 正式环境需要重新获取数据
		SHJC("S1","300000002270290"),
		SHGH1("S2","300000002270313"),
		SHBB("S3","300000002270325"),
		SHGH2("S8","300000002270337"),
		SHJC2("SHJC2","300000003746043"),
	
		;
		private String code;
		private String message;

		FactoryEnum(String code, String message) {
		this.code = code;
		this.message = message;
		}

}
