package com.fuyaogroup.eam.modules.fusion.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("asset_repair_trackrecord")
public class RepairRecord extends BaseEntity {

	//oa表单ID
	private String oaId;
	//公司
	private String company;
	//单据编号
	private String documentNumber;
	//资产名称
	private String assetName;
	//资产编号
	private String assetNumber;
	//联系电话
	private String connectPhone;
	//故障显现
	private String faultPhenomenon;
	//型号
	private String modelNumber;
	//序列号
	private String serialNumber;
	//配置
	private String configure;
	//处理意见
	private String handleOpinions;
	//维修验收
	private String repairCheckAndAccept;
	//状态  1过保 2未过保产生费用 3 未过保不产生费用
	private String status;
	//履历表单创建日期
	private Date createDate;
	//购买日期
	private Date purchaseDate;
	
	
}
