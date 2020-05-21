package com.fuyaogroup.eam.common.enums;

import lombok.Getter;


@Getter
public enum OAOrgEnum implements CodeEnum{
	//TODO 正式环境需要重新获取数据
//		FYGROUP("福耀玻璃工业集团股份有限公司（总办）FYG","集团管理局"),//测试环境
//		FQFFBL("福耀汽玻FYA","福清浮法玻璃"),
//		FQJXZZ("福耀集团(福建)机械制造有限公司","福清机械制造"),
//		FYFFJT("福耀汽玻FYA","福耀浮法集团"),
//		FYGJJT("福耀国际集团","FYGJJT"),
//		FYQBJT("福耀汽玻集团","FYQBJT"),
//		SF("三峰","SF"),
//	
//		;
	    FYGROUP("福耀玻璃工业集团股份有限公司（总办）FYG","集团管理局"),
	    FYARG("福耀ARG","福耀ARG"),
	    FYFFFYF("福耀浮法FYF","福耀浮法FYF"),
	    FYQBFYA("福耀汽玻FYA","福耀汽玻FYA"),
	    FYSF("福耀三锋","福耀三锋"),
	    ;
		private String code;
		private String message;

		OAOrgEnum(String code, String message) {
		this.code = code;
		this.message = message;
		}

}
