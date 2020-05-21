package com.fuyaogroup.eam.modules.fusion.model;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AssetLifeRecored extends BaseEntity implements Comparator<AssetLifeRecored>{

	private String action; //动作
	private String formNumber;//对应单据编号
	private String assetNumber;//资产编号
	private String assetName;//资产名称
	private String startDate;//创建日期
	private String userName;//使用人
	private String borrower;//借用人
	private String contactInfor;//联系方式
	private String borrowOutDate;//借出时间
	private int borrowUseDate;//借用天数（单位天）
	private String borrowOutman;//出借人
	private String returnIs;//是否归还（归还，续借）
	private String returntwoIs;//归还人
	private String renewDateNumber;//续借天数（天）
	private String thingSituation;//物品归还完整状况
	private String reciver;//接收人
	private String returnDate;//归还日期
	private String faultPhenomenon;//故障显现
	//处理意见
		private String handleOpinions;
		//维修验收
		private String repairCheckAndAccept;
		//维修单据编号
		private String documentNumber;
	private String reason;//报废原因
	private String measures;//  非正常报废的纠正措施
	@Override
	public int compare(AssetLifeRecored o1, AssetLifeRecored o2) {
		// TODO Auto-generated method stubaaaaaaaaaaaaa
		return o1.getStartDate().compareTo(o2.getStartDate());
	}
	
}
