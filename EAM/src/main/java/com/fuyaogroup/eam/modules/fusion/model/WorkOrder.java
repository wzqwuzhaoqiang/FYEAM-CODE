package com.fuyaogroup.eam.modules.fusion.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fuyaogroup.eam.common.model.BaseEntity;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkOrder extends BaseEntity{
	/**
	 * Fusion工单模块
	 */
//	private static final long serialVersionUID = 1L;
	
	//@JsonProperty(value = "name")//如果名称不一致，可添加该注释进行装换
	private String WorkOrderId;
	private String WorkOrderDescription;
	private String OrganizationCode;
	private String OrganizationId;
	private String ItemNumber;
	private String InventoryItemId;
	private String AssetNumber;
	private String WoAssetId;
	private String WorkOrderTypeCode;
	private String WorkOrderSubTypeCode;
	private String MntWorkDefinitionCode;
	private String WorkDefinitionId;
	private String WorkDefinitionVersionId;
	private Date WorkDefinitionAsOfDateMnt;
	private String WorkOrderStatusCode;
	private String WorkOrderStatusId;
	private String Status;
	private Date PlannedStartDate;
	private Date PlannedCompletionDate;
	private String ActualStartDate;
	private String ActualCompletionDate;
	private String ReleasedDate;
	private String ClosedDate;
	private String WorkOrderPriority;
	private String WorkMethodCode;
	private String CanceledReason;
	private String AssetId;
	private String WorkOrderAssetId;
	private List<Operation> optList;
	private String assetName;
	private String workcenter;
	private Map<String, Object> descriptions;//获取弹性域
	private String repairMan;
	private String reason;
	private String faultcode;
	private String TTIME;
	private String CTIME;
	private String RTIME;
	private String faulttype;
	private String faultScrapt;
	private String solution;
	private String timeoutAnalysis;
	private String MANAGEVALUE;
	private String equipmentPart;
	private String SERVICEID;
	private String BTYPE_CODE;
	private String BTYPE_DESC;
	private String ANDONSTATUS;
	private String RESPONDVALUE;
	private Integer woType;//0-设备；1-计算机
	private String OrganizationName;
}
