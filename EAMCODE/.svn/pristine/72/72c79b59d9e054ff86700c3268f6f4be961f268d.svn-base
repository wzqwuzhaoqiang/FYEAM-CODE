package com.fuyaogroup.eam.job;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.common.enums.AssetTypeEnum;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.Config;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.ConfigService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;

@Component    
@Slf4j
@EnableScheduling   // 2.开启定时任务
public class UpdateAllAssetJob {
	
	@Autowired
    private AssetService assetService;
	
	@Autowired
    private ConfigService configService;
	
	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	protected void updateAssetTasks() {
	// 1.根据eventID查询有进度更新系统（状态关闭、录入状态为未录入）
    	log.info("{}:执行定时任务更新资产开始...",LocalDateTime.now());
    	//1.获取   
    	List<Asset> assetList=null;
    	List<Config> configList = configService.getOrgList();//获取所有
    	try {
    		for(Config config:configList){
    			assetList = frutil.getAllAsset(config.getConfigCode());
    			if(CollectionUtil.isEmpty(assetList)) continue; 
    			//2.更新资产
    	    	for(Asset asset:assetList){
    	    		try{
    	    		List<Asset> st =assetService.getByAssetId(asset.getAssetId()) ;
    	    		if(CollectionUtil.isEmpty(st)){
    	    			asset=frutil.getOneAssetDspById(asset,asset.getAssetId());
    	    			asset.setOrganizationCode(config.getConfigCode());
    	    			asset.setOrganizationName(config.getConfigName());
    	    			if(asset.getHtcIncredible()==null||asset.getHtcIncredible()==6){
    	    				asset.setAssetType(AssetTypeEnum.CMP__SOFT_ASSET.getCode());
    	    			}else{
    	    				asset.setAssetType(AssetTypeEnum.CMP_ASSET.getCode());
    	    			}
    	    			
    	    			assetService.createOne(asset);
    	    		}	
    	    		else{
    	    			Asset asset_new=frutil.getOneAssetDspById(asset,asset.getAssetId());
    	    			asset_new.setOrganizationCode(config.getConfigCode());
    	    			asset_new.setOrganizationName(config.getConfigName());
    	    			if(asset_new.getHtcIncredible()==null||asset.getHtcIncredible()==6){
    	    				asset_new.setAssetType(AssetTypeEnum.CMP__SOFT_ASSET.getCode());
    	    			}else{
    	    				asset_new.setAssetType(AssetTypeEnum.CMP_ASSET.getCode());
    	    			}
    	    			if(asset_new.getJobnum()==null||asset_new.getJobnum()==""){
    	    				asset_new.setJobnum(st.get(0).getJobnum());
    	    			}
    	    			assetService.updateOne(asset_new);
    	    		}
    	    		}catch(Exception e){
    	    			log.error("更新编码:"+asset.getSerialNumber()+"失败，原因："+e.toString());
    	    		}	
    	    	}
    	    	
    		}
			 
		} catch (Exception e) {
			log.info("更新失败，原因+"+e.toString());
		}
    	log.info("{}:执行定时任务更新资产开始...",LocalDateTime.now());
    	
}
}
