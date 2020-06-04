package com.fuyaogroup.eam.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import tk.mybatis.mapper.util.StringUtil;
import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.fastjson.JSONArray;
import com.fuyaogroup.eam.common.enums.FactoryEnum;
import com.fuyaogroup.eam.common.enums.FaultStatusEnum;
import com.fuyaogroup.eam.common.enums.FaultTypeEnum;
import com.fuyaogroup.eam.common.enums.OrgEnum;
import com.fuyaogroup.eam.common.enums.SHOperationEnum;
import com.fuyaogroup.eam.modules.fusion.model.AndonAsset;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.Operation;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;
import com.fuyaogroup.eam.modules.fusion.service.AndonAssetService;
import com.fuyaogroup.eam.modules.fusion.service.WorkOrderService;
import com.fuyaogroup.eam.modules.mes.model.AndonHis;
import com.fuyaogroup.eam.modules.mes.model.RepairBill;
/**
 * fusion EAM API接口
 * @author fuyao
 *
 */
@Slf4j
@Component
public class FusionEAMAPIUtil {
	
	@Value("${interface.fusionRest}")
	private  String fusionRest;
	
	@Value("${interface.fusionSoap.deaultUserName}")
	private  String deaultUserName;
	
	@Value("${interface.fusionSoap.deaultPassword}")
	private  String deaultPassword;
	
	@Value("${invention.organization}")
	private  String organizationId;
	
	@Autowired
    private AndonAssetService andonService;
	
	@Autowired
    WorkOrderService woService;
	
	private static  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static  SimpleDateFormat usingDateFormat=new SimpleDateFormat("yyyy-MM-dd");

	 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
	 private static  String itemNum = "生产设备";//TODO 测试环境，正式环境需要更换
	
	FusionRestUtil frUtil = new FusionRestUtil()  ;
	
