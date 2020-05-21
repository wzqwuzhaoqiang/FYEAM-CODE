package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import com.fuyaogroup.eam.common.model.Page;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.enums.AssetTypeEnum;
import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetLifeRecored;


/**
 * 按灯编码对应表数据库调用
 *
 */
@Service
public interface AssetService extends IBaseService<Asset>{
	
	/**
	 * 获取全部资产
	 */
	public List<Asset> getAllCmpAsset(AssetTypeEnum assetType);

	/**
	 * 获取全部资产
	 */
	public List<Asset> getAllCmpAssetByPage(AssetTypeEnum assetType, Page page);


	/**
	 * 获取全部资产条数
	 */
	public Integer queryTotalRows(AssetTypeEnum assetType);


	/**
	 * 根据资产ID获取资产
	 * @param assetId
	 * @return
	 */
	public List<Asset> getByAssetId(String assetId);


	/**
	 * 
	 * @param asset
	 */
	public void createOne(Asset asset) throws DataAccessException;
	
	/**
	 * 根据组织，获取资产
	 * @param orgList
	 * @return
	 */
	public List<Asset> getAssetsByOrgList(String orgList);

	/**
	 * 根据工号查询资产
	 * @param userId
	 */
	public List<Asset> getCmpByJobNum(String userId);

	/**
	 * 获取
	 * @param assetNumber
	 * @return
	 */
	public List<Asset> getBySerialNumber(String assetNumber);
	
	public List<Asset> getByAssetNumber(String AssetNumber);

	public void updateOne(Asset asset);
	/**
	 * 获取指定资产信息
	 */
	public AssetLifeRecored getAssetByNumber(String AssetNumber);

	/**
	 * 获取借用记录
	 * @param assetNumber
	 * @return
	 */
	public List<AssetLifeRecored> getBorrowRecored(String assetNumber);

	/**
	 * 获取维修记录
	 * @param assetNumber
	 * @return
	 */
	public List<AssetLifeRecored> getRepairRecored(String assetNumber);

	/**
	 * 获取报废信息
	 * @param assetNumber
	 * @return
	 */
	public List<AssetLifeRecored> getScrapRecored(String assetNumber);
	
	
}
