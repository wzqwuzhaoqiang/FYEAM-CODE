package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.RepairRecordMapper;
import com.fuyaogroup.eam.modules.fusion.model.RepairRecord;
import com.fuyaogroup.eam.modules.fusion.service.IRepairRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RepairRecordImpl extends BaseServiceImpl<RepairRecordMapper, RepairRecord> implements IRepairRecordService {
	
	@Autowired
	private RepairRecordMapper rrmapper;
	
	/**
	 * 查询所有资产的维修履历
	 */
	@Override
	public List<RepairRecord> queryList() {
		List<RepairRecord> result = new ArrayList<RepairRecord>();
		result = rrmapper.queryList();
		return result;
	}
	/**
	 * 根据资产编码查询维修履历
	 */
	@Override
	public List<RepairRecord> queryListByAssetNumber(String assetNumber) {
		List<RepairRecord> result = new ArrayList<RepairRecord>();
		result = rrmapper.queryListByAssetNumber(assetNumber);
		return result;
	}

	

}
