package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;


/**
 * 按灯编码对应表数据库调用
 * @author fuyao
 *
 */
@Service
public interface WorkOrderService extends IBaseService<WorkOrder>{
	
	/**
	 * 获取检修单-woType;//0-设备；1-计算机
	 */
	public List<WorkOrder> getAllByType(Integer woType);

	public void createOne(WorkOrder workOrder);
	
	
	
	
}