	/**
	 * 创建检修单
	 * @param andon
	 * @return
	 * @throws Exception
	 */
	public boolean CreateNewWorkOrder(AndonHis andon) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		DecimalFormat df = new DecimalFormat("#.0");
		log.info("生成检修单:{}...",df.format(andon.getEvent_id()));
		HashMap<String, Object> testObj = new HashMap<String, Object>();
		WorkOrder wo = new WorkOrder();
		Map<String,Object> map = GetOneWorkOrder(andon.getEvent_id());
		if(CollectionUtil.isNotEmpty(map)){
			log.info("检修单:{},已生成...",df.format(andon.getEvent_id()));
			throw new Exception("检修单:"+df.format(andon.getEvent_id())+",已生成");
		}
		try{
		//1.增加基础数据
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders";
		//进行转换(工厂编号->组织ID) 现在默认300000002270290
				FactoryEnum code = EnumUtil.getByCode(andon.getPlant_code(), FactoryEnum.class);
				if(code==null){
					throw new Exception("传入组织编码为空或者展示不支持该组织");
				}else{
					testObj.put("OrganizationId", organizationId);
				}
		// 设备按灯编码和设备编码做转换 ，替换类
		if(andon.getAndon_code()==null){
			throw new Exception("新建工单"+df.format(andon.getEvent_id())+"失败，原因:传入按灯为空");
		}
		String assetNum = this.getAsstNum(andon.getAndon_code());
		if(assetNum==null){
			throw new Exception("新建工单"+df.format(andon.getEvent_id())+"失败，原因:传入按灯编码<"+andon.getAndon_code()+">没有对应的设备");
		}
		testObj.put("AssetNumber", assetNum);// 设备编码-按灯编码一一对应
		
		testObj.put("WorkOrderDescription", andon.getAndon_desc());//按灯描述
		testObj.put("WorkOrderSubTypeCode", "ORA_EMERGENCY");//工单类型-紧急-固定不动
		testObj.put("WorkOrderTypeCode", "CORRECTIVE");//工单状态-更正性-固定不动
		testObj.put("WorkOrderStatusCode", "ORA_UNRELEASED");//工单状态未发放
		testObj.put("WorkOrderPriority", "1");//优先级-固定不动
		testObj.put("WorkOrderNumber", df.format(andon.getEvent_id()));//问题ID
		JSONObject jsonObj = new JSONObject(testObj);
		String	mainworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject mainjb = com.alibaba.fastjson.JSONObject.parseObject(mainworkresult);
		
		String orderId =null ;
		if(null!=mainjb){
			orderId=mainjb.getString("WorkOrderId");
		}
		
		if(null!=orderId){
		/**
		 * 2.增加额外的数据:SERVER_ID:服务ID;SERVER_NAME:服务名称;BTYPE_CODE:事件类型编码;
		 * BTYPE_DESC:事件类型描述;ANDON_STATUS:安灯状态;RESPOND_VALUE:响应时长(分);
		 * MANAGE_VALUE:处理时长(分);T_TIME:触发时间;R_TIME:响应时间;C_TIME:恢复时间;
		 */
			andon.setId(Long.parseLong(orderId));
			try{
			this.createDescriptiveField(andon);
			}catch(Exception  e){
				throw new Exception("创建弹性域失败:"+e.toString());
			}
			try{
			//3.增加工序
			this.createOperation(orderId,andon.getPlant_code());
			}catch(Exception  e){
				throw new Exception("增加工序失败:"+e.toString());
			}
			//4.工单设置成已发放
			try{
			this.updateOrdersStatus(orderId);
			}catch(Exception  e){
				throw new Exception("工单设置成已发放失败:"+e.toString());
			}
		}
		}catch(Exception e){
			log.error("新建工单{}失败，请注意,原因：{}",df.format(andon.getEvent_id()),e.getMessage());
			throw new Exception("新建工单"+df.format(andon.getEvent_id())+"失败，原因:"+e.getMessage());
		}
		//TODO 存储在数据库 work_order里面
				WorkOrder workOrder = this.transWorkOrderFromAndon(andon,testObj);
				woService.createOne(workOrder);
		return true;
	}
	
	/**
	 * 按灯记录和工单的转换
	 * @param andon
	 * @param testObj
	 * @return
	 */
	private WorkOrder transWorkOrderFromAndon(AndonHis andon, HashMap<String, Object> testObj) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setSERVICEID( andon.getSERVER_ID()==null?"":andon.getSERVER_ID().toString());//SERVER_ID:服务ID
		workOrder.setOrganizationName( andon.getServer_name());//SERVER_NAME:服务名称
		workOrder.setBTYPE_CODE( andon.getBtype_code());//BTYPE_CODE:事件类型编码
		workOrder.setBTYPE_DESC( andon.getBtype_desc());//BTYPE_DESC:事件类型描述
		workOrder.setANDONSTATUS(andon.getAndon_status());//ANDON_STATUS:安灯状态
		workOrder.setRESPONDVALUE( andon.getRespond_value());//RESPOND_VALUE:响应时长(分)
		workOrder.setMANAGEVALUE( andon.getManage_value());//MANAGE_VALUE:处理时长(分)
		workOrder.setTTIME(formatter.format(andon.getT_time()) );//T_T

		workOrder.setOrganizationId(testObj.get("OrganizationId").toString());
		workOrder.setAssetNumber(testObj.get("AssetNumber").toString());
		workOrder.setWorkOrderDescription(testObj.get("WorkOrderDescription").toString());
		workOrder.setWorkOrderSubTypeCode(testObj.get("WorkOrderSubTypeCode").toString());
		workOrder.setWorkOrderTypeCode(testObj.get("WorkOrderTypeCode").toString());
		workOrder.setWorkOrderStatusCode(testObj.get("WorkOrderStatusCode").toString());
		workOrder.setWorkOrderAssetId(testObj.get("WorkOrderNumber").toString());
		workOrder.setWorkOrderPriority(testObj.get("WorkOrderPriority").toString());
		return workOrder;
	}

	/**
	 * 创建维修单
	 * @param rb
	 * @return
	 * @throws Exception
	 */
	public boolean CreateAssetWorkOrder(RepairBill rb) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		DecimalFormat df = new DecimalFormat("#.0");
		log.info("生成维修单:{}...",df.format(rb.getId()));
		HashMap<String, Object> testObj = new HashMap<String, Object>();
		Map<String,Object> map = GetOneWorkOrder(rb.getId());
		if(CollectionUtil.isNotEmpty(map)){
			log.info("检修单:{},已生成...",df.format(rb.getId()));
			//更新弹性域
			Map<String,Object> mapDsp = this.getAllDescpritons(rb.getId().toString());
			this.updatespareParts(rb, mapDsp.get("ASSORDER").toString()+rb.getSpareParts());
			throw new Exception("检修单:"+df.format(rb.getId())+",已生成");
		}
		try{
		//1.增加基础数据
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders";
		//TODO 进行转换(工厂编号->组织ID) 现在默认300000002270290
				OrgEnum code = EnumUtil.getByCode(rb.getCmpName(), OrgEnum.class);
				if(code==null){
					throw new Exception("传入组织编码为空或者展示不支持该组织");
				}else{
					testObj.put("OrganizationId", code.getMessage());
				}
				String assetNum = rb.getAssetCode();
				if(assetNum==null){
					throw new Exception("新建工单"+df.format(rb.getAssetCode())+"失败，原因:传入设备编码<"+rb.getAssetCode()+">没有对应的设备");
				}
				testObj.put("AssetNumber", assetNum);// 设备编码-按灯编码一一对应
				
		testObj.put("WorkOrderDescription", rb.getFaultCause());//按灯描述
		testObj.put("WorkOrderSubTypeCode", "ORA_EMERGENCY");//工单类型-紧急-固定不动
		testObj.put("WorkOrderTypeCode", "CORRECTIVE");//工单状态-更正性-固定不动
		testObj.put("WorkOrderStatusCode", "ORA_UNRELEASED");//工单状态未发放
		testObj.put("WorkOrderPriority", "1");//优先级-固定不动
		testObj.put("WorkOrderNumber", df.format(rb.getId()));//问题ID
		JSONObject jsonObj = new JSONObject(testObj);
		String	mainworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject mainjb = com.alibaba.fastjson.JSONObject.parseObject(mainworkresult);
		
		String orderId =null ;
		if(null!=mainjb){
			orderId=mainjb.getString("WorkOrderId");
		}
		
		if(null!=orderId){
		/**
		 * 2.增加额外的数据:SERVER_ID:服务ID;SERVER_NAME:服务名称;BTYPE_CODE:事件类型编码;
		 * BTYPE_DESC:事件类型描述;ANDON_STATUS:安灯状态;RESPOND_VALUE:响应时长(分);
		 * MANAGE_VALUE:处理时长(分);T_TIME:触发时间;R_TIME:响应时间;C_TIME:恢复时间;
		 */
			rb.setId(Long.parseLong(orderId));
			try{
			this.createDescriptiveField(rb);
			}catch(Exception  e){
				throw new Exception("创建弹性域失败:"+e.toString());
			}
			try{
			//3.增加工序
			this.createOperation(orderId,organizationId);
			}catch(Exception  e){
				throw new Exception("增加工序失败:"+e.toString());
			}
			//4.工单设置成已发放
			try{
			this.updateOrdersStatus(orderId);
			}catch(Exception  e){
				throw new Exception("工单设置成已发放失败:"+e.toString());
			}
		}
		}catch(Exception e){
			log.error("新建维修单{}失败，请注意,原因：{}",df.format(rb.getId()),e.getMessage());
			throw new Exception("新建维修单"+df.format(rb.getId())+"失败，原因:"+e.getMessage());
		}
		//TODO 存储在数据库 work_order里面
		WorkOrder workOrder = this.transWorkOrder(rb,testObj);
		woService.createOne(workOrder);
		return true;
	}
	
	private WorkOrder transWorkOrder(RepairBill rb, HashMap<String, Object> testObj) {
		WorkOrder workOrder = new WorkOrder();
		workOrder.setStatus( rb.getStatus().getCode());//TODO 保修状态
		workOrder.setReason( rb.getSuggestion());//BTYPE_DESC:事件类型描述
		workOrder.setTTIME(formatter.format(new Date()) );//T_TIME:触发时间:YYYY-MM-DDTHH:MI:SS.sss+hh:mm
		workOrder.setSolution( rb.getAcceptCheck());

		workOrder.setOrganizationId(testObj.get("OrganizationId").toString());
		workOrder.setAssetNumber(testObj.get("AssetNumber").toString());
		workOrder.setWorkOrderDescription(testObj.get("WorkOrderDescription").toString());
		workOrder.setWorkOrderSubTypeCode(testObj.get("WorkOrderSubTypeCode").toString());
		workOrder.setWorkOrderTypeCode(testObj.get("WorkOrderTypeCode").toString());
		workOrder.setWorkOrderStatusCode(testObj.get("WorkOrderStatusCode").toString());
		workOrder.setWorkOrderAssetId(testObj.get("WorkOrderNumber").toString());
		workOrder.setWorkOrderPriority(testObj.get("WorkOrderPriority").toString());
		return workOrder;
	}

	/**
	 * 根据按灯编码获取设备编码
	 * @param andon_code
	 * @return
	 * @throws Exception
	 */
	private String getAsstNum(String andon_code) throws Exception {
		AndonAsset asset = andonService.getAndonAssetByCode(andon_code);
		return null==asset?null:asset.getAsset_num();
	}

	/**
	 * 根据工单ID获取工单
	 * @param orderId
	 * @return 
	 * @throws Exception 
	 */
	public Map<String, Object> GetOneWorkOrder(Double orderId) {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		DecimalFormat df = new DecimalFormat("#.0");
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?q=WorkOrderNumber="+df.format(orderId);
		try{
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject mainjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		Map<String, Object> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(mainjb, Map.class);
		if(Integer.parseInt(itemMap.get("count").toString())==0){
			return null;
		}else{
			String one =  itemMap.get("items").toString();
			String oneStr = one.substring(1, one.length()-1);
			Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
		
		return oneMap;
		}
		}catch(Exception e){
			log.error("获取EAM工单失败，原因："+e.toString());
			return null;
		}
	

	}
	/**
	 * 根据工单ID获取工单
	 * @param orderId
	 * @return 
	 * @throws Exception 
	 */
	public Map<String, Object> GetOneWorkOrder(Long orderId) {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		DecimalFormat df = new DecimalFormat("#.0");
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?q=WorkOrderNumber="+df.format(orderId);
		try{
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject mainjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		Map<String, Object> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(mainjb, Map.class);
		if(Integer.parseInt(itemMap.get("count").toString())==0){
			return null;
		}else{
			String one =  itemMap.get("items").toString();
			String oneStr = one.substring(1, one.length()-1);
			Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
		
		return oneMap;
		}
		}catch(Exception e){
			log.error("获取EAM工单失败，原因："+e.toString());
			return null;
		}
	

	}
	
	/**
	 * 创建检修单弹性域字段
	 * @param andon
	 * @throws Exception
	 */
	public void createDescriptiveField(AndonHis andon) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		String orderUrl = fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+andon.getId()+"/child/WorkOrderDFF";
		otherObjs.put("__FLEX_Context", null);//SERVER_ID:服务ID
		otherObjs.put("SERVERID", andon.getSERVER_ID());//SERVER_ID:服务ID
		otherObjs.put("SERVERNAME", andon.getServer_name());//SERVER_NAME:服务名称
		otherObjs.put("BTYPECODE", andon.getBtype_code());//BTYPE_CODE:事件类型编码
		otherObjs.put("BTYPEDESC", andon.getBtype_desc());//BTYPE_DESC:事件类型描述
		otherObjs.put("ANDONSTATUS",andon.getAndon_status());//ANDON_STATUS:安灯状态
		otherObjs.put("RESPONDVALUE", andon.getRespond_value());//RESPOND_VALUE:响应时长(分)
		otherObjs.put("MANAGEVALUE", andon.getManage_value());//MANAGE_VALUE:处理时长(分)
		otherObjs.put("TTIME",formatter.format(andon.getT_time()) );//T_TIME:触发时间:YYYY-MM-DDTHH:MI:SS.sss+hh:mm
		if(null!=andon.getR_time()){
			otherObjs.put("RTIME", formatter.format(andon.getR_time()) );//R_TIME:响应时间
		}
		if(null!=andon.getC_time()){
			otherObjs.put("CTIME", formatter.format(andon.getC_time()) );//C_TIME:恢复时间
		}
		JSONObject jsonObj = new JSONObject(otherObjs);//T_TIME:触发时间:YYYY-MM-DDTHH:MI:SS.sss+hh:mm
		String	otherworkresult = frUtil.post(orderUrl, jsonObj.toString(),deaultUserName,deaultPassword);
