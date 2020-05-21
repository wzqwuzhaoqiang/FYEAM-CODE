package com.fuyaogroup.eam.modules.fusion.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ASSET_SCRAP_TRACKRECORD")
public class AssetScrap extends BaseEntity {
	
	private String oaId;   //OA表单ID
	private String company;  //公司
	private String usedepartment;   //使用部门
	private String createDate;  //创建日期
	private String formNumber;  //单据编号
	private String asserType;//资产类型（基础设施，房屋建筑物，办公设备，机器设备，运输设备，电子设备，其他设备，其他长期资产）
	private String departmentHead;//  部门主管
	private String assetNumber;//  资产编号
	private String assetName;//  资产名称
	private String model;//  规格型号
	private String startDate;//  启用日期
	private String supplusLife;//  剩余年限
	private String bondedAttribute;//  保税属性
	private int count;//数量
	private BigDecimal assetNatValue;//  资产净值
	private String reason;//  报废原因
	private String handingSuggestions;//  处理建议
	private BigDecimal sumMoney;//  合计金额
	private String measures;//  非正常报废的纠正措施
	

}
