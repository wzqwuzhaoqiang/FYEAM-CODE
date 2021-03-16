package com.fuyaogroup.eam.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.util.StringUtil;

import com.fuyaogroup.eam.common.enums.OAOrgEnum;
import com.fuyaogroup.eam.common.service.qtfwWeixinMessageService;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.util.EnumUtil;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
import com.fuyaogroup.eam.util.GroupRecordTool;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.message.impl.MsgObject;
import com.soa.eis.adapter.framework.requester.IServiceRequester;
import com.soa.eis.adapter.framework.requester.impl.BaseServiceRequester;
import com.soa.eis.adapter.framework.utils.log.LogUtil;

@RestController
@Slf4j
@ConfigurationProperties
@Component
public class AddAssetController {
	
	
//	//设置参数
	@Value("${activemq_url}")
	private  String activemq_url;
	
//	@Value("${activemq_username}")
//	private  String username;
//	
//	@Value("${activemq_password}")
//	private  String password;
//	
	private static Integer requestNo = 0;
	
	@Autowired
	AssetPdService assetPdService;
	
	@Autowired
	AssetPdBatService assetPdBat;
	
	@Autowired
	qtfwWeixinMessageService qtfwService;
	
	@Autowired
	private FusionEAMAPIUtil fuEAMUtil;
	
	@Autowired
	private AssetService assetService;
	
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	//测试
	private static  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final String SYNCSALESDATA_SERVICEID = "02012000000001";
	
	public  IMsgObject addAsset(IMsgObject reqMo) throws Exception{
		log.info("{}:增加固定资产,开始...",LocalDateTime.now());
		if (reqMo == null)
			return null;
		try {
			//解析reqMo
			log.info("验收开始-------------------------");
			Asset asset = this.getAssetFromIMsgObj(reqMo);
//			Asset asset = new Asset();
//			asset.setAssetNumber("ZB56500009");
//			asset.setWarrantyperiod(2);
//			asset.setAssetmodel("惠普");
//			asset.setJobnum("024556");
//			asset.setUsername("陈小龙");
//			asset.setAmount(new BigDecimal(6800));
//			asset.setOABillINum("FI202005022OA");
			if (asset != null) {
				log.info("asset的数据为---------------------"+asset.toString());
				//asset = fuEAMUtil.createOneAsset(asset);
				log.info("经过fuEAMUtil接口的方法后asset的数据为---------------------"+asset.toString());
				assetService.createOne(asset);//数据库增加，一条资产
				Date nowDate  = myFormatter.parse(myFormatter.format(new Date()));
				List<AssetPdBat> nowTimeList = assetPdBat.getAllBDate(nowDate);
				if(!CollectionUtils.isEmpty(nowTimeList)) {
					for(AssetPdBat apd :nowTimeList) {
						if(apd.getOrgList().contains(asset.getOrganizationName())) {
							//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
							List<Asset> alist = new ArrayList<Asset>();
							alist.add(asset);
							assetPdService.createAllAssetPd(Long.parseLong(apd.getPdBatId()),alist );
						}
				}
				}
				
				List<GroupRecord> grs = new ArrayList<GroupRecord>();
				grs.add(GroupRecordTool.convertToGroupRecord(asset));
				reqMo.setResGroupRecord(grs);
				log.info("{}:增加固定资产,结束...",LocalDateTime.now());
				return reqMo;
			}
		} catch (Exception e) {
			qtfwService.send("101798", "", "资产新增出现异常了，赶紧滚去处理.......");
			log.error("增加固定资产,失败,{}",e.getMessage());
			log.info(e.getMessage());
			e.printStackTrace();
		}
		log.info("{}:增加固定资产,结束...",LocalDateTime.now());
		return null;
		
	//执行请求
	
	}
	
