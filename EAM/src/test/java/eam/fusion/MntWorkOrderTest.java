package eam.fusion;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import jxl.read.biff.BiffException;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.fastjson.JSONArray;
import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.common.enums.AssetTypeEnum;
import com.fuyaogroup.eam.common.enums.CenterEnum;
import com.fuyaogroup.eam.common.enums.FactoryEnum;
import com.fuyaogroup.eam.common.enums.OrgEnum;
import com.fuyaogroup.eam.common.enums.ToolEnum;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.util.EnumUtil;
import com.fuyaogroup.eam.util.ExcelUtil;
import com.fuyaogroup.eam.util.FusionRestUtil;

import freemarker.template.utility.StringUtil;



@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class MntWorkOrderTest {
	
//	@Value("${interface.fusionRest}")
	private  String fusionRest="https://ekfa-test.fa.us2.oraclecloud.com";
	
//	@Value("${interface.fusionSoap.deaultUserName}")
	private  String deaultUserName="112520";
	
//	@Value("${interface.fusionSoap.deaultPassword}")
	private  String deaultPassword="fy123456";
	
//	@Value("${invention.organization}")
	private  String organizationId="300000002809683";
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);

	@Autowired
	AssetService assetService;
	
	FactoryEnum factory = FactoryEnum.SHGH1;//导入的单位OrganizationId
	String path = "C:\\GH01-LZOperation.xlsx";//BB、GH01、JC操作工序表格
	String softWarePath = "C:\\software.xlsx";
	String assetPath = "C:\\fy-ff.xlsx";
	/**
	 * 
	 * 1.获取组织ID,配置到地方去
	 */
	@Test
	public void getOragizationAll() throws Exception {
		String val = this.getOrganization("FY_QBJT");//FQ_QB
		System.out.println("1233"+":"+val);
		for(FactoryEnum enums :FactoryEnum.values()){
			 val = this.getOrganization(enums.getCode());
			System.out.println(enums.getCode()+":"+val);
		}
		}
		
	/**
	 * 2.进行配置
	 * @throws Exception
	 */
	@Test
	public void createAll() throws Exception {
		//1.创建资源
		this.setResoureAll();
		//2.加入中心
		this.addResourceToCenter();
		//3.创建标准工序
		this.createStandardOperations();
	}

	
	@Test
	public void test() throws Exception {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		//1.增加基础数据
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders";
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj.put("AssetNumber", "M001023");//TODO 设备编码-按灯编码一一对应
		testObj.put("WorkOrderDescription", "更换洗涤机毛刷");//按灯描述
		testObj.put("WorkOrderSubTypeCode", "ORA_EMERGENCY");//工单类型-紧急-固定不动
		testObj.put("WorkOrderTypeCode", "CORRECTIVE");//工单状态-更正性-固定不动
		testObj.put("WorkOrderStatusCode", "ORA_UNRELEASED");//工单状态未发放
		testObj.put("WorkOrderPriority", "1");//优先级-固定不动
		testObj.put("WorkOrderNumber", "93655643.3");//问题ID
		testObj.put("OrganizationId", "300000002270290");
		JSONObject jsonObj = new JSONObject(testObj);
		String	mainworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject mainjb = com.alibaba.fastjson.JSONObject.parseObject(mainworkresult);
		
		
		String orderId = "300000003516784";
		if(null!=mainjb){
			orderId=mainjb.getString("WorkOrderId");
		}
		
		
		if(null!=orderId){
		/**
		 * 2.增加额外的数据:SERVER_ID:服务ID;SERVER_NAME:服务名称;BTYPE_CODE:事件类型编码;
		 * BTYPE_DESC:事件类型描述;ANDON_STATUS:安灯状态;RESPOND_VALUE:响应时长(分);
		 * MANAGE_VALUE:处理时长(分);T_TIME:触发时间;R_TIME:响应时间;C_TIME:恢复时间;
		 */
		this.createDescriptiveField(orderId);
		//3.增加工序
		this.createOperation(orderId);
		//4.工单设置成已发放
		this.updateOrdersStatus(orderId);
		}
		
		//JSONObject mainjb = JSONObject.parseObject(mainworkresult);
		
	}
	
