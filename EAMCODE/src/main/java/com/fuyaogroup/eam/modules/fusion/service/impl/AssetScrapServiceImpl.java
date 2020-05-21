package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.model.Page;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetScrapMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetScrap;
import com.fuyaogroup.eam.modules.fusion.service.IAssetScrapService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetScrapServiceImpl extends BaseServiceImpl<AssetScrapMapper, AssetScrap> implements IAssetScrapService {
	
	@Autowired
	private AssetScrapMapper asm;
	@Override
	public List<AssetScrap> queryScrapList() {
		// TODO Auto-generated method stub
		return asm.queryScrapList();
	}
	@Override
	public List<AssetScrap> getAllScrapByPage(Page page) {
		Integer rowStartNum = page.getStar();
		Integer rowEndNum = page.getStar()+page.getPageSize();
		return asm.queryScrapListByPage(rowStartNum,rowEndNum);
	}


}
