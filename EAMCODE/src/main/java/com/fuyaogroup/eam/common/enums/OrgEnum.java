package com.fuyaogroup.eam.common.enums;

import lombok.Getter;


@Getter
public enum OrgEnum implements CodeEnum{
	//TODO 正式环境需要重新获取数据
		SHJC2("上海轿车","300000003746043"),
		FYGROUP("集团管理局","300000006715159"),//测试环境
		FQFFBL("福清浮法玻璃","300000009431661"),
		FQJXZZ("福清机械制造","300000006719334"),
		FYFFJT("福耀浮法集团","300000009431661"),
		FYGJJT("福耀国际集团","300000009813916"),
		FYQBJT("福耀汽玻集团","300000009813935"),
		FYARG("福耀ARG集团","300000006715159"),
		SF("三峰","SF"),
	
		;
		private String code;
		private String message;

		OrgEnum(String code, String message) {
		this.code = code;
		this.message = message;
		}

}
