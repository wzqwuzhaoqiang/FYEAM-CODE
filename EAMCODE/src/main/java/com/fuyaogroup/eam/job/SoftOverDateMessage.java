package com.fuyaogroup.eam.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.common.service.WeixinService;
import com.fuyaogroup.eam.common.service.qtfwWeixinMessageService;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.ConfigService;
import com.fuyaogroup.eam.modules.fusion.service.WindowServerService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
/**
 * 微信通知定时器
 * @author fuyao
 *
 */   
@Slf4j
@EnableScheduling   // 2.开启定时任务
@Component
public class SoftOverDateMessage{

	
	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	@Autowired
	ConfigService configService;
	
	@Autowired
	AssetService as;

	@Autowired
	WeixinMessageService wxService;

	
	protected void execute()
			throws JobExecutionException, ParseException {
		log.info("{}:执行定时任务-"+this.getClass().getName()+",开始...",LocalDateTime.now());
		
		List<Asset> wslist  =  as.getAllSoftAsset();
		Calendar ctime = Calendar.getInstance();
		if(wslist!=null) {
			for(Asset ws:wslist) {
				if(ws.getJobnum()!=null&&!"".equals(ws.getJobnum())) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(ws.getWarrantyreminderdate());
					if(ctime.after(cal)) {
						if(ctime.getTime().getDay()%2==0) {
							wxService.send(ws.getJobnum(), "", "软件维保通知  :  </br>"
									+ws.getDescription()+"软件</br>"
									+"型号："+ws.getAssetmodel()+"</br>"
									+"备注："+ws.getRemark()+"</br>"
									+"将于"+ws.getWarrantdate()+"过期，请及时维护!");
						}
					}
				}
				
			}
		}
		
					 

		
	}
	
}

    
