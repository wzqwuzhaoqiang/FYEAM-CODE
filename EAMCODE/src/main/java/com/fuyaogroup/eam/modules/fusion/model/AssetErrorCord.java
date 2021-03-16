package com.fuyaogroup.eam.modules.fusion.model;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("ASSETERRORCORD")

public class AssetErrorCord extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String myid;
	
	private String oabilliNum;//OA单据
	
	
	private String tranMan;//转移人
	private String tranid;//转移人工号
	private String receiveMan;//接收人
	private String receiveid;//接收人工号
	private String assetName;//资产名称
	private String serial;//序列号
	@JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	
}
