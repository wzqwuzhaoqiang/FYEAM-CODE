package com.fuyaogroup.eam.modules.fusion.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ASSETPD")
public class AssetSoftware extends BaseEntity{
	private String	OrganizationName;//运营组织名称
	private Long	jobNum;//使用人工号
	private String	userName;//使用人名字
	private String	Description;//说明
	private String	AssetNumber;//资产编号
	private String	assetModel;//型号
	private String	allocation;//基本配置
	private String	manufacturer;//生产厂商/销售厂商
	private String	contractId;//合同号
	private Integer	status;//软件维保状态
	private Integer	softType;//类型 1-单机版 2-网络版
	private Integer	source;//来源
	private Integer	suite;//套数
	private BigDecimal	amount;//金额
	private Date	usingStartTime;//启用时间
	private Date	warrantyprivate ;//授权期限
	private String	ramark;//备注
	private Date	warrantyReminderprivate ;//过保提醒时间

}
