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

import com.fuyaogroup.eam.common.enums.FaultStatusEnum;
import com.fuyaogroup.eam.modules.mes.model.AndonHis;
import com.fuyaogroup.eam.modules.mes.model.AndonHisTemp;
import com.fuyaogroup.eam.modules.mes.service.AndonService;
import com.fuyaogroup.eam.modules.mes.service.AndonTempService;
import com.fuyaogroup.eam.util.EnumUtil;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;

@Component    
@Slf4j
@EnableScheduling   // 2.开启定时任务
//@ImportResource(value ="classpath:/scheduling-config.xml")
public class UpdateWorkOrderJob {
	
	@Autowired
    private AndonService andonService;
	
	@Autowired
    private AndonTempService andontempService;
	
	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	 //3.添加定时任务(60S)
//    @Scheduled(cron = "0 */10 * * * ?")
	protected void updateWorkOrderTasks() {
	// 1.根据eventID查询有进度更新系统（状态关闭、录入状态为未录入）
    	log.info("{}:执行定时任务EAM更新检修单开始...",LocalDateTime.now());
    	//1.获取按灯触发列表
    	List<AndonHis> list = null;
    	try {
    		Date endDate = new Date();
    		Date startTime = DateUtils.addDays(endDate, -10);
			list = andonService.getAndonHisBySolStatus("关闭","未录", startTime);
			log.debug(list.toString());
		} catch (Exception e) {
			log.error(e.getCause().toString());
		}
    //2.更新EAM检修单的处理时间、反应时间等（状态为未关闭）
    	if(CollectionUtil.isNotEmpty(list)){
		    for(int i=0;i<list.size();i++){
		    	AndonHis andon = list.get(i);
		    	Double orderId = andon.getEvent_id();
		    	DecimalFormat df = new DecimalFormat("#.0");		
			try {
				log.debug("2.1 查詢是否已回寫臨時表:{}...",df.format(orderId));
					AndonHisTemp temp = andontempService.getAndonTempByEId(orderId);
					if((null !=temp)){//如果临时表有数据，不进行更新操作
						log.debug("MES临时表{}已回写",df.format(orderId));
						continue;
					}
					log.debug("2.2 查询EAM检修单:{}...",df.format(orderId));
					Map<String,Object> map = frutil.GetOneWorkOrder(orderId);
					if(CollectionUtil.isNotEmpty(map)){
						andon.setId(Long.parseLong(map.get("WorkOrderId").toString()));
					if("ORA_CLOSED".equals(map.get("WorkOrderStatusCode"))||"ORA_CANCELED".equals(map.get("WorkOrderStatusCode"))){
						log.debug("2.3 更新EAM检修单:{}",df.format(orderId));
						try{
						 frutil.createDescriptiveField(andon);
						 log.debug("2.4 更新MES临时表，问题编码{}",df.format(orderId));
						temp = this.createAndonHisTemp(andon,map);
						andontempService.insertAndonTemp(temp);
						}catch(Exception e){
							log.error("更新EAM检修单：{}失败,{}",df.format(orderId),e.toString());
						}
						
						}else{
						log.debug("2.4 更新EAM检修单:{}",df.format(orderId));
						try{
						 frutil.createDescriptiveField(andon);
						}catch(Exception e){
							log.error("更新EAM检修单：{}失败,{}",df.format(orderId),e.toString());
						}
					}
					}}catch (Exception e) {
						log.error("更新EAM检修单：{}失败,{}",df.format(orderId),e.toString());
				}
				}
		    }
		    log.info("{}:执行定时任务EAM更新检修单结束...",LocalDateTime.now());
    	
    }

    /**
     * 创建按灯临时记录
     * @param andonHis
     * @param map
     * @return
     * @throws Exception 
     */
	private AndonHisTemp createAndonHisTemp(AndonHis andonHis,
			Map<String, Object> map) throws Exception {
		AndonHisTemp temp = new AndonHisTemp();
		Map<String, Object> oneMap=null;
		try {
			oneMap = frutil.getAllDescpritons(andonHis.getId().toString());
		} catch (Exception e) {
			throw new Exception("更新失败，获取敏感域失败"+e.getMessage());
		}
		if(CollectionUtil.isNotEmpty(oneMap)){
			/**
			 *  故障類型ELECTRICAL/SOFTWARE/OTHER/MACHINE 

			 */	
			temp.setFault_type(null==oneMap.get("faulttype")?"":oneMap.get("faulttype").toString());

			/**
			 *  故障編碼/故障 1-16
			 */
			temp.setFault_code(null==oneMap.get("faultcode")?"":oneMap.get("faultcode").toString());
			temp.setFault_info(null==oneMap.get("faultcode")?"":EnumUtil.getByCode(temp.getFault_code(), FaultStatusEnum.class).getMessage());
			/**
			 *  修复员
			 */
			temp.setRepair_man(null==oneMap.get("repairMan")?"":oneMap.get("repairMan").toString());
			/**
			 * 解決方案
			 */
			temp.setSolution(null==oneMap.get("solution")?"":oneMap.get("solution").toString());
			/**
			 *  出現原因
			 */
			temp.setReason(null==oneMap.get("reason")?"":oneMap.get("reason").toString());
			/**
			 *  安灯责任WORKSHOP	车间;EQ	设备科

			 */
			temp.setResponse_unit(null==oneMap.get("responseUnit")?"":oneMap.get("responseUnit").toString());
		}
//		DecimalFormat df = new DecimalFormat("#.0");
		temp.setEvent_id(andonHis.getEvent_id());
		temp.setCreatedBy(new Long(123));
		temp.setLastUpdatedBy(new Long(123));
		temp.setLastUpdateDate(new Date());
		temp.setCreationDate(new Date());
		temp.setTimteout_analysis(null==oneMap.get("timeoutAnalysis")?"":oneMap.get("timeoutAnalysis").toString());
		temp.setUpdate_file("");
		temp.setFalut_scrapt(null==oneMap.get("faultScrapt")?"":oneMap.get("faultScrapt").toString());
		temp.setIs_stop("");
		temp.setOperate_man("");
		
		temp.setSpare_part("");
		temp.setResponse_unit(andonHis.getPlant_code());
		temp.setDescription(andonHis.getAndon_desc());
		temp.setStatus("DONE");//EXE,DONE
		temp.setAttribute8(null==oneMap.get("equipmentPart")?"":oneMap.get("equipmentPart").toString());

		temp.setMes_process_status("P");
		temp.setRemark("");
		
		return temp;
	}
}
