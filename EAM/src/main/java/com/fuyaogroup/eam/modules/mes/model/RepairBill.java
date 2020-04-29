package com.fuyaogroup.eam.modules.mes.model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.fuyaogroup.eam.common.enums.ComputerStatusEnum;
import com.fuyaogroup.eam.common.model.BaseEntity;


/**
 * 创建按灯模块视图表
 * @author liangqing.chi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class RepairBill extends BaseEntity{
	private ComputerStatusEnum status;
	private String proposer ;
	private String cmpName ;//公司名称或者公司编码
	private String cmpCode ;//公司名称或者公司编码
	private String userId;
	private String applyDepartment;
	private String assetName;
	private String assetCode;
	private String telphone;
	private String faultCause;
	private String assetModel;
	private String SerialNumber;
	private String allocation;
	private String suggestion;
	private String remark;
	private String acceptCheck;
	private String spareParts;
}
