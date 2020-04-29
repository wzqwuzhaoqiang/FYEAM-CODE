package com.fuyaogroup.eam.modules.mes.service.impl;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.mes.dao.AndonHisMapper;
import com.fuyaogroup.eam.modules.mes.model.AndonHis;
import com.fuyaogroup.eam.modules.mes.service.AndonService;
@Slf4j
@Service
public class AndonServiceImpl extends BaseServiceImpl<AndonHisMapper, AndonHis> implements AndonService{

	@Autowired
	private AndonHisMapper andonHisMapper;

	@Override
	public List<AndonHis> getAndonHisByStatus(String status, Date starttime) throws Exception{
		return andonHisMapper.queryByStatus(status);//, starttime
	}

	@Override
	public List<AndonHis> getAndonHisBySolStatus(String status, String solStatus,Date starttime)
			throws Exception {
		return andonHisMapper.queryBySolStatus(status,solStatus,starttime);//, starttime
	}


	public List<AndonHis> getAndonHisByDate(Date starttime) throws Exception {
		return andonHisMapper.queryByDate(starttime);//, starttime
	}
	

}