//		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(otherworkresult);
//		Map<String, Object> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(otherjb, Map.class);
//		return itemMap;
	}
	
	/**
	 * 创建保修单弹性域字段
	 * @param rb
	 * @throws Exception
	 */
	public void createDescriptiveField(RepairBill rb) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String orderUrl = fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+rb.getId()+"/child/WorkOrderDFF";
		otherObjs.put("__FLEX_Context", null);//SERVER_ID:服务ID
		otherObjs.put("status", rb.getStatus());//TODO 保修状态
		otherObjs.put("reason", rb.getSuggestion());//BTYPE_DESC:事件类型描述
		otherObjs.put("TTIME",formatter.format(new Date()) );//T_TIME:触发时间:YYYY-MM-DDTHH:MI:SS.sss+hh:mm
		otherObjs.put("solution", rb.getAcceptCheck());
		JSONObject jsonObj = new JSONObject(otherObjs);//T_TIME:触发时间:YYYY-MM-DDTHH:MI:SS.sss+hh:mm
		String	otherworkresult = frUtil.post(orderUrl, jsonObj.toString(),deaultUserName,deaultPassword);
	}
	
	public void updatespareParts(RepairBill rb,String spareParts) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		String orderUrl = fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+rb.getId()+"/child/WorkOrderDFF";
		otherObjs.put("__FLEX_Context", null);//SERVER_ID:服务ID
		otherObjs.put("ASSORDER", spareParts);//TODO 保修状态
		JSONObject jsonObj = new JSONObject(otherObjs);//T_TIME:触发时间:YYYY-MM-DDTHH:MI:SS.sss+hh:mm
		String	otherworkresult = frUtil.post(orderUrl, jsonObj.toString(),deaultUserName,deaultPassword);
	}
	
	/**
	 * 创建基础工序（检查）
	 * @param WorkOrderId
	 * @param factory 
	 * @throws Exception 
	 */
	public void createOperation(String WorkOrderId, String factory) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