	public  IMsgObject addsoftWareAsset(IMsgObject reqMo) throws Exception{
		log.info("{}:增加软件资产,开始...",LocalDateTime.now());
		if (reqMo == null)
			return null;
		try {
			//解析reqMo
			Asset asset = this.getSoftAssetFromIMsgObj(reqMo);
			if (fuEAMUtil != null) {
				//asset = fuEAMUtil.createOneAsset(asset);
				List<GroupRecord> grs = new ArrayList<GroupRecord>();
				grs.add(GroupRecordTool.convertToGroupRecord(asset));
				reqMo.setResGroupRecord(grs);
				log.info("{}:增加软件资产,结束...",LocalDateTime.now());
				return reqMo;
			}
		} catch (Exception e) {
			log.error("增加固定资产,失败,{}",e.getMessage());
			e.printStackTrace();
		}
		log.info("{}:增加固定资产,结束...",LocalDateTime.now());
		return null;
		
	//执行请求
	
	}
	
	
	private Asset getAssetFromIMsgObj(IMsgObject reqMo) throws ParseException {
		log.info("getAssetFromIMsgObj方法验收开始-------------------------");
		Asset asset = new Asset();
		//TODO 需要进行转换 组织名称->组织编码
		log.info("reqMo.getReqGroupRecord(\"Asset\")方法验收开始-------------------------");
		List<GroupRecord> gr = reqMo.getReqGroupRecord("Asset");
		log.info("136");
		GroupRecord reqRecord = gr.get(0);
		log.info("137");
		asset.setOrganizationName(EnumUtil.getByCode(reqRecord.getFieldValue("cmpCode"), OAOrgEnum.class).getMessage());
		log.info("138");
		asset.setWorkCenterName(reqRecord.getFieldValue("WorkCenterName"));
		log.info("139");
		asset.setItemNumber("生产设备");
		log.info("140");
		asset.setDescription(reqRecord.getFieldValue("Description"));
		log.info("141");
		asset.setFinancialCode(reqRecord.getFieldValue("FinancialCode"));
		log.info("142");
		asset.setJobnum(reqRecord.getFieldValue("jobNum"));
		log.info("143");
		asset.setDescription(reqRecord.getFieldValue("Descriptions"));
		log.info("144");
		//机型1:DELL笔记本;2:DELL移动工作站;3:笔记本;4:笔记本工作站;5:台式工作站;6:DELL移动工作站;7:台式机;8:显示器;9:移动工作站;10:其他;
		asset.setHtcIncredible(Integer.parseInt(reqRecord.getFieldValue("htcIncredible")));
		log.info("145");
		asset.setAssetmodel(reqRecord.getFieldValue("assetModel"));
		log.info("146");
		asset.setSerialNumber(reqRecord.getFieldValue("SerialNumber"));
		log.info("中间位置方法验收开始-------------------------");
		asset.setAssetNumber(reqRecord.getFieldValue("AssetNumber"));
		asset.setAllocation(reqRecord.getFieldValue("allocation"));
		asset.setDisplayer(reqRecord.getFieldValue("displayer"));
		asset.setMacaddress(reqRecord.getFieldValue("macAddress"));
		asset.setServiceid(reqRecord.getFieldValue("serviceId"));
		asset.setMouse(reqRecord.getFieldValue("mouse"));
		asset.setWifimac(reqRecord.getFieldValue("wifiMac"));
		asset.setKeyboard(reqRecord.getFieldValue("keyBoard"));
		asset.setPoweradapt(reqRecord.getFieldValue("powerAdapt"));
		asset.setUsername(reqRecord.getFieldValue("userName"));
		//TODO 设置传入的日期格式YYYY-MM-DD
		asset.setUsingstarttime(myFormatter.parse(reqRecord.getFieldValue("usingStartTime")));
		asset.setRemark(reqRecord.getFieldValue("remark"));
		asset.setManufacturer(reqRecord.getFieldValue("manufacturer"));
		//保修期 按月
		log.info("数据装换前方法验收开始-------------------------");
		asset.setWarrantyperiod(Integer.parseInt(reqRecord.getFieldValue("warrantyPeriod")));
		
		asset.setAmount(new BigDecimal(reqRecord.getFieldValue("amount")));
		
		//1：在用；2:闲置;3:待报废;4:报废;5:调设备使用;6.过期
		asset.setStatus(Integer.parseInt(reqRecord.getFieldValue("status")));//reqRecord.getFieldValue("status")
		log.info("装换后方法验收开始-------------------------");
		return asset;
	}
	
	private Asset getSoftAssetFromIMsgObj(IMsgObject reqMo) throws ParseException {
		Asset asset = new Asset();
		//TODO 需要进行转换 组织名称->组织编码
		List<GroupRecord> gr = reqMo.getReqGroupRecord("Asset");
		GroupRecord reqRecord = gr.get(0);
		asset.setOrganizationName(reqRecord.getFieldValue("OrganizationName"));
		asset.setWorkCenterName(reqRecord.getFieldValue("WorkCenterName"));
		asset.setItemNumber("生产设备");
		asset.setDescription(reqRecord.getFieldValue("Description"));
		asset.setFinancialCode(reqRecord.getFieldValue("FinancialCode"));
		asset.setJobnum(reqRecord.getFieldValue("jobNum"));
		asset.setDescription(reqRecord.getFieldValue("Descriptions"));
		//机型1:DELL笔记本;2:DELL移动工作站;3:笔记本;4:笔记本工作站;5:台式工作站;6:DELL移动工作站;7:台式机;8:显示器;9:移动工作站;10:其他;
		asset.setHtcIncredible(Integer.parseInt(reqRecord.getFieldValue("htcIncredible")));
		asset.setAssetmodel(reqRecord.getFieldValue("assetModel"));
		asset.setSerialNumber(reqRecord.getFieldValue("SerialNumber"));
		asset.setAssetNumber(reqRecord.getFieldValue("AssetNumber"));
		asset.setAllocation(reqRecord.getFieldValue("allocation"));
		asset.setDisplayer(reqRecord.getFieldValue("displayer"));
		asset.setMacaddress(reqRecord.getFieldValue("macAddress"));
		asset.setServiceid(reqRecord.getFieldValue("serviceId"));
		asset.setMouse(reqRecord.getFieldValue("mouse"));
		asset.setWifimac(reqRecord.getFieldValue("wifiMac"));
		asset.setKeyboard(reqRecord.getFieldValue("keyBoard"));
		asset.setPoweradapt(reqRecord.getFieldValue("powerAdapt"));
		asset.setUsername(reqRecord.getFieldValue("userName"));
		//TODO 设置传入的日期格式YYYY-MM-DD
		asset.setUsingstarttime(simpleDateFormat.parse(reqRecord.getFieldValue("usingStartTime")+" 00:00:00"));
		asset.setRemark(reqRecord.getFieldValue("remark"));
		asset.setManufacturer(reqRecord.getFieldValue("manufacturer"));
		//保修期 按月
		asset.setWarrantyperiod(Integer.parseInt(reqRecord.getFieldValue("warrantyPeriod")));
		asset.setAmount(new BigDecimal(reqRecord.getFieldValue("amount")));
		//1：在用；2:闲置;3:待报废;4:报废;5:调设备使用;6.过期
		asset.setStatus(Integer.parseInt(reqRecord.getFieldValue("status")));

		return asset;
	}

