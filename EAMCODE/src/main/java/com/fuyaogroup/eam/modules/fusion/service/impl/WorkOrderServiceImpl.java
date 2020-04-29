package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.WorkOrderMapper;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;
import com.fuyaogroup.eam.modules.fusion.service.WorkOrderService;
@Service
public class WorkOrderServiceImpl extends BaseServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService{
	  @Autowired
		private WorkOrderMapper woMapper;

	@Override
	public List<WorkOrder> getAllByType(Integer woType) {
		// TODO Auto-generated method stub
		return woMapper.queryAll(woType);
	}

	@Override
	public void createOne(WorkOrder workOrder) {
		 woMapper.insert(workOrder);
	}


	
	

	
	

}
