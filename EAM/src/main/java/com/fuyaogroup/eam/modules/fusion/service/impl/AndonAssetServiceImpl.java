package com.fuyaogroup.eam.modules.fusion.service.impl;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AndonAssetMapper;
import com.fuyaogroup.eam.modules.fusion.model.AndonAsset;
import com.fuyaogroup.eam.modules.fusion.service.AndonAssetService;
@Slf4j
@Service
public class AndonAssetServiceImpl extends BaseServiceImpl<AndonAssetMapper, AndonAsset> implements AndonAssetService{

	@Autowired
	private AndonAssetMapper andonMapper;

	@Override
	public AndonAsset getAndonAssetByCode(String code) throws Exception {
		// TODO Auto-generated method stub
		List<AndonAsset> andon = andonMapper.queryByCode(code);
		return andon.size()==0?null:andon.get(0);
	}
	
	
}
