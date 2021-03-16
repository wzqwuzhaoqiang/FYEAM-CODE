package com.fuyaogroup.eam.modules.fusion.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
@Service
public interface AssetPdService  extends IBaseService<AssetPd>{
	
	public List<AssetPd> getbyBatId(Long batId);
	
	public void updateAssetPd(AssetPd AssetPd);
	
	public void updateAssetStatus(@Param("status") int status,@Param("pdTime") Date pdTime,@Param("pdCode")Long pdCode,@Param("pdImgPath")String pdImgPath);
	
	public void createAllAssetPd(Long batId,List<Asset> assets);
	
	public void deleteAssetPd(String pdcode);
	
	public List<AssetPd> getBySerialNum(String serialNum,Integer status);
	
	public AssetPd getBySerialNumAndBatid(String serialNum,String batid);

	public void insertPdRecord(AssetPd assetPd);

	public List<AssetPd> queryAllUnDoByBatId(Long batId);

	public List<AssetPd> sendAgain(String pdBatId);

	public List<Long> queryMaxPdcode(Long batId);
	
	public boolean sendEmail(String result);
}