//	@Test
//	public void GetOneTest() throws Exception{
//		FusionRestUtil frUtil = new FusionRestUtil()  ;
//		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?q=WorkOrderNumber=1053";
//		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
//		//JSONObject mainjb = JSONObject.parseObject(workOrder);
//
//	}
	
	public Map<String, Object> GetOneTest(Double orderId) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		DecimalFormat df = new DecimalFormat("#.0");
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?q=WorkOrderNumber="+df.format(orderId);
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject mainjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		Map<String, Object> itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(mainjb, Map.class);
		if(Integer.parseInt(itemMap.get("count").toString())==0){
			return null;
		}
		return itemMap;
		//JSONObject mainjb = JSONObject.parseObject(workOrder);

	}
	
	public void createDescriptiveField(String WorkOrderId) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		HashMap<String, String> otherObjs = new HashMap<String, String>();
		String orderUrl = fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+WorkOrderId+"/child/WorkOrderDFF";
		otherObjs.put("__FLEX_Context", null);//SERVER_ID:服务ID
		otherObjs.put("SERVERID", "30");//SERVER_ID:服务ID
		otherObjs.put("SERVERNAME", "龙田");//SERVER_NAME:服务名称
		otherObjs.put("BTYPECODE", "EQ");//BTYPE_CODE:事件类型编码
		otherObjs.put("BTYPEDESC", "设备类型按灯");//BTYPE_DESC:事件类型描述
		otherObjs.put("ANDONSTATUS", "响应");//ANDON_STATUS:安灯状态
		otherObjs.put("RESPONDVALUE", "20");//RESPOND_VALUE:响应时长(分)
		otherObjs.put("MANAGEVALUE", "20");//MANAGE_VALUE:处理时长(分)
		otherObjs.put("TTIME","2019-05-14T00:00:00+00:00" );//T_TIME:触发时间:YYYY-MM-DDTHH:MI:SS.sss+hh:mm
		otherObjs.put("RTIME", "2019-05-14T00:00:00+00:00");//R_TIME:响应时间
		otherObjs.put("CTIME", "2019-05-14T00:00:00+00:00");//C_TIME:恢复时间
		JSONObject jsonObj = new JSONObject(otherObjs);
		String	otherworkresult = frUtil.post(orderUrl, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(otherworkresult);
	}
	
	public void createOperation(String WorkOrderId) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
//		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders";
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+WorkOrderId+"/child/WorkOrderOperation";
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj.put("StandardOperationId", "300000003503079");
		testObj.put("OperationSequenceNumber", "10");
		JSONObject jsonObj = new JSONObject(testObj);
		String	otherworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(otherworkresult);
		
	}
	public void updateOrdersStatus(String WorkOrderId) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		WorkOrderId = "300000003471815";
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+WorkOrderId;
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj.put("WorkOrderStatusCode", "ORA_RELEASED");
		JSONObject jsonObj = new JSONObject(testObj);
		String	otherworkresult = frUtil.patch(url, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(otherworkresult);
		
	}
	
	public String getResource(String code) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/productionResources?q=ResourceCode="+code+";OrganizationId="+organizationId;
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
//		String one =  otherjb.get("items").toString();
//		String oneStr = one.substring(1, one.length()-1);
//		String[] mapStr = StringUtil.split(oneStr, "\"OrganizationId\":", false);
//		for(int i=1;i<mapStr.length;i++){
//			String str = mapStr[i];
//			 String[] str1= StringUtil.split(str, "\"ResourceId\":", false);
//			 String str2 = str1[1].substring(0, 15);
//			String onestr = str.substring(0, 15);
			String one =  otherjb.get("items").toString();
			String oneStr = one.substring(1, one.length()-1);
			Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
