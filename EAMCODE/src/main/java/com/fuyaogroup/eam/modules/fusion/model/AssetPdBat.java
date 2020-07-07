package com.fuyaogroup.eam.modules.fusion.model;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ASSETPDBAT")
public class AssetPdBat extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pdBatId;//盘点批次ID
	private String pdBatCode;//盘点编码-6位 201909
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date pdStartDate;//盘点开始时间 
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date pdEndDate ;//盘点结束时间
	private String iSAll ;//是否是全体公司,默认是0/null都是全体
	private String orgList;//公司用分号分开比如 万达公司;吉利公司
	private String headId;
}