//		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders";
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+WorkOrderId+"/child/WorkOrderOperation";
		HashMap<String, String> testObj = new HashMap<String, String>();
		SHOperationEnum op = EnumUtil.getByCode(factory, SHOperationEnum.class);
		if(op==null){
			op=SHOperationEnum.OTHER;
		}
		testObj.put("StandardOperationId", op.getMessage());
		testObj.put("OperationSequenceNumber", "10");
		JSONObject jsonObj = new JSONObject(testObj);
		String	otherworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
//		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(otherworkresult);
		
	}
	/**
	 * 更新工单状态为已发放
	 * @param WorkOrderId
	 * @throws Exception 
	 */
	public void updateOrdersStatus(String WorkOrderId) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+WorkOrderId+"/";
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj.put("WorkOrderStatusCode", "ORA_RELEASED");
		JSONObject jsonObj = new JSONObject(testObj);
		String	otherworkresult = frUtil.patch(url, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(otherworkresult);
		
	}
	
	/**
	 * 获取资源
	 * @throws Exception
	 */
	public void getResource() throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/productionResources?q=ResourceCode="+"DCD";
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		System.out.print(otherjb.toString());
	}
	
	/**
	 * 创建资源
	 * @throws Exception
	 */
	public void createResource() throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/productionResources";
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj.put("OrganizationId", organizationId);
		testObj.put("OrganizationCode", "S1MAIN");
		testObj.put("UOMCode", "EA");
		testObj.put("ResourceCode", "QQQ");
		testObj.put("ResourceName", "钳");
		JSONObject jsonObj = new JSONObject(testObj);
		String workOrder = frUtil.post(url,jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		System.out.print(otherjb.toString());
	}
	
	/**
	 * 根据工厂编码-组织编码
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String getOrganization(String code) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/inventoryOrganizations?q=OrganizationCode="+code;
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		System.out.print(otherjb.toString());
		return otherjb.getString("OrganizationId");
		
	}
	/**
	 * 获取工单所有的弹性域
	 * @throws Exception
	 */
	public Map<String, Object> getAllDescpritons(String WorkOrderId) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+WorkOrderId+"/child/WorkOrderDFF";
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		if(CollectionUtil.isNotEmpty(otherjb)){
			Map<String, Object> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(otherjb, Map.class);
			if(Integer.parseInt(itemMap.get("count").toString())==0){
				return null;
			}else{
			String one =  itemMap.get("items").toString();
			String oneStr = one.substring(1, one.length()-1);
			Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
			return oneMap;
		}	
	}
		return null;
	}
	
	/**
	 * 获取基本操作
	 * @param operationCode
	 * @return
	 * @throws Exception
	 */
	public String getStandardOperation(String operationCode) throws Exception{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/standardOperations?q=StandardOperationCode="+operationCode;
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		String one =  otherjb.get("items").toString();
		String oneStr = one.substring(1, one.length()-1);
		Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
		return oneMap.get("StandardOperationId").toString();

	}
	
	/**
	 * 
	 * @param WorkOrderTypeCode：PREVENTIVE，CORRECTIVE
	 * @param WorkOrderStatusCode:ORA_UNRELEASED,ORA_RELEASED
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	public List<WorkOrder> getPreventWkOdrs(String workOrderTypeCode,String workOrderStatusCode,String startDate) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil();
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?q=WorkOrderTypeCode="+workOrderTypeCode+";WorkOrderStatusCode="+workOrderStatusCode+";PlannedStartDate>"+startDate;
		if(StringUtil.isEmpty(workOrderTypeCode)){
			 url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?q=WorkOrderStatusCode="+workOrderStatusCode+";PlannedStartDate>"+startDate;
		}
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<WorkOrder> wrList = this.getFusionListFromOjbect(reponseStr, WorkOrder.class);
		int index = 0;
		for(WorkOrder wo:wrList){
			List<Operation> opList = new ArrayList<Operation>();
			opList = this.getWkOdrsOperations(wo.getWorkOrderId());
			wo.setOptList(opList);
			wrList.set(index, wo);
			index++;
		}
		return wrList;
	}
	
	/**
	 * 获取工单标准操作
	 * @param workOrderId
	 * @return
	 * @throws Exception
	 */
	public List<Operation> getWkOdrsOperations(String workOrderId) throws Exception{
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+workOrderId+"/child/WorkOrderOperation";
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<Operation> opList = this.getFusionListFromOjbect(reponseStr, Operation.class);
		return opList;
	}
	
	/**
	 * 从Fusion返回的字符串，转成对应的类列表
	 * @param reponseStr
	 * @param object
	 * @return
	 */
	public  <T> List<T> getFusionListFromOjbect(String reponseStr,Class<T> object){
		List<T> list = new ArrayList<T>();  
		String result=reponseStr;
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(result);
		if(otherjb.get("items")!=null){
		list =JSONArray.parseArray(otherjb.get("items").toString(),object);
		}else{
			otherjb = com.alibaba.fastjson.JSONObject.parseObject(result);
			list =JSONArray.parseArray('['+otherjb.toString()+']',object);
		}
		return list;
	}
	
	/**
	 * 获取工单
	 * @param workOrderTypeCode
	 * @param workOrderStatusCode
	 * @param startDate
	 * @param endDate
	 * @param assetNum
	 * @param isDownload
	 * @return
	 * @throws Exception
	 */
	public List<WorkOrder> getWkOdrs(String workOrderTypeCode,String workOrderStatusCode,String startDate,String endDate,String assetNum,Boolean isDownload) throws Exception{
		 String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?limit=1000;";
		StringBuffer sb =new StringBuffer(fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?limit=1000&q=");
		if(StringUtil.isNotEmpty(workOrderStatusCode)){
			sb.append("WorkOrderStatusCode="+workOrderStatusCode+";");
		}
		if(StringUtil.isNotEmpty(workOrderTypeCode)){
			sb.append("workOrderTypeCode="+workOrderTypeCode+";");
		}
		if(StringUtil.isNotEmpty(startDate)){
			sb.append("PlannedStartDate>"+startDate+";");
		}
		if(StringUtil.isNotEmpty(assetNum)){
			sb.append("AssetNumber="+assetNum+";");
		}
		if(StringUtil.isNotEmpty(endDate)){
			sb.append("PlannedCompletionDate<"+endDate+" orderBy=PlannedCompletionDate:desc");
		}

		url = sb.substring(0, sb.length()-1);
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<WorkOrder> wrList = this.getFusionListFromOjbect(reponseStr, WorkOrder.class);
		if(isDownload){
			wrList=this.getAssetAndDescription(wrList);
	}
		return wrList;
	}
	
	/**
	 * 获取资产和弹性域
	 * @param wrList
	 * @return
	 * @throws Exception
	 */
	public List<WorkOrder> getAssetAndDescription(List<WorkOrder> wrList) throws Exception {
		int index = 0;
		Map<String, Object> asset = null;
		Map<String, Object> map = new HashMap<String, Object>();
		for(WorkOrder wo:wrList){
			map = this.getAllDescpritons(wo.getWorkOrderId());
			wo.setRepairMan(map.get("repairMan")!=null?map.get("repairMan").toString():"");
			wo.setReason(map.get("reason")!=null?map.get("reason").toString():"");
			wo.setFaultcode(map.get("faultcode")!=null?EnumUtil.getByCode(map.get("faultcode").toString(), FaultStatusEnum.class).getMessage():"");
			wo.setTTIME(map.get("TTIME")!=null?map.get("TTIME").toString():"");
			wo.setCTIME(map.get("CTIME")!=null?map.get("CTIME").toString():"");
			wo.setRTIME(map.get("RTIME")!=null?map.get("RTIME").toString():"");
			wo.setMANAGEVALUE(map.get("MANAGEVALUE")!=null?map.get("MANAGEVALUE").toString():"");
			wo.setFaulttype(map.get("faulttype")!=null?EnumUtil.getByCode(map.get("faulttype").toString(), FaultTypeEnum.class).getMessage():"");
			wo.setFaultScrapt(map.get("faultScrapt")!=null?map.get("faultScrapt").toString():"");
			wo.setSolution(map.get("solution")!=null?map.get("solution").toString():"");
			wo.setEquipmentPart(map.get("equipmentPart")!=null?map.get("equipmentPart").toString():"");
			wo.setTimeoutAnalysis(map.get("timeoutAnalysis")!=null?map.get("timeoutAnalysis").toString():"");

			asset = this.getOneDescription(wo.getAssetId());
			wo.setAssetName(asset.get("Description")!=null?asset.get("Description").toString():"");
			wo.setWorkcenter(asset.get("WorkCenterName")!=null?asset.get("WorkCenterName").toString():"");
			wo.setDescriptions(map);
			wrList.set(index, wo);
			index++;
		}
		return wrList;
	}

	/**
	 * 获取资产信息
	 * @param workOrderTypeCode
	 * @param workOrderStatusCode
	 * @param startDate
	 * @param endDate
	 * @param assetNum
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getOneDescription(String assetId) throws Exception{

	String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets/"+assetId+"/child/MaintenanceAssetDFF";
	String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
	List<HashMap> map = this.getFusionListFromOjbect(reponseStr,HashMap.class);
	return map.get(0);
	}
	
	/**
	 * 根据序列号，获取资产
	 * @param assetSerNum
	 * @return
	 * @throws Exception
	 */
	public Asset getOneAssetBySerNum(String assetSerNum) {
		List<Asset> assetList = null;
		try{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets?q=SerialNumber="+assetSerNum;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		assetList = this.getFusionListFromOjbect(reponseStr,Asset.class);
		}catch(Exception e){
			log.error("Fusion获取资产失败，原因："+e.getMessage());
			return null;
		}
		if(CollectionUtil.isNotEmpty(assetList)){
			try{
			Asset asset = assetList.get(0);
			Map<String,Object> map = this.getOneDescription(asset.getAssetId());
			if(map.get("allocation")!=null)
				asset.setAllocation(map.get("allocation").toString());
//			asset.setUserid(map.get("userid").toString());
			if(map.get("userid")!=null)
				asset.setUsername(map.get("userid").toString());
			if(map.get("username")!=null)
				asset.setUsername(map.get("username").toString());
			if(map.get("assetmodel")!=null)
				asset.setAssetmodel(map.get("assetmodel").toString());
			if(map.get("htcIncredible")!=null)
				asset.setHtcIncredible(Integer.parseInt(map.get("htcIncredible").toString()));
			if(map.get("displayer")!=null)
				asset.setDisplayer(map.get("displayer").toString());
			if(map.get("macaddress")!=null)
				asset.setMacaddress(map.get("macaddress").toString());
			if(map.get("serviceid")!=null)
				asset.setServiceid(map.get("serviceid").toString());
			if(map.get("mouse")!=null)
				asset.setMouse(map.get("mouse").toString());
			if(map.get("wifimac")!=null)
				asset.setWifimac(map.get("wifimac").toString());
			if(map.get("keyboard")!=null)
				asset.setKeyboard(map.get("keyboard").toString());
			if(map.get("poweradapt")!=null)
				asset.setPoweradapt(map.get("poweradapt").toString());
			if(map.get("usingstarttime")!=null)
			asset.setUsingstarttime(usingDateFormat.parse(map.get("usingstarttime").toString()));
			if(map.get("manufacturer")!=null)
				asset.setManufacturer(map.get("manufacturer").toString());
			if(map.get("warrantyperiod")!=null)
				asset.setWarrantyperiod(Integer.parseInt(map.get("warrantyperiod").toString()));
			if(map.get("amount")!=null)
				asset.setAmount(new BigDecimal(map.get("amount").toString()));
			if(map.get("status")!=null)
				asset.setStatus(Integer.parseInt(map.get("status").toString()));
			if(map.get("oabillnum")!=null)
				asset.setOABillINum(map.get("oabillnum").toString());;
			asset.setJobnum(map.get("jobnum")==null?map.get("softtype")==null?null:map.get("softtype").toString():map.get("jobnum").toString());
			if(map.get("prolinename")!=null)
				asset.setWorkCenterName(map.get("prolinename").toString());		
			return asset;
			}catch(Exception e){
				log.error("Fusion获取资产失败，原因："+e.getMessage());
				return null;
			}
			
		}
			return null;
		}
	
	public Asset getOneAssetDspById(Asset asset,String assetId) throws Exception{
		Map<String,Object> map = this.getOneDescription(asset.getAssetId());
		if(map.get("allocation")!=null)
			asset.setAllocation(map.get("allocation").toString());
		if(map.get("userid")!=null)
			asset.setUsername(map.get("userid").toString());
		if(map.get("username")!=null)
			asset.setUsername(map.get("username").toString());
		if(map.get("assetmodel")!=null)
			asset.setAssetmodel(map.get("assetmodel").toString());
		if(map.get("htcdesire")!=null)
			asset.setHtcIncredible(Integer.parseInt(map.get("htcdesire").toString()));
		if(map.get("displayer")!=null)
			asset.setDisplayer(map.get("displayer").toString());
		if(map.get("macaddress")!=null)
			asset.setMacaddress(map.get("macaddress").toString());
		if(map.get("serviceid")!=null)
			asset.setServiceid(map.get("serviceid").toString());
		if(map.get("mouse")!=null)
			asset.setMouse(map.get("mouse").toString());
		if(map.get("wifimac")!=null)
			asset.setWifimac(map.get("wifimac").toString());
		if(map.get("keyboard")!=null)
			asset.setKeyboard(map.get("keyboard").toString());
		if(map.get("poweradapt")!=null)
			asset.setPoweradapt(map.get("poweradapt").toString());
		if(map.get("usingstarttime")!=null)
			asset.setUsingstarttime(usingDateFormat.parse(map.get("usingstarttime").toString()));
		if(map.get("manufacturer")!=null)
			asset.setManufacturer(map.get("manufacturer").toString());
		if(map.get("warrantyperiod")!=null)
			asset.setWarrantyperiod(Integer.parseInt(map.get("warrantyperiod").toString()));
		if(map.get("amount")!=null)
			asset.setAmount(new java.math.BigDecimal(map.get("amount").toString()));
		if(map.get("status")!=null)
			asset.setStatus(Integer.parseInt(map.get("status").toString()));
		if(map.get("oabillnum")!=null)
			asset.setOABillINum(map.get("oabillnum").toString());;
		if(map.get("softtype")!=null)
			asset.setJobnum(map.get("softtype").toString().length()==6?map.get("softtype").toString():this.comAddZero(map.get("softtype").toString()));
		if(map.get("jobnum")!=null)
			asset.setJobnum(map.get("jobnum").toString().length()==6?map.get("jobnum").toString():this.comAddZero(map.get("jobnum").toString()));//TODO 测试
		return asset;
		
	}
	
	public String comAddZero(String str) {
		int zeroNum = 6 - str.length();
		for(int i=0;i<zeroNum;i++){
			str='0'+str;
		}
		return str;
	}

	/**
	 * 获取组织编码和itemID
	 * @param itemNumber
	 * @param organizationCode
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> getOneItemByOrgIdAndItemID(String itemNumber,String organizationName) throws Exception{
		//TODO 如果是固定的话，就去掉
		if(StringUtil.isEmpty(itemNumber)){
			itemNumber = itemNum;
		}
		if(StringUtil.isEmpty(organizationName)){
			throw new Exception("组织名称为空！");
		}
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/items?fields=ItemId,OrganizationId&q=ItemNumber=" + itemNumber  +";OrganizationId=" + EnumUtil.getByCode(organizationName, OrgEnum.class).getMessage()
			;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<Map> assetList = this.getFusionListFromOjbect(reponseStr,Map.class);
		return assetList.get(0);
		}
	
	/**
	 * 创建固定资产
	 * @param asset
	 * @return
	 * @throws Exception
	 */
	public Asset createOneAsset(Asset asset) throws Exception{
		//1.获取ItemID和组织ID
		Map map=this.getOneItemByOrgIdAndItemID(asset.getItemNumber(), asset.getOrganizationName());//生产设备,SHJC2<生产设备，集团管理局>
		asset.setItemId(map.get("ItemId").toString());
		asset.setLocationOrganizationId(map.get("OrganizationId").toString());
		//2.获取工作中心ID TODO 是否要做集成配置，或者动态存储
		try{
		String centerID = this.getOneWorkCenter(asset.getWorkCenterName(), asset.getLocationOrganizationId());
		asset.setWorkCenterId(centerID);
		}catch(Exception e){
			log.error("获取工作中心失败："+e.getMessage());
			String workcenterId = this.createWorkCenter(asset.getLocationOrganizationId(), asset.getWorkCenterName());
			asset.setWorkCenterId(workcenterId);
		}
		//3.创建一个资产
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets";
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		otherObjs.put("AssetNumber",asset.getAssetNumber());//保修期
		 otherObjs.put("SerialNumber",asset.getSerialNumber());//OA总计
		 otherObjs.put("Description",asset.getDescription());//状态
		 otherObjs.put("WorkCenterId",asset.getWorkCenterId());//OA移交单据号
		 otherObjs.put("ItemId",asset.getItemId());//状态
		 otherObjs.put("ItemOrganizationId",asset.getLocationOrganizationId());//OA移交单据号
		 otherObjs.put("LocationOrganizationId",asset.getLocationOrganizationId());//OA移交单据号
		 otherObjs.put("CurrentLocationContext","ORA_WORK_CENTER");//OA移交单据号
		JSONObject jsonObj = new JSONObject(otherObjs);
	
		String reponseStr = frUtil.post(url,jsonObj.toString(),deaultUserName,deaultPassword);
		

		List<Asset> assetList = this.getFusionListFromOjbect(reponseStr,Asset.class);
		//2.创建弹性域字段
		if(CollectionUtil.isEmpty(assetList)){
			return asset;
		}
		String assetId =assetList.get(0).getAssetId() ;
		asset.setAssetId(assetId);
		this.createAssetDescriptiveField(asset);
		return asset;
		}
	
	/**
	 * 创建资产弹性域字段
	 * @param asset
	 * @throws Exception
	 */
	public void createAssetDescriptiveField(Asset asset) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		
		 String assetUrl = fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceAssets/"+asset.getAssetId()+"/child/MaintenanceAssetDFF";//"/fscmRestApi/resources/11.13.18.05/maintenanceAssets/"+asset.getAssetId()+"/child/MaintenanceAssetDFF";
		 otherObjs.put("financialCode",asset.getFinancialCode());//财务编码
		 otherObjs.put("jobnum",asset.getJobnum());//工号
		 otherObjs.put("htcdesire",asset.getHtcIncredible());//工号
		 otherObjs.put("assetmodel",asset.getAssetmodel());//型号
		 otherObjs.put("allocation",asset.getAllocation());//基本配置
		 otherObjs.put("displayer",asset.getDisplayer());//显示器
		 otherObjs.put("macAddress",asset.getMacaddress());//MAC地址
		 otherObjs.put("serviceId",asset.getServiceid());//服务代码
		 otherObjs.put("mouse",asset.getMouse());//鼠标
		 otherObjs.put("wifiMac",asset.getWifimac());//无线MAC
		 otherObjs.put("keyBoard",asset.getKeyboard());//键盘
		 otherObjs.put("username", asset.getUsername());//用户名称
		 otherObjs.put("poweradapt",asset.getPoweradapt());//电源适配器
		 if(asset.getUsingstarttime()!=null)
			 otherObjs.put("usingstarttime",formatter.format(asset.getUsingstarttime()));//启用时闫
		 otherObjs.put("remark",asset.getRemark());//备注
		 otherObjs.put("manufacturer",asset.getManufacturer());//生产厂商售厂商
		 otherObjs.put("warrantyperiod",asset.getWarrantyperiod());//保修期
		 otherObjs.put("amount",asset.getAmount());//OA总计
		 otherObjs.put("status",asset.getStatus());//状态
		 otherObjs.put("oabillinum",asset.getOABillINum());//OA移交单据号
		 otherObjs.put("handovercpt",asset.getOrganizationName());
		 otherObjs.put("prolinename",asset.getWorkCenterName());
		JSONObject jsonObj = new JSONObject(otherObjs);
		String	otherworkresult = frUtil.post(assetUrl, jsonObj.toString(),deaultUserName,deaultPassword);
		log.debug(otherworkresult);
	}

	
	/**
	 * 更新资产弹性域字段
	 * @param asset
	 * @return
	 * @throws Exception
	 */
	public Asset updateAssetDescriptiveFields(Asset asset,boolean flag) throws Exception {
		String url=fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets/"+asset.getAssetId()+"/child/MaintenanceAssetDFF/";//+asset.getAssetId();
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		if(flag){
			 otherObjs.put("jobnum",asset.getJobnum());//工号
			 otherObjs.put("username",asset.getUsername());//工号
			 otherObjs.put("oabillnum",asset.getOABillINum());//OA移交单据号

			 otherObjs.put("handoverperson",asset.getHandoverPerson());//OA移交单据号
			 otherObjs.put("handovertime",simpleDateFormat.format(asset.getHandoverTime()));//OA移交单据号

                 otherObjs.put("handovercpt",asset.getHandoverCpt());//OA转移公司
		}else{
			 otherObjs.put("financialCode",asset.getFinancialCode());//财务编码
			 otherObjs.put("jobnum",asset.getJobnum());//工号
			 otherObjs.put("assetmodel",asset.getAssetmodel());//型号
			 otherObjs.put("allocation",asset.getAllocation());//基本配置
			 otherObjs.put("displayer",asset.getDisplayer());//显示器
			 otherObjs.put("macAddress",asset.getMacaddress());//MAC地址
			 otherObjs.put("serviceId",asset.getServiceid());//服务代码
			 otherObjs.put("mouse",asset.getMouse());//鼠标
			 otherObjs.put("wifiMac",asset.getWifimac());//无线MAC
			 otherObjs.put("keyBoard",asset.getKeyboard());//键盘
			 otherObjs.put("poweradapt",asset.getPoweradapt());//电源适配器
			 if(asset.getUsingstarttime()!=null)
				 otherObjs.put("usingstarttime",formatter.format(asset.getUsingstarttime()));//启用时闫
			 otherObjs.put("remark",asset.getRemark());//备注
			 otherObjs.put("manufacturer",asset.getManufacturer());//生产厂商售厂商
			 otherObjs.put("warrantyperiod",asset.getWarrantyperiod());//保修期
			 otherObjs.put("amount",asset.getAmount());//OA总计
			 otherObjs.put("status",asset.getStatus());//状态
			 otherObjs.put("oabillnum",asset.getOABillINum());//OA移交单据号
			 otherObjs.put("handovercpt",asset.getOrganizationName());
			 otherObjs.put("prolinename",asset.getWorkCenterName());
			 
		}
		
		 JSONObject jsonObj = new JSONObject(otherObjs);
		 String	otherworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);

		return asset;
	}
	
	/**
	 * 获取工作中心编码
	 * @param workCenterName
	 * @param organizationCode
	 * @return
	 * @throws Exception
	 */
	public String getOneWorkCenter(String workCenterName,String OrganizationId) throws Exception{
		if(StringUtil.isEmpty(workCenterName)||StringUtil.isEmpty(OrganizationId)){
			throw new Exception("工作中心名称或者组织编码为空！");
		}
		String url=fusionRest+"/fscmRestApi/resources/11.13.18.05/workCenters?q=WorkCenterName=" + workCenterName + ";OrganizationId=" + OrganizationId;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<Map> list = this.getFusionListFromOjbect(reponseStr,Map.class);
		if(CollectionUtil.isEmpty(list)){
			log.info("未查询到工作中心{}，组织名称为{}，的工作中心编码",workCenterName,OrganizationId);
			throw new Exception("未查询到工作中心"+workCenterName+"，组织为"+OrganizationId+"，的工作中心编码");
		}
		return list.get(0).get("WorkCenterId").toString();
	}
	/**
	 * 查找工作区
	 * @param OrganizationId
	 * @return
	 * @throws Exception 
	 */
	public String getWorkAreaId(String OrganizationId) throws Exception{
		if(StringUtil.isEmpty(OrganizationId)){
			throw new Exception("组织编码为空！");
		}
		String url=fusionRest+"/fscmRestApi/resources/11.13.18.05/workAreas?q=OrganizationId="  + OrganizationId;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<Map> list = this.getFusionListFromOjbect(reponseStr,Map.class);
		if(CollectionUtil.isEmpty(list)){
			log.info("未查询到组织名称为{}，的工作区编码",OrganizationId);
			throw new Exception("未查询到组织为"+OrganizationId+"，的工作中心编码");
		}
		return list.get(0).get("WorkAreaId").toString();
		
	}
	
	/**
	 * 创建工作中心
	 * @param OrganizationId
	 * @param BM
	 * @return
	 * @throws Exception
	 */
	public String createWorkCenter(String OrganizationId,String BM) throws Exception{
		String url=fusionRest+"//fscmRestApi/resources/11.13.18.05/workCenters";
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		 otherObjs.put("OrganizationId",OrganizationId);
		 otherObjs.put("WorkCenterName",BM);
		 otherObjs.put("WorkCenterDescription",BM);
		 otherObjs.put("WorkCenterCode",BM);
		 otherObjs.put("WorkAreaId",this.getWorkAreaId(OrganizationId));
		JSONObject jsonObj = new JSONObject(otherObjs);
		String	otherworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
		log.debug(otherworkresult);
		List<Map> list = this.getFusionListFromOjbect(otherworkresult,Map.class);
		return list.get(0).get("WorkCenterId").toString();

	}
	
	public List<Asset> getAllAsset(String organizationCode) throws Exception{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets?q=OrganizationCode="+organizationCode+"&totalResults=true";
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<Asset> assets = this.getFusionListFromOjbect(reponseStr, Asset.class);
		return assets.size()>1?assets:null;
	}
	
	public Asset getOneAssetByAssetNum(String AssetNumber) {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		List<Asset> assetList = null;
		try{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets?q=AssetNumber="+AssetNumber;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		assetList = this.getFusionListFromOjbect(reponseStr,Asset.class);
		}catch(Exception e){
			log.error("Fusion获取资产失败，原因："+e.getMessage());
			return null;
		}
		return assetList.get(0);
	}
	
	public Asset updateOneAsset(Asset asset){
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		List<Asset> assetList = null;
		try{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets/"+asset.getAssetId();
		//2.获取工作中心ID TODO 是否要做集成配置，或者动态存储
//		String centerID = this.getOneWorkCenter(asset.getWorkCenterName(), asset.getLocationOrganizationId());
//		asset.setWorkCenterId(centerID);
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		otherObjs.put("AssetNumber",asset.getAssetNumber());//保修期
		 otherObjs.put("SerialNumber",asset.getSerialNumber());//OA总计
		 otherObjs.put("Description",asset.getDescription());//状态
//		 if(asset.getWorkCenterId()!=null)
//			 otherObjs.put("WorkCenterId",asset.getWorkCenterId());
		JSONObject jsonObj = new JSONObject(otherObjs);
	
		String reponseStr = frUtil.patch(url,jsonObj.toString(),deaultUserName,deaultPassword);
		assetList = this.getFusionListFromOjbect(reponseStr,Asset.class);
		}catch(Exception e){
			log.error("Fusion获取资产失败，原因："+e.getMessage());
			return null;
		}
		return assetList.get(0);
	}
}