//			if(null!=str2&&onestr.equals(organizationId))
//				return str2;

//		}
		return oneMap.get("ResourceId").toString();
		
	}
	
	
	public void createResource(String code,String name) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/productionResources";
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj.put("OrganizationId", organizationId);
		testObj.put("CostedFlag", "Y");
		testObj.put("UOMCode", "EA");
		if("Operator".equals(code)==true){
			testObj.put("ResourceType", "Labor");
		}else{
			testObj.put("ResourceType", "Equipment");
		}
		
		testObj.put("ResourceCode", code);
		testObj.put("ResourceName", name);
		
		JSONObject jsonObj = new JSONObject(testObj);
		String workOrder = frUtil.post(url,jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		System.out.print(otherjb.toString());
	}
	
	@Test
	public void setResoureAll() throws BiffException, IOException {
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj=ExcelUtil.readExcel("C:\\Resource.xlsx", true);
        Iterator<Entry<String, String>> it = testObj.entrySet().iterator();
		  while(it.hasNext()){
              Entry<String, String> entry = it.next();
			try {
				this.createResource(entry.getKey(),entry.getValue());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				continue;
			}

	 }

	}
	
	public String getOrganization(String code) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/inventoryOrganizations?q=OrganizationCode="+code;
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		System.out.print(otherjb.toString());
		return otherjb.getString("OrganizationId");
