package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

@Getter
public enum ComputerTypeEnum implements CodeEnum{
	//1:笔记本;2:移动工作站;3:台式工作站;4:台式机;5:其他;
	NOTEBOOK(0,"笔记本"),
	TS(1,"笔记本"),
	WORKSTATION(2,"移动工作站"),
	COMPUTER_WORKSTATION(3,"台式工作站"),
	COMPUTER(4,"台式机"),
	ZZJ(5,"组装机"),
	HBDYJ(6,"黑白激光打印机"),
	CSDYJ(7,"彩色激光打印机"),
	ZSDYJ(8,"针式打印机"),
	DGNYTJ(9,"多功能一体机"),
	TMDYJ(10,"条码打印机"),
	SMY(11,"扫描仪"),
	HTY(12,"绘图仪"),
	TYY(13,"投影仪"),
	FYJ(14,"复印机"),
	FWQ(15,"服务器"),
	CC(16,"存储"),
	SCZD(17,"手持终端"),
	WXZD(18,"无线终端"),
	JHJ(19,"交换机"),
	LYQ(20,"路由器"),
	SMXJ(21,"数码相机"),
	PMDYJ(22,"喷墨打印机"),
	HYDH(23,"会议电话"),
	PZZDJ(24,"凭证装订机"),
	WXAP(25,"无线AP"),
	XSQ(26,"显示器"),
	YXSMQ(27,"有线扫描枪"),
	WXSMQ(28,"无线扫描枪"),
	YTJ(29,"一体机")

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
