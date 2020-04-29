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
@TableName("asset")
/**
 * Fusion资产模块
 */
public class Asset extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	private String CurrentLocationContext;
	private String NewWoAllowedFlag;
	private String ItemOrganizationId;
	private String ItemId;
	private String AssetNumber;
	private String LocationOrganizationId;
	private String MaintainableFlag;
	private String SerialNumber;
	private String DfltWoType;
	private String ActiveEndDate;
	private String DfltWoSubType;
	private String AssetId;
	private String Description;
	private String WorkCenterId;
	private String ItemNumber;
	private String ItemDescription;
	private String WorkCenterName;
	private String OrganizationCode;
	private String OrganizationName;
	private String Userid;
	private String Configuration;
	//敏感域的字段
	//财务编码	
	private String financialCode;
	//工号	
	private String jobnum;
	//使用人名称
	private String username;
	//机型
	private Integer htcIncredible;
	//型号
	private String assetmodel;
	//基本配置
	private String allocation;
	//显示器
	private String displayer;
	//MAC地址
	private String macaddress;
	//服务代码
	private String serviceid;
	//鼠标
	private String mouse;
	//无线MAC
	private String wifimac;
	//键盘
	private String keyboard;
	//电源适配器
	private String poweradapt;
	//启用时间
	private Date usingstarttime;
	//备注
	private String remark;
	//生产厂商售厂商
	private String manufacturer;
	//保修期
	private Integer warrantyperiod;
	//保修状态
	private int warrantStatus;
	//OA总计
	private BigDecimal amount;
	//状态
	private int status;
	//OA移交单据号
	private String OABillINum;
	//移交公司	
	private String handoverCpt;
	//移交人
	private String handoverPerson;
	//移交时间	
	private Date handoverTime;
	//资产类型
	private Integer assetType;
	//软件维保状态
	private Integer softwarestatus;
	//来源
	private Integer source;
	//过保日期
	private Date warrantdate;
	//过保提醒日期 
	private Date warrantyreminderdate;
	//套数
	private Integer suite;
	//软件类型 单机/网络版
	private Integer softType;
	//合同号
	private String contractId;
	
	private Date  updateTime;
	
	//修改人
	private String updateName;
	//修改操作的备注
	private String changeRemark;
	//修改时间
	private Date  changeTime;


}
