package com.fuyaogroup.eam.job;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.modules.mes.model.AndonHis;
import com.fuyaogroup.eam.modules.mes.service.AndonService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;

@Component
@EnableScheduling   // 2.开启定时任务
@Slf4j
//@ImportResource(value ="classpath:/scheduling-config.xml")
public class CreateWorkOrderJob {
	 //3.添加定时任务(60S)
	@Autowired
    private AndonService andonService;
	
	@Autowired
	private FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	protected void createWorkOrderTasks() {
    	log.info("{}:执行定时任务EAM创建检修单开始...",LocalDateTime.now());
    	//1.获取按灯触发列表
    	List<AndonHis> list = null;
    	DecimalFormat df = new DecimalFormat("#.0");
    	try {
    		Date endDate = new Date();
    		Date startTime = DateUtils.addDays(endDate, -1);
    		list = andonService.getAndonHisByDate(startTime);
//			list = andonService.getAndonHisByStatus("触发", new Date());
			log.debug(list.toString());
		} catch (Exception e) {
			log.error(e.getCause().toString());
		}
    	if(!CollectionUtil.isEmpty(list)){
    		 // 2.根据eventID来查询未生成检修单列表
    		for(int i=0;i<list.size();i++){
    			Double orderId = list.get(i).getEvent_id();
    			log.debug("查询检修单:{}...",df.format(orderId));
    			
				Map<String,Object> map = frutil.GetOneWorkOrder(orderId);
				if(CollectionUtil.isNotEmpty(map)){
					log.debug("检修单:{},已生成...",df.format(orderId));
					continue;
				}else{
					//生成检修单
					try {
						log.info("生成检修单:{}...",df.format(orderId));
						frutil.CreateNewWorkOrder(list.get(i));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error(e.getMessage());
					}
				}
    		}
    		
    	}
    	log.info("{}:执行定时任务EAM创建检修单结束...",LocalDateTime.now());
       
    }
}
