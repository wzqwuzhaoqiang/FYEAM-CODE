package com.fuyaogroup.eam.modules.mes.service.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.mes.dao.AndonHisTempMapper;
import com.fuyaogroup.eam.modules.mes.model.AndonHisTemp;
import com.fuyaogroup.eam.modules.mes.service.AndonTempService;
@Slf4j
@Service
public class AndontempServiceImpl extends BaseServiceImpl<AndonHisTempMapper, AndonHisTemp>  implements AndonTempService{
	@Autowired
	private AndonHisTempMapper andonHisTempMapper;
	
	/**
	 * 插入临时表
	 */
	@Override
	public void insertAndonTemp(AndonHisTemp temp) throws Exception {
		// TODO Auto-generated method stub
		 andonHisTempMapper.insertTempById(temp);
				
	}

	@Override
	public AndonHisTemp getAndonTempByEId(Number eventId) throws Exception {
		List<AndonHisTemp> list=andonHisTempMapper.selectByEId(eventId);
		return list.size()==0?null:list.get(0);
	}

}