//		return otherjb.getString("OrganizationId");
		
	}
	
	@Test
	public void getAllDescpritons() throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		Map<String,Object> map = this.GetOneTest(Double.parseDouble("93657540.6"));
		if(CollectionUtil.isNotEmpty(map)){
			String one =  map.get("items").toString();
			String oneStr = one.substring(1, one.length()-1);
			Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
		
		String WorkOrderId = oneMap.get("WorkOrderId").toString();
		
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/"+WorkOrderId+"/child/WorkOrderDFF";
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		System.out.print(otherjb.toString());
//		return otherjb.getString("OrganizationId");
		
	}
	}
	
	@Test
	public void createStandardOperations() throws Exception{
		List<HashMap<String, String>> testObj = new ArrayList<HashMap<String, String>>();
		testObj=ExcelUtil.readALLExcel(path, 0);
		for(HashMap<String, String> map:testObj){
				 this.createStandardOperation(map);
				  
		
		}
	}
	@Test
	public void addResourceToCenter() throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String center = this.getWorkCenter(EnumUtil.getByCode(factory.getCode(), CenterEnum.class).getMessage());
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/workCenters/"+center+"/child/WorkCenterResource";
		HashMap<String, Object> testObj = new HashMap<String, Object>();
		HashMap<String, String> map = new HashMap<String, String>();
		map=ExcelUtil.readExcel("C:\\Resource.xlsx", true);
        Iterator<Entry<String, String>> it = map.entrySet().iterator();
		  while(it.hasNext()){
			  Entry<String, String> entry = it.next();
			  testObj.put("ResourceQuantity", 10);
			  testObj.put("ResourceId", this.getResource(entry.getKey()));
			  testObj.put("EfficiencyPercentage", "100");
			  testObj.put("UtilizationPercentage", "100");
		  
		
		
//		testObj.put("StandardOperationId", "StandardOperationId");
//		testObj.put("ResourceName", name);
		
		JSONObject jsonObj = new JSONObject(testObj);
		try{
		String workOrder = frUtil.post(url,jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		System.out.print(otherjb.toString());// TODO Auto-generated method stub
		}catch(Exception e){}
		  }
				  
		
		}
		
		
        


	private void createResourceS(String StandardOperationId,String ResourceCode,int flag) throws Exception{	
	FusionRestUtil frUtil = new FusionRestUtil()  ;
	String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/standardOperations/"+StandardOperationId+"/child/StandardOperationResource";
	HashMap<String, Object> testObj = new HashMap<String, Object>();
//	testObj.put("OrganizationId", "300000002270325");
	
	testObj.put("ResourceSequenceNumber", flag);
	testObj.put("ResourceId", this.getResource(ResourceCode));
	testObj.put("PrincipalFlag", "N");
	
//	testObj.put("StandardOperationId", "StandardOperationId");
//	testObj.put("ResourceName", name);
	
	JSONObject jsonObj = new JSONObject(testObj);
	String workOrder = frUtil.post(url,jsonObj.toString(),deaultUserName,deaultPassword);
	com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
	System.out.print(otherjb.toString());// TODO Auto-generated method stub
		
	}

	public Map<String,Object> createStandardOperation(Map<String,String> map) throws Exception {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/standardOperations";
		HashMap<String, String> testObj = new HashMap<String, String>();
//		String ogzId = this.getOrganization("300000002270290");
		testObj.put("OrganizationId", organizationId);
		testObj.put("OperationType", "IN_HOUSE");
		testObj.put("StandardOperationName", map.get("StandardOperationDescription"));
//		testObj.put("DefaultforAutomaticWorkDefinition", "Y");
		testObj.put("StandardOperationCode",map.get("StandardOperationCode"));
		testObj.put("StandardOperationDescription", map.get("StandardOperationDescription"));
		testObj.put("CountPointOperationFlag", "Y");
//		testObj.put("WorkAreaCode", map.get("WorkAreaCode"));
		testObj.put("WorkCenterId",this.getWorkCenter(map.get("WorkCenterCode")));
//		testObj.put("WorkCenterCode",map.get("WorkCenterCode"));
		JSONObject jsonObj = new JSONObject(testObj);
		String workOrder;
		Map<String, Object> itemMap=null;
		try {
			workOrder = frUtil.post(url,jsonObj.toString(),deaultUserName,deaultPassword);
			com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		 itemMap = com.alibaba.fastjson.JSONObject.toJavaObject(otherjb, Map.class);
		 String tool = map.get("tool");
		 String[] tools = StringUtil.split(tool, '|');
		 this.createResourceS(itemMap.get("StandardOperationId").toString(),"Operator",10);
		 for(String t:tools){
			 String code = ToolEnum.getByMessage(t).getCode();
				this.createResourceS(itemMap.get("StandardOperationId").toString(),code,20);

		 }
			System.out.print(otherjb.toString());
			return itemMap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itemMap;
		
	}
	
	
	public String getOneWorkCenter(String workCenterName,String OrganizationId) throws Exception{
//		String workCenterName="信息部",OrganizationId="300000006715159";
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url=fusionRest+"/fscmRestApi/resources/11.13.18.05/workCenters?q=WorkCenterName=" + workCenterName + ";OrganizationId=" + OrganizationId;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<Map> list = this.getFusionListFromOjbect(reponseStr,Map.class);
		if(CollectionUtil.isEmpty(list)){
			log.info("未查询到工作中心{}，组织名称为{}，的工作中心编码",workCenterName,OrganizationId);
			throw new Exception("未查询到工作中心"+workCenterName+"，组织为"+OrganizationId+"，的工作中心编码");
		}
		return list.get(0).get("WorkCenterId").toString();
	}
	
	@Test
	public void getOneItemByOrgIdAndItemID() throws Exception{
		//TODO 如果是固定的话，就去掉
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String organizationId="300000006715159";
		String itemNumber = "生产设备";
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/items?fields=ItemId,OrganizationId&q=ItemNumber=" + itemNumber  +";OrganizationId=" + organizationId
			;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		List<Map> assetList = this.getFusionListFromOjbect(reponseStr,Map.class);
		System.out.print(assetList.get(0).toString());
		}
	
	
	@Test
	public void getStandardOperation() throws Exception{
		String operationCode = "维修";
		String url = fusionRest+":443/fscmRestApi/resources/11.13.18.05/standardOperations?q=StandardOperationCode="+operationCode;
		FusionRestUtil frUtil = new FusionRestUtil();
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		String one =  otherjb.get("items").toString();
		String oneStr = one.substring(1, one.length()-1);
		Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
//		return oneMap.get("StandardOperationId").toString();

	}
	
	public String getWorkCenter(String centerCode) throws Exception{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/workCenters?q=WorkCenterCode="+centerCode;
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(workOrder);
		String one =  otherjb.get("items").toString();
		String oneStr = one.substring(1, one.length()-1);
		Map<String, Object> oneMap = com.alibaba.fastjson.JSONObject.parseObject(oneStr, Map.class);
		return oneMap.get("WorkCenterId").toString();
	}
	
	public Asset createOneAsset(Asset asset) throws Exception{
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		//1.获取ItemID和组织ID TODO 固定值
		//Map map=this.getOneItemByOrgIdAndItemID(asset.getItemNumber(), asset.getOrganizationName());//生产设备,SHJC2
		asset.setItemId("300000003746075");//设备的Item
		asset.setLocationOrganizationId(OrgEnum.FYGROUP.getMessage());//TODO 福耀浮法集团
		//2.获取工作中心ID TODO 是否要做集成配置，或者动态存储  TODO 固定值
		String centerID = this.getOneWorkCenter(asset.getWorkCenterName(), asset.getLocationOrganizationId());
		asset.setWorkCenterId(centerID);//TODO 信息部的 centerID
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
		String reponseStr = null;
		try{
		 reponseStr = frUtil.post(url,jsonObj.toString(),deaultUserName,deaultPassword);
		 List<Asset> assetList = this.getFusionListFromOjbect(reponseStr,Asset.class);
		 if(CollectionUtil.isEmpty(assetList)){
				return asset;
			}
			String assetId =assetList.get(0).getAssetId() ;
			asset.setAssetId(assetId);
		}catch(Exception e){
			Asset asset2=this.getOneAssetByAssetNum(asset.getAssetNumber());
			String assetId =asset2.getAssetId() ;
			asset.setAssetId(assetId);
		}
		
		//2.创建弹性域字段
		
		this.createAssetDescriptiveField2(asset);
		return asset;
		}
	
	public void createAssetDescriptiveField2(Asset asset) throws Exception{
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
//		 otherObjs.put("oabillinum",asset.getOABillINum());//OA移交单据号

		JSONObject jsonObj = new JSONObject(otherObjs);
		String	otherworkresult = frUtil.post(assetUrl, jsonObj.toString(),deaultUserName,deaultPassword);
		log.debug(otherworkresult);
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
		 otherObjs.put("jobnum",asset.getJobnum());//工号
		 otherObjs.put("assetmodel",asset.getAssetmodel());//型号
		 otherObjs.put("allocation",asset.getAllocation());//基本配置
		 if(asset.getUsingstarttime()!=null)
			 otherObjs.put("usingstarttime",formatter.format(asset.getUsingstarttime()));//启用时闫
		 if(asset.getWarrantdate()!=null)
			 otherObjs.put("warrantydate",formatter.format(asset.getWarrantdate()));//启用时闫
		 if(asset.getWarrantyreminderdate()!=null)
			 otherObjs.put("warrantyreminderdate",formatter.format(asset.getWarrantyreminderdate()));//启用时闫
		 otherObjs.put("remark",asset.getRemark());//备注
		 otherObjs.put("manufacturer",asset.getManufacturer());//生产厂商售厂商
		 otherObjs.put("softwarestatus",asset.getSoftwarestatus());//保修期
		 otherObjs.put("amount",asset.getAmount());//OA总计
		 otherObjs.put("source",asset.getSource());//状态
		 otherObjs.put("remark",asset.getRemark());//状态
//		 otherObjs.put("contractid",asset.getContractId());//状态

		JSONObject jsonObj = new JSONObject(otherObjs);
		String	otherworkresult = frUtil.post(assetUrl, jsonObj.toString(),deaultUserName,deaultPassword);
		log.debug(otherworkresult);
	}
	@Test
	public void getWorkorders() throws Exception {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders?q=WorkOrderTypeCode=PREVENTIVE;WorkOrderStatusCode=ORA_UNRELEASED;PlannedStartDate>2018-06-05";
		String workOrder = frUtil.get(url,deaultUserName,deaultPassword);// PlannedStartDate=2019-06-05T00:00:00+00:00"=
		List<WorkOrder> list = getFusionListFromOjbect(workOrder,WorkOrder.class);
		
		if(list.size()>0){
			System.out.print(list.toString());
		}
	}
	
	@Test
	public void createAssets() throws Exception {
		//TODO 读取文件
		List<HashMap<String, String>> testObj = new ArrayList<HashMap<String, String>>();
		testObj=ExcelUtil.readALLExcel(assetPath, 0);
		for(HashMap<String, String> map:testObj){
			com.alibaba.fastjson.JSONObject jo = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(map);
			Asset asset = this.createAssetFromMap(map);
//			List<Asset> asset = this.getFusionListFromOjbect(jo.toJSONString(),Asset.class);
			try{
				 this.createOneAsset(asset);
				 asset.setAssetType(AssetTypeEnum.CMP_ASSET.getCode());
				 assetService.createOne(asset);
			}catch(Exception e){
				System.out.print(e.toString());
			}
		
		}
	}
	
	@Test
	public void createSoftWares() throws Exception {
		//TODO 读取文件
		List<HashMap<String, String>> testObj = new ArrayList<HashMap<String, String>>();
		testObj=ExcelUtil.readALLExcel(softWarePath, 0);
		for(HashMap<String, String> map:testObj){
			com.alibaba.fastjson.JSONObject jo = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.toJSON(map);
			Asset asset = this.createAssetFromMap(map);
//			List<Asset> asset = this.getFusionListFromOjbect(jo.toJSONString(),Asset.class);
			try{
				 this.createOneAsset(asset);
				 asset.setAssetType(AssetTypeEnum.CMP__SOFT_ASSET.getCode());
				 assetService.createOne(asset);
			}catch(Exception e){
				System.out.print(e.toString());
			}
		
		}
	}
	
	private Asset createAssetFromMap(HashMap<String, String> map) throws ParseException {
		Asset asset = new Asset();
		asset.setAssetNumber(map.get("AssetNumber"));
		asset.setOrganizationName(map.get("OrganizationName"));
		if(map.get("usingstarttime")!="")
			asset.setUsingstarttime(myFormatter.parse(map.get("usingstarttime")));
		asset.setAllocation(map.get("allocation"));
		if(map.get("amount")!=null&&map.get("amount")!="")
			asset.setAmount(new BigDecimal(map.get("amount")));
		asset.setDescription(map.get("Description"));
		asset.setJobnum(map.get("jobnum"));
		asset.setRemark(map.get("remark"));
		if(map.get("source")!=null&&map.get("source")!="")
			asset.setSource(Integer.parseInt(map.get("source")));
		if(map.get("source")!=null&&map.get("softwarestatus")!="")
			asset.setSoftwarestatus(Integer.parseInt(map.get("softwarestatus")));
		asset.setAssetNumber(map.get("AssetNumber"));
		asset.setAssetmodel(map.get("assetmodel"));
		if(map.get("warrantdate")!=null){
			asset.setWarrantdate(myFormatter.parse(map.get("warrantdate")));
		asset.setWarrantyreminderdate(DateUtils.addMonths(asset.getWarrantdate(), 3));
		}
		asset.setManufacturer(map.get("manufacturer"));
		if(map.get("suite")!=null)
			asset.setSuite(Integer.parseInt(map.get("suite")));
		if(map.get("SerialNumber")!=null)
			asset.setSerialNumber(map.get("SerialNumber"));
		if(map.get("softType")!=null){
			asset.setSoftType(Integer.parseInt(map.get("softType")));
		}
		if(map.get("WorkCenterId")!=null){
			asset.setWorkCenterName(map.get("WorkCenterId"));
		}
		asset.setUsername(map.get("username"));
		asset.setContractId(map.get("contractId"));
		asset.setRemark(map.get("remark"));
		return asset;
	}

	private void createAsset(HashMap<String, String> map,String WorkOrderId) throws Exception {
		FusionRestUtil frUtil = new FusionRestUtil()  ;
//		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders";
		String url =fusionRest+ "/fscmRestApi/resources/11.13.18.05/maintenanceWorkOrders/";
		HashMap<String, String> testObj = new HashMap<String, String>();
		testObj.put("StandardOperationId", "300000003503079");
		testObj.put("OperationSequenceNumber", "10");
		JSONObject jsonObj = new JSONObject(testObj);
		String	otherworkresult = frUtil.post(url, jsonObj.toString(),deaultUserName,deaultPassword);
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(otherworkresult);
		

		
	}

	public  <T> List<T> getFusionListFromOjbect3(String reponseStr,Class<T> object){
		List<T> list = new ArrayList<T>();  
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(reponseStr);
		list =JSONArray.parseArray(otherjb.get("items").toString(),object);
		return list;
	}
	
	public  <T> List<T> getFusionListFromOjbect2(String reponseStr,Class<T> object){
		List<T> list = new ArrayList<T>();  
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(reponseStr);
		list =JSONArray.parseArray(otherjb.toString(),object);
		return list;
	}
	
	public  <T> List<T> getFusionListFromOjbect(String reponseStr,Class<T> object){
		List<T> list = new ArrayList<T>();  
		String result=reponseStr;
		com.alibaba.fastjson.JSONObject otherjb = com.alibaba.fastjson.JSONObject.parseObject(result);
		if(otherjb.get("items")!=null){
		list =JSONArray.parseArray(otherjb.get("items").toString(),object);
		}else{
//			result=result.substring(1);
//			result=result.subSequence(1, result.length()-1).toString();
			otherjb  = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(result);
//			list = com.alibaba.fastjson.JSONArray.parseArray(otherjb.toString(), object);
			list =JSONArray.parseArray('['+otherjb.toString()+']',object);
		}
		return list;
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
	@Test
	public void getOneAssetByAssetNum() {
		String AssetNumber ="BTTTLP2";
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		List<Asset> assetList = null;
		try{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets?q=SerialNumber="+AssetNumber;
		String reponseStr = frUtil.get(url,deaultUserName,deaultPassword);
		assetList = this.getFusionListFromOjbect(reponseStr,Asset.class);
		}catch(Exception e){
			log.error("Fusion获取资产失败，原因："+e.getMessage());
//			return null;
		}
//		return assetList.get(0);
	}
	@Test
	public void updateOneAsset(){
		FusionRestUtil frUtil = new FusionRestUtil()  ;
		List<Asset> assetList = null;
		try{
		String url = fusionRest+"/fscmRestApi/resources/11.13.18.05/maintenanceAssets/"+"300000006577389";
		//2.获取工作中心ID TODO 是否要做集成配置，或者动态存储
		
		HashMap<String, Object> otherObjs = new HashMap<String, Object>();
		otherObjs.put("AssetNumber","G-NB932");//保修期
		 otherObjs.put("SerialNumber","5CD908130S");//OA总计
		 otherObjs.put("Description","GNB932");//状态
		 otherObjs.put("WorkCenterName","财务部");//OA移交单据号
		 otherObjs.put("ItemOrganizationId", "300000009431661");
		 otherObjs.put("OrganizationCode", "FY_FFJT");
		JSONObject jsonObj = new JSONObject(otherObjs);
	
		String reponseStr = frUtil.patch(url,jsonObj.toString(),deaultUserName,deaultPassword);
		assetList = this.getFusionListFromOjbect(reponseStr,Asset.class);
		}catch(Exception e){
			log.error("Fusion获取资产失败，原因："+e.getMessage());
			
		}
		
	}
		
}
