package com.fuyaogroup.eam.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.fuyaogroup.eam.common.service.qtfwWeixinMessageService;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
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
public class QtfwMessageJob{

	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	@Autowired
	ConfigService configService;
	
	@Autowired
	WindowServerService wss;

	
	@Autowired
	qtfwWeixinMessageService qtfwService;
	

	
	protected void execute()
			throws JobExecutionException, ParseException {
		log.info("{}:执行定时任务-"+this.getClass().getName()+",开始...",LocalDateTime.now());
		
		
		Date nowDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String newdate = sdf.format(nowDate);
		
		Calendar ctime = Calendar.getInstance();
		Date dt = ctime.getTime();
		List<WindowServer>  wslist =  wss.queryListByCondition();
		if(wslist!=null) {
			for(WindowServer ws:wslist) {
				System.out.println("进入定时任务的循环...............................");
				log.info("进入定时任务的循环...............................");
			    Date btime = sdf.parse(ws.getBorrowTime());
			    
			    Calendar obt = Calendar.getInstance();
			    obt.setTime(btime);
			    int day = obt.get(Calendar.DATE);
			    obt.set(Calendar.DATE,day+1);
			    if(ctime.after(obt)) {
					qtfwService.send(ws.getBorrowerId(), "", "您有前台借用品未归还，逾期7天未归还、损坏或丢失，将按照物品原价赔偿。请及时归还  :  "+ws.getTools()
					);
				}
			}
		}
		
					 

	}
	
}

    
