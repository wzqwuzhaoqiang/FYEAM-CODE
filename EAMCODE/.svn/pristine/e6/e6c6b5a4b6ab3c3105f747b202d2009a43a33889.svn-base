package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetTransferMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetTransfer;
import com.fuyaogroup.eam.modules.fusion.service.AssetTransferService;
@Service
public class AssetTransferServiceImpl extends BaseServiceImpl<AssetTransferMapper, AssetTransfer> implements AssetTransferService{
	  @Autowired
		private AssetTransferMapper assetTransferMapper;

	@Override
	public List<AssetTransfer> getAll() {
		// TODO Auto-generated method stub
		return assetTransferMapper.queryAll();
	}

	@Override
	public void createOne(AssetTransfer assettf) {
		assetTransferMapper.createOne(assettf);
		
	}

	
	
	

	
	

	
	

}
