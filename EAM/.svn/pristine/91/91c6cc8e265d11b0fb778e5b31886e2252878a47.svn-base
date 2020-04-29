package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

@Getter
public enum ComputerTypeEnum implements CodeEnum{
	//1:笔记本;2:移动工作站;3:台式工作站;4:台式机;5:其他;
	NOTEBOOK(1,"笔记本"),
	WORKSTATION(2,"移动工作站"),
	COMPUTER_WORKSTATION(3,"台式工作站"),
	COMPUTER(4,"台式机"),
	OHTER(5,"其他"),
	

	;
	private Integer code;
	private String message;

	ComputerTypeEnum(Integer code, String message) {
	this.code = code;
	this.message = message;
	}
	
	public Integer getByMsg( String message){
		 for (ComputerTypeEnum each: ComputerTypeEnum.values()) { 
			    //利用message进行循环比较，获取对应的枚举
			    if (message.equals(each.getMessage())) {
			    return each.getCode();
			    }
			}
			return null;
		
	}
	
	public String getByCode(Integer code){
		 for (ComputerTypeEnum each: ComputerTypeEnum.values()) { 
			    //利用message进行循环比较，获取对应的枚举
			    if (code.equals(each.getCode())) {
			    return each.getMessage();
			    }
			}
			return null;
		
	}
	
}
