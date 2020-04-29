package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.RepairRecord;

@Service
public interface IRepairRecordService extends IBaseService<RepairRecord> {

	/**
	 * 查询所有资产的维修列表
	 */
	public List<RepairRecord> queryList();

	public List<RepairRecord> queryListByAssetNumber(String assetNumber);
}
