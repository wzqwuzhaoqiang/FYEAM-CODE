package com.fuyaogroup.eam.modules.fusion.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("asset_borrow_trackrecord")
public class AssetBorrow extends BaseEntity {

	//oa表单ID
	private String odId;
	//公司
	private String company;
	//日期，借用日期
	private Date formDate;
	//借用人
	private String borrower;
	//部门
	private String department;
	//联系方式
	private String contactInfor;
	//借用物品
	private String borrowThing;
	//数量
	private int number;
	//用途
	private String purpose;
	//资产编号
	private String assertNumber;
	//资产名称
	private String assertName;
	//规格/型号
	private String model;
	//序列号
	private String serialNumber;
	//配置信息
	private String configInfo;
	//启用日期
	private Date startDate;
	//借出时间
	private Date borrowOutDate;
	private Date borrowUseDate; //借用时间（单位天）
	private String borrowOutman;//  出借人
	private String returnIs;//  是否归还（归还，续借）
	private String returntwoIs;//  是否归还;
	private int renewDateNumber;//  续借天数（天）;
	private String thingSituation;//  物品归还完整状况;
	private String reciver;//  接收人;
	
	private Date returnDate;//  归还日期;
	
	
}
