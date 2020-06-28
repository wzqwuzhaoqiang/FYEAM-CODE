package com.fuyaogroup.eam.modules.fusion.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("WINDOW_SERVER")
/**
 * 前台办公资产模块
 */
public class WindowServer extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	//@TableId(value = "ID")
	private String tableID;
	//@TableField("BORROWERID")
	private String borrowerId;//
	//@TableField("BORROWERNAME")
	private String borrowerName;//
	//@TableField("TOOLS")
	private String tools;//
	//@TableField("COUNT")
	private int count;//
	//@TableField("BORROWTIME")
	private String borrowTime;//
	//@TableField("RETURNTIME")
	private String returnTime;//
	//@TableField("STATUS")
	private String status;//
	//BORROWCONFIRM
	private String borrowConfirm;
	//BACKCONFIRM
	private String backConfirm;
	//PHOTO
	private String photo;
	//
	private String mobile;
	//
	private String serial;
	
	
	
	
	
	
	
	
	
	
	
}