	@Deprecated
	private static IMsgObject executeReQuest(IMsgObject reqMo) throws Exception {
		IServiceRequester requester = BaseServiceRequester.getInstance();
		return requester.execute(reqMo);
	}
	
	@Deprecated
	public static void main(String[] args) throws Exception {
		 //1.设置
			String currentDateStr = simpleDateFormat.format(new Date());
			IMsgObject reqMo = new MsgObject();
			// 对请求报文对象进行设置
			String sn = String.format("%06d", ++requestNo);
			try{
			reqMo.setSourceSysID("06002"); // 设置报文 请求方系统号
			reqMo.setServiceID(SYNCSALESDATA_SERVICEID); // 设置报文 提供方服务号
			reqMo.setSerialNO(currentDateStr.substring(0, 8) + requestNo + sn);// 设置报文流水号,第三段表示今天发送的第几的一次报文
			reqMo.setServiceDateTime(currentDateStr); // 设置报文 日期时间
			// 设置请求报文数据
			List<Object> dataList=new ArrayList<Object>();
			List<Asset> list = new ArrayList<Asset>();
			Asset his = new Asset();
			his.setOrganizationCode("123455");
			his.setAssetmodel("HP 440 G5");
			his.setFinancialCode("12345677");
			his.setJobnum("2345677");
			his.setDescription("GNB932");
			his.setAssetNumber("G-NB932");
			his.setHtcIncredible(4);
			his.setSerialNumber("5CD908130S");
			his.setAllocation("I5-8G-256G-500G");
			his.setDisplayer("CNK8390DRH");
			his.setMacaddress("B0-0C-D1-61-DD-6B");
			his.setServiceid("11289638414");
			his.setMouse("MPMMFB180145646");
			his.setWifimac("D8-F2-CA-2A-38-39");
			his.setKeyboard("BGCWR0AVBBM0E4");
			his.setPoweradapt("WGECD0BNJBS1LH");
			//TODO 设置传入的日期格式YYYY-MM-DD
			his.setUsingstarttime(new Date());
			his.setWorkCenterName("财务部");
			his.setRemark("");
			his.setManufacturer("DELL");
			//保修期 按月
			his.setWarrantyperiod(24);
			his.setAmount(new BigDecimal(5000));
			//1：在用；2:闲置;3:待报废;4:报废;5:调设备使用;6.过期
			his.setStatus(1);
			
			list.add(his);
			list.add(his);
			dataList.addAll(list);
			GroupRecord groupRecord =GroupRecordTool.convertToGroupRecord(his);
			GroupRecord groupRecord1 =GroupRecordTool.convertToGroupRecord(his);
			List<GroupRecord> groupRecords = new ArrayList<GroupRecord>();
			groupRecords.add(groupRecord);
			groupRecords.add(groupRecord1);
			reqMo.setReqGroupRecord(groupRecords);
			reqMo.setControlValue("Username", "guest");
			reqMo.setControlValue("Password", "abc123");
			//打印报文
			LogUtil.getInstance().info(reqMo);
			//执行请求
			IMsgObject resMo = executeReQuest(reqMo);
			//打印报文
			Element rootElement = resMo.getRoute();
			Element serviceResponseElement = rootElement.element("ServiceResponse");
			String status = serviceResponseElement.elementText("Status");
			if(StringUtil.isNotEmpty(status) && status.equals("COMPLETE")){
				List<Integer> completeIds = new ArrayList<Integer>();
				List<Integer> errorIds = new ArrayList<Integer>();
				String errMsg = resMo.getResValue("ErrMsg");
			}else{
				String desc = serviceResponseElement.elementText("Desc");
				throw new Exception("ServiceResponse\nStatus:"+status+"\nDesc:"+desc);
			}
			}catch(Throwable  e){
				System.out.print(e.toString());
			}
	   }
}
