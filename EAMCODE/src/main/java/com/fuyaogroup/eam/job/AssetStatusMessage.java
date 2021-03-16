package com.fuyaogroup.eam.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.model.AssetScrap;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.ConfigService;
import com.fuyaogroup.eam.modules.fusion.service.IAssetScrapService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 资产状态同步管理
 * @author fuyao
 *
 */   
@Slf4j
@EnableScheduling   // 2.开启定时任务
@Component
public class AssetStatusMessage{

	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	IAssetScrapService iass;
	
	@Autowired
	AssetService assetService;
	
	@Autowired
	AssetPdBatService assetPdBat;
	
	@Autowired
	AssetPdService assetPdService;


	
	protected void execute()
			throws JobExecutionException, ParseException {
		log.info("{}:执行定时任务-"+this.getClass().getName()+",开始...",LocalDateTime.now());
		List<AssetScrap> ascrapList = iass.getUnDoMessager();
		for(AssetScrap as:ascrapList) {
			List<Asset> relist = assetService.getByAssetNumber(as.getAssetNumber());
			if(!CollectionUtils.isEmpty(relist)) {
				Asset asset = relist.get(0);
				asset.setStatus(2);
				Date nowDate  = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
				List<AssetPdBat> nowTimeList = assetPdBat.getAllBDate(nowDate);
				if(!CollectionUtils.isEmpty(nowTimeList)) {
					for(AssetPdBat apd :nowTimeList) {
						AssetPd pd = assetPdService.getBySerialNumAndBatid(asset.getSerialNumber(),apd.getPdBatId());
						if(pd!=null) {
							
								assetPdService.deleteAssetPd(pd.getPdCode().toString());

						}
					}				
				}
				assetService.updateOne(asset);
				iass.update(asset.getAssetNumber());
			}else {
				iass.update(as.getAssetNumber());
			}
		}
		
					 

		
	}
	
}