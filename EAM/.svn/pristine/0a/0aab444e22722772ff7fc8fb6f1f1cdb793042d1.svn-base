package com.fuyaogroup.eam.job;

import java.lang.reflect.Field;
import java.text.ParseException;
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

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.common.service.WeixinService;
import com.fuyaogroup.eam.modules.fusion.model.Operation;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
import com.fuyaogroup.eam.util.ImageUtil;

import freemarker.template.utility.StringUtil;
/**
 * 微信通知定时器
 * @author fuyao
 *
 */   
@Slf4j
@EnableScheduling   // 2.开启定时任务
@Component
public class WeixinInfoTransferJob{

	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	static String INFORM_DATE = "星期四";//周几进行下周维护任务的通知
	static String INFORM_DATE_NUM = "4";//周几进行下周维护任务的通知
	static String INFORM_IMG_PATH = "./pics/";//图片存储路径
	static String INFORM_USER_LIST = "112520|002303|012204|083380|0077046|008387|009474|009140|028226|009152|009149|061232|086497|085376|096455|090800|113906|010356|121332|121329|123885|123887|065708|010332|009142|009141|017444|059044|079047|064917|028227|072525|076488|095711|112928|120544|009131|19259|09136|14896|09134|09137|09130|29719|33304|35429|65626|09129|74781|82225|96141|10172|009135|020736|038329|050824|083217|114785|122797|";//周几进行下周维护任务的通知
//	static String INFORM_USER_LIST = "112520";
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	static SimpleDateFormat infoFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

	int[] dragList =null;
	static Integer DRAG_LINE_NUM = 16;//图片每行只允许10个字

	@Autowired
	WeixinMessageService wxService;
	
	@Autowired
	WeixinService wx;
	
	protected void execute()
			throws JobExecutionException {
		log.info("{}:执行定时任务-"+this.getClass().getName()+",开始...",LocalDateTime.now());
		Date nowDate = new Date();
		//1.通知下周维护任务
		if(isInformDate()){
			//1.1 获取下周未发放的工单
			String InfoDate = myFormatter.format(DateUtils.addDays(nowDate,8-Integer.parseInt(INFORM_DATE_NUM)));
			Date endDate =DateUtils.addDays(nowDate,15-Integer.parseInt(INFORM_DATE_NUM));
			try {
				List<WorkOrder> list =frutil.getPreventWkOdrs("PREVENTIVE", "ORA_UNRELEASED",InfoDate);
				List<WorkOrder> listResp=new ArrayList<WorkOrder>();
				if(CollectionUtil.isNotEmpty(list)){
					//不得超过结束时间
						for(WorkOrder wo:list){
							if(wo.getPlannedStartDate().before(endDate)){
								listResp.add(wo);
							}
						}
						if(CollectionUtil.isNotEmpty(listResp)){
					String[] datas = {"维护编码","维护内容","设备编码","检修日期","检修内容"};
					String data[][]=this.getImgTable(listResp,datas,"WorkOrderId|WorkOrderDescription|AssetNumber|PlannedStartDate|optList");
					ImageUtil cg = new ImageUtil();
					 cg.myGraphicsGeneration(data, INFORM_IMG_PATH+"myPic1"+InfoDate+".jpg","下周<"+InfoDate+"~"+endDate+">预防性维护通知：",dragList,DRAG_LINE_NUM);
					 wx.getAccessToken();
					 wxService.send(INFORM_USER_LIST, "", "下周<"+InfoDate+"~"+endDate+">预订检修工单通知：");
					 wxService.sendImg(INFORM_USER_LIST, "", INFORM_IMG_PATH+"myPic1"+InfoDate+".jpg");
				}
					}
			} catch (Exception e) {
				log.error(this.getClass().getName()+"：获取工单失败："+e.getMessage());
			}
		}
		
		//2.通知明天任务
		String InfoDate = myFormatter.format(DateUtils.addDays(nowDate,1));//测试改成0，原值为1
		try {
			List<WorkOrder> list =frutil.getPreventWkOdrs("PREVENTIVE", "ORA_RELEASED",InfoDate );
			List<WorkOrder> listResp=new ArrayList<WorkOrder>();
			if(CollectionUtil.isNotEmpty(list)){
				//不得超过结束时间
				if(CollectionUtil.isNotEmpty(list)){
					for(WorkOrder wo:list){
						if(myFormatter.format(wo.getPlannedStartDate()).compareTo(InfoDate)==0){
							listResp.add(wo);
						}
					}
				if(CollectionUtil.isNotEmpty(listResp)){
				String[] datas = {"维护编码","维护内容","设备编码","检修日期","检修内容"};
				String data[][]=this.getImgTable(listResp,datas,"WorkOrderId|WorkOrderDescription|AssetNumber|PlannedStartDate|optList");
				ImageUtil cg = new ImageUtil();
				 cg.myGraphicsGeneration(data, INFORM_IMG_PATH+"myPic"+InfoDate+".jpg","明日<"+InfoDate+">预防性维护列表",dragList,DRAG_LINE_NUM);
				wx.getAccessToken();
				wxService.send(INFORM_USER_LIST, "", "以下是明日<"+InfoDate+">"+listResp.size()+"个预防性维护任务：");
				wxService.sendImg(INFORM_USER_LIST, "", INFORM_IMG_PATH+"myPic"+InfoDate+".jpg");
				}
			}}} catch (Exception e) {
				log.error(this.getClass().getName()+"：发送微信消息失败："+e.getMessage());
			}
		
				
		log.info("{}:执行定时任务-"+this.getClass().getName()+",成功...",LocalDateTime.now());

	}
	
