package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.fuyaogroup.eam.common.model.Page;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.enums.AssetTypeEnum;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetMapper;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetLifeRecored;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
@Service
@Slf4j
public class AssetServiceImpl extends BaseServiceImpl<AssetMapper, Asset> implements AssetService{
	  @Autowired
		private AssetMapper assetMapper;

	@Override
	public List<Asset> getAllCmpAsset(AssetTypeEnum assetType) {
		return assetMapper.queryByType( assetType.getCode());
	}

	@Override
	public List<Asset> getAllCmpAssetByPage(AssetTypeEnum assetType, Page page) {
		Integer rowStartNum = page.getStar();
		Integer rowEndNum = page.getStar()+page.getPageSize();
		return assetMapper.queryByTypeByPage( assetType.getCode(),rowStartNum,rowEndNum);
	}

	@Override
	public Integer queryTotalRows(AssetTypeEnum assetType) {
		return assetMapper.queryTotalRows( assetType.getCode());
	}

	@Override
	public void createOne(Asset asset)  throws DataAccessException{
		assetMapper.createOne(asset);
		
	}

	@Override
	public List<Asset> getAssetsByOrgList(String assetPdBat) {
		String[] orgs = assetPdBat.split(",");
		List<String> list = new ArrayList<String>();
		for(String org:orgs){
			list.add(org);
		}
		return assetMapper.queryByOrg( AssetTypeEnum.CMP_ASSET.getCode(),list);
	}

	@Override
	public List<Asset> getCmpByJobNum(String userId) {
		return assetMapper.QueryByJobNum( AssetTypeEnum.CMP_ASSET.getCode(), userId);
		
	}

	@Override
	public List<Asset> getBySerialNumber(String SerialNumber) {
		return assetMapper.getBySerialNumber(SerialNumber);
	}
	
	@Override
	public List<Asset> getByAssetId(String assetId) {
		return assetMapper.getByAssetId(assetId);
	}

	@Override
	public void updateOne(Asset asset) {
		 assetMapper.updateOne(asset);
		
	}

	@Override
	public List<Asset> getByAssetNumber(String AssetNumber) {
		// TODO Auto-generated method stub
		return assetMapper.getByAssetNumber(AssetNumber);
	}

	@Override
	public AssetLifeRecored getAssetByNumber(String AssetNumber) {
		// TODO Auto-generated method stub
		return assetMapper.getAssetByNumber(AssetNumber);
	}

	@Override
	public List<AssetLifeRecored> getBorrowRecored(String assetNumber) {
		// TODO Auto-generated method stub
		return assetMapper.getBorrowRecored(assetNumber);
	}

	@Override
	public List<AssetLifeRecored> getRepairRecored(String assetNumber) {
		// TODO Auto-generated method stub
		return assetMapper.getRepairRecored(assetNumber);
	}

	@Override
	public List<AssetLifeRecored> getScrapRecored(String assetNumber) {
		// TODO Auto-generated method stub
		return assetMapper.getScrpRecored(assetNumber);
	}
	
	

	
	

	
	

}
