package com.fuyaogroup.eam.job;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.fuyaogroup.eam.common.enums.AssetTypeEnum;
import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.common.service.WeixinService;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.model.Config;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.ConfigService;
import com.fuyaogroup.eam.util.EmailUtil;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
/**
 * 微信通知定时器
 * @author fuyao
 *
 */   
@Slf4j
@EnableScheduling   // 2.开启定时任务
@Component
public class AssetCheckInfoJob{

	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	@Autowired
	ConfigService configService;
	
	static String INFORM_USER_LIST_TEST = "112520";
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	static SimpleDateFormat infoFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

	int[] dragList =null;
	@Autowired
	WeixinMessageService wxService;
	
	@Autowired
	WeixinService wx;
	
	@Autowired
	AssetPdBatService paBatService;
	
	@Autowired
	AssetPdService paService;
	
	@Autowired
	AssetService assetService;
	
	protected void execute()
			throws JobExecutionException {
		log.info("{}:执行定时任务-"+this.getClass().getName()+",开始...",LocalDateTime.now());
		Date nowDate = new Date();
		//1.通知盘点
		List<AssetPdBat> batList = paBatService.getAll();
		for(AssetPdBat bat:batList){
			if(!DateUtils.isSameDay(bat.getPdStartDate(), nowDate)){
				continue;
			}else{
				List<Asset> list  = assetService.getAssetsByOrgList(bat.getOrgList());
				for(Asset asset:list){
					 wx.getAccessToken();
					 if(asset.getJobnum()==null){
						 continue;
					 }
					 wxService.send(asset.getJobnum().toString(), "", "<"+myFormatter.format(bat.getPdStartDate())+"~"+myFormatter.format(bat.getPdEndDate())+"限期计算机资产盘点通知>\n您需要盘点资产如下:\n"
					 +"资产编号:"+asset.getAssetNumber()+"\n"
					 +"序列号:"+asset.getSerialNumber()+"\n"
					 +"型号:"+asset.getAssetmodel()+"\n"
					 +"配置:"+asset.getAllocation()+"\n"
					 		+ "(请点击微信下方“扫一扫”，扫描您的办公资产上粘贴的二维码，完成今年的资产盘点。)");
				}
			}
		}
		
	}
	
	protected void softWareInfo()
			throws  JobExecutionException  {
		log.info("{}:执行定时任务-"+this.getClass().getName()+",软件推送开始...",LocalDateTime.now());
		Date nowDate = new Date();
		//1.查找所有软件
		List<Asset> list  = assetService.getAllCmpAsset(AssetTypeEnum.CMP__SOFT_ASSET);
				for(Asset asset:list){
					if(DateUtils.isSameDay(asset.getWarrantyreminderdate(), nowDate)){
					 wx.getAccessToken();
					 wxService.send(asset.getJobnum().toString(), "",  "软件维保通知:\n"+asset.getDescription()+","+asset.getAssetmodel()+"\n软件将在"+myFormatter.format(asset.getWarrantdate())+",合同号："+asset.getOABillINum()+"过期，请及时维护！");
					 EmailUtil eutil = new EmailUtil();
					List<Config> confList = configService.getEmailList(3);//configType = 3 是邮件组
					List<String> address = new ArrayList<String>();
					for(Config config:confList){
						address.add(config.getConfigVal());//从配置表里面，获取邮件池 configType=3
					}
					try {
						eutil.sendEmail(address, "软件维保通知", "软件维保通知:\n"+asset.getDescription()+","+asset.getAssetmodel()+",合同号："+asset.getOABillINum()+"\n软件将在"+myFormatter.format(asset.getWarrantdate())+"过期，请及时维护！");
					} catch (Exception e) {
						throw new JobExecutionException(e.toString());
					}
					}
				}
			}
	
		
}

    
