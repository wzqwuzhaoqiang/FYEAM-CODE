package com.fuyaogroup.eam.modules.fusion.model;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.TableName;

@Data
@Accessors(chain = true)
@TableName("ASSETPD")
public class AssetPd{
	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;
	private String	OrganizationName;//运营组织名称
	private String	department;//部门
	private String	jobNum;//使用人工号
	private String	userName;//使用人名字
	private String	Description;//计算机名
	 private String	AssetNumber;//资产编号
	private String	assetModel;//型号
	private String	allocation;//基本配置
	private String	SerialNumber;//序列号
	Date	pdTime;//盘点时间
	private Long pdBatId;//盘点批次编码
	int	status;//结果
	private Long pdCode;//盘点编码
	private String pdImgPath;//盘点图片链接


}
