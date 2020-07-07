package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetrPdMapper;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
@Service
public class AssetPdServiceImpl extends BaseServiceImpl<AssetrPdMapper, AssetPd> implements AssetPdService{
	  @Autowired
		private AssetrPdMapper assetrPd;
	  
	
	public List<AssetPd> getbyBatId(Long batId) {
		if(batId==null){
			return assetrPd.queryAll();
		}
		return assetrPd.queryAllByBatId(batId);
	}

	@Override
	public void updateAssetPd(AssetPd assetPd) {
		 assetrPd.updatePd(assetPd);
	}

	@Override
	@Transactional
	public void createAllAssetPd(Long batId,List<Asset> assets) {
		for(int i=0;i<assets.size();i++){
			AssetPd assetPd = this.createPdFromAsset(assets.get(i),batId,i);
			assetrPd.insertPdRecord(assetPd);
		}
	}

	private AssetPd createPdFromAsset(Asset asset, Long batId, int i) {
		AssetPd pd = new AssetPd();
		pd.setAllocation(asset.getAllocation());
		pd.setAssetModel(asset.getAssetmodel());
		pd.setAssetNumber(asset.getAssetNumber());
		pd.setDepartment(asset.getWorkCenterName());
		pd.setDescription(asset.getDescription());
		pd.setJobNum(asset.getJobnum());
		pd.setOrganizationName(asset.getOrganizationName());
		pd.setSerialNumber(asset.getSerialNumber());
		pd.setUserName(asset.getUsername());
		pd.setStatus(PdStatusEnum.ASSET_PD_WAITING.getCode());
		pd.setPdBatId(batId);
		pd.setPdCode(batId*100+i);
		return pd;
	}

	@Override
	public List<AssetPd> getBySerialNum(String serialNum, Integer status) {
		if(status!=null){
			return assetrPd.queryByCode(serialNum, status);
		}
		return assetrPd.querySN(serialNum);
	}

	@Override
	public void insertPdRecord(AssetPd assetPd) {
		assetrPd.insertPdRecord(assetPd);
		
	}

	@Override
	public void updateAssetStatus(int status, Date pdTime, Long pdCode,
			String pdImgPath) {
		assetrPd.updatePdRecord(status, pdTime, pdCode, pdImgPath);
		
	}

	@Override
	public List<AssetPd> queryAllUnDoByBatId(Long batId) {
		// TODO Auto-generated method stub
		return assetrPd.queryAllUnDoByBatId(batId);
	}
	

}