	private String[][] getImgTable(List<WorkOrder> list, String[] datas, String fields) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String[] data = StringUtil.split(fields, '|');
		dragList=new int[list.size()+1];
		String[][] dataInfo = new String[list.size()+1][datas.length] ;
		dataInfo[0]=datas;
		dragList[0]=1;
		for(int i = 0 ;i<list.size();i++){
		for(int j=0;j<data.length;j++){
			String str = data[j];
			if(str.contentEquals("optList")){
				String strs = "" ;
				int k=1;
				for(Operation oprt:list.get(i).getOptList()){
					strs+=k+"."+oprt.getOperationName()+";";
					k++;
				}
				if(strs!=null){
				dataInfo[i+1][j]=strs;
				}
				
			}else{
				Field[] flds = WorkOrder.class.getDeclaredFields();
				for(Field fld:flds){
					fld.setAccessible(true);
					if(data[j].contentEquals(fld.getName())){
						Object obj= fld.get(list.get(i));
						 if(str.contains("Date")){
							 try{
//								Date t = new Date();
								 Date t=infoFormatter.parse(obj.toString());
								String dateTime = myFormatter.format(t);
								dataInfo[i+1][j]=dateTime==null?"":dateTime;
							 }catch(Exception e){
								 System.out.println(e.getMessage());
							 }
						}else{
							dataInfo[i+1][j]=(obj==null?"":obj.toString());
						}
						
						break;
					}
					
				}
				
			}
			if((dataInfo[i+1][j]!=null)&&(dataInfo[i+1][j].length()>DRAG_LINE_NUM&&(dataInfo[i+1][j].length()/DRAG_LINE_NUM+1>dragList[i+1]))){
				dragList[i+1]=dataInfo[i+1][j].length()/DRAG_LINE_NUM+1;
			}
		}
		if(dragList[i+1]==0){
			dragList[i+1]=1;
		}
		}
		return dataInfo;
	}
	
	protected void executeShowList()
			throws JobExecutionException {
		log.info("{}:执行定时任务-展示昨日故障维修列表,开始...",LocalDateTime.now());
		Date nowDate = new Date();
		//3.昨日故障维修列表
		String infoDate = myFormatter.format(DateUtils.addDays(nowDate,-1));//测试改成0，原值为-1
//		String endDate = myFormatter.format(DateUtils.addDays(nowDate,-1));
		try {
			List<WorkOrder> list =frutil.getPreventWkOdrs("", "ORA_CLOSED",infoDate );
			List<WorkOrder> listResp=new ArrayList<WorkOrder>();
			if(CollectionUtil.isNotEmpty(list)){
				//不得超过结束时间
				if(CollectionUtil.isNotEmpty(list)){
					for(WorkOrder wo:list){
						if(myFormatter.format(wo.getPlannedStartDate()).compareTo(infoDate)==0){
							listResp.add(wo);
						}
					}
				if(CollectionUtil.isNotEmpty(listResp)){
				String[] datas = {"维护编码","维护内容","设备编码","检修时间","备注"};
				String data[][]=this.getImgTable(listResp,datas,"WorkOrderId|WorkOrderDescription|AssetNumber|PlannedStartDate|optList");
				ImageUtil cg = new ImageUtil();
				 cg.myGraphicsGeneration(data, INFORM_IMG_PATH+"myPic02"+infoDate+".jpg","<"+infoDate+">昨日完成故障维修列表",dragList,DRAG_LINE_NUM);
				wx.getAccessToken();
				wxService.send(INFORM_USER_LIST, "", "以下是<"+infoDate+">"+"昨日完成的"+listResp.size()+"个故障维修列表：");
				wxService.sendImg(INFORM_USER_LIST, "", INFORM_IMG_PATH+"myPic02"+infoDate+".jpg");
				}
			}
}} catch (Exception e) {
	log.error(this.getClass().getName()+"：获取工单失败："+e.getMessage());
}
		log.info("{}:执行定时任务-展示昨日故障维修列表,结束...",LocalDateTime.now());
		}

	public boolean isInformDate(){
		Date nowDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("E");
        String str = formatter.format(nowDate);
        if(str.contains(INFORM_DATE)){
        	return true;
        }
		return false;
		
	}
	//形如Mon Dec 31 00:00:00 CST 2012字符串转换为相应日期Date 
    public  Date parse(String str, String pattern, Locale locale) { 
        if(str == null || pattern == null) { 
            return null; 
        } 
        try { 
            return new SimpleDateFormat(pattern, locale).parse(str); 
        } catch (ParseException e) { 
            e.printStackTrace(); 
        } 
        return null; 
    } 

}

    
