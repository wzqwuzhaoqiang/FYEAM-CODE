package com.fuyaogroup.eam.common.enums;

import lombok.Getter;

@Getter
public enum ToolEnum implements CodeEnum{
	R001T("001","10'活动扳手"),
	R002T("002","内六角"),
	R003T("003","卡簧钳"),
	R004T("004","4'内六角"),
	R005T("005","螺丝刀 "),
	R006T("006","锤子"),
	R007T("007","10'扳手"),
	R008T("008","扭力扳手"),
	R009T("009","10'呆扳手"),
	R010T("010","扳手"),
	R011T("011","活动扳手"),
	R012T("012","万用表"),
	R013T("013","螺丝刀"),
	R014T("014","钳子"),
	R015T("015","套筒组套"),
	R016T("016","黄油枪"),
	R017T("017","呆扳手"),
	R018T("018","漏斗"),
	R019T("019","直角尺"),
	R020T("020","钢板尺"),
	R021T("021","水平仪"),
	R022T("022","拉马"),
	R023T("023","塞尺"),
	R024T("024","百分表"),
	R025T("025","清洗纸"),
	R026T("026","电笔"),
	R027T("027","油壶"),
	R028T("028","套筒扳手"),
	R029T("029","起子"),
	R030T("030","尖嘴钳"),
	R031T("031","卡环钳"),
	R032T("032","压线钳"),
	R033T("033","纱布"),
	R034T("034","油枪"),
	R035T("035","卷尺"),
	R036T("036","测温枪"),
	R037T("037","毛刷"),
	R038T("038","吸尘器"),
	R039T("039","专用量具"),
	R040T("040","三爪捋子"),
	R041T("041","老虎钳")

	;
	private String code;
	private String message;

	ToolEnum(String code, String message) {
	this.code = code;
	this.message = message;
	}

	public static ToolEnum getByMessage(String message){
		for(ToolEnum  e:ToolEnum.values()){
			if(message.equals(e.getMessage())){
				return e;
			}
		}
		return null;
	}
	

}
