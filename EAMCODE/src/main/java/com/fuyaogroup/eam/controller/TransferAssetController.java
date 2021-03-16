package com.fuyaogroup.eam.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetErrorCord;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.model.AssetTransfer;
import com.fuyaogroup.eam.modules.fusion.service.AssetErrorCordService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.AssetTransferService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
import com.fuyaogroup.eam.util.GroupRecordTool;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;
import com.soa.eis.adapter.framework.requester.IServiceRequester;
import com.soa.eis.adapter.framework.requester.impl.BaseServiceRequester;

@RestController
@Slf4j
public class TransferAssetController {
	
	@Autowired
	AssetTransferService assetTransferSevice;
	
	@Autowired
	AssetService assetSevice;
	
	@Autowired
	AssetPdService assetPdService;
	
	@Autowired
	AssetErrorCordService aecService;
	
	@Autowired
	AssetPdBatService assetPdBat;
	
	@Autowired
	private FusionEAMAPIUtil fuEAMUtil=new FusionEAMAPIUtil();
	
	
	//测试
	private static  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	private static  SimpleDateFormat simpleDateFormat1=new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
	
	public static final String SYNCSALESDATA_SERVICEID = "01002000000001";
	
	public  IMsgObject transAsset(IMsgObject reqMo) throws Exception{
		log.info("{}:转移固定资产,开始...",LocalDateTime.now());
		if (reqMo == null)
			return null;
		try {
			//解析reqMo
			List<GroupRecord> gr = reqMo.getReqGroupRecord("Asset");
			GroupRecord reqRecord = gr.get(0);
			String assetSerNum = reqRecord.getFieldValue("SerialNumber");
			//Asset asset = fuEAMUtil.getOneAssetBySerNum(assetSerNum);
			Asset asset =null;
			if(assetSerNum==null||"".equals(assetSerNum)) {
				throw new Exception("异常：资产序列号参数为空！");
			}
			List<Asset> assetList = assetSevice.getBySerialNumber(assetSerNum);
			if(assetList!=null&&assetList.size()>0) {
				asset = assetList.get(0);
			}
			if(asset==null){
				String assetNumber = reqRecord.getFieldValue("AssetNumber");
				assetList = assetSevice.getByAssetNumber(assetNumber);
				if(assetList!=null&&assetList.size()>0) {
					asset = assetList.get(0);
				}else {
					throw new Exception("异常：没有此资产信息！");
				}
				
			}
			asset.setJobnum(reqRecord.getFieldValue("jobNum"));
			asset.setOABillINum(reqRecord.getFieldValue("OABillINum"));
			asset.setHandoverCpt(reqRecord.getFieldValue("handoverCpt"));
			asset.setHandoverPerson(reqRecord.getFieldValue("handoverPerson"));//需要工号
			asset.setHandoverTime(simpleDateFormat.parse(reqRecord.getFieldValue("handoverTime")));
			asset.setUsername(reqRecord.getFieldValue("userName"));
			asset.setWorkCenterName(reqRecord.getFieldValue("WorkCenterName"));
			asset.setOrganizationName(reqRecord.getFieldValue("handoverCpt"));
			
			AssetTransfer assettf = new AssetTransfer();
			assettf.setJobNum(reqRecord.getFieldValue("jobNum"));
			assettf.setOABillNum(reqRecord.getFieldValue("OABillINum"));
			assettf.setHandoverCpt(reqRecord.getFieldValue("handoverCpt"));
			assettf.setAssetNumber(reqRecord.getFieldValue("AssetNumber"));
			assettf.setUserName(reqRecord.getFieldValue("userName"));
			assettf.setSerialNumber(assetSerNum);
			assettf.setHandoverPerson(reqRecord.getFieldValue("handoverPerson"));//需要工号
			assettf.setHandoverTime(simpleDateFormat.parse(reqRecord.getFieldValue("handoverTime")));
			asset.setHandoverTime(simpleDateFormat.parse(reqRecord.getFieldValue("handoverTime")));
			//if (fuEAMUtil != null) {
				//asset = fuEAMUtil.updateAssetDescriptiveFields(asset,true);
				assetSevice.updateOne(asset);
				assetTransferSevice.createOne(assettf);
//				List<AssetPd> pdList = assetPdService.getBySerialNum(asset.getSerialNumber(), PdStatusEnum.ASSET_PD_WAITING.getCode());
//				if(!CollectionUtil.isEmpty(pdList)){
//				assetPdService.updateAssetPd(pdList.get(0));
//				}
				List<GroupRecord> grs = new ArrayList<GroupRecord>();
				grs.add(GroupRecordTool.convertToGroupRecord(asset));
				reqMo.setResGroupRecord(grs);
				
				
				Date nowDate  = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
				List<AssetPdBat> nowTimeList = assetPdBat.getAllBDate(nowDate);
				if(!CollectionUtils.isEmpty(nowTimeList)) {
					for(AssetPdBat apd :nowTimeList) {
						AssetPd pd = assetPdService.getBySerialNumAndBatid(asset.getSerialNumber(),apd.getPdBatId());
						if(pd!=null) {
							if(pd.getOrganizationName().equals(asset.getOrganizationName())) {
								//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
								pd = this.createPd(asset,pd);
								assetPdService.updateAssetPd(pd);
							}else {
								assetPdService.deleteAssetPd(pd.getPdCode().toString());
								//盘点里的组织不等于更改后的组织，所以要删除此条盘点信息
							}
						}
					}
					for(AssetPdBat apd :nowTimeList) {
							if(apd.getOrgList().contains(asset.getOrganizationName())) {
								//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
								
								AssetPd pd = assetPdService.getBySerialNumAndBatid(asset.getSerialNumber(),apd.getPdBatId());
								if(pd==null) {
									List<Asset> lista =  new ArrayList<Asset>();
									lista.add(asset);
									assetPdService.createAllAssetPd(Long.parseLong(apd.getPdBatId()),lista );
								}
								
							}
					}
				
				}
				
				
				log.info("{}:变更固定资产,结束...",LocalDateTime.now());
				return reqMo;
			//}
		} catch (Exception e) {
			log.info("固定资产转移失败......................记录表开始工作" +e.getMessage());
			List<GroupRecord> gr = reqMo.getReqGroupRecord("Asset");
			GroupRecord reqRecord = gr.get(0);
			String assetSerNum = reqRecord.getFieldValue("SerialNumber");
			//Asset asset = fuEAMUtil.getOneAssetBySerNum(assetSerNum);
//			Asset asset =null;
//			List<Asset> assetList = assetSevice.getBySerialNumber(assetSerNum);
//			if(assetList!=null&&assetList.size()>0) {
//				asset = assetList.get(0);
//			}
			AssetErrorCord aec = new AssetErrorCord();
			aec.setOabilliNum(reqRecord.getFieldValue("OABillINum"));
			aec.setTranMan(reqRecord.getFieldValue("userName"));
			//aec.setTranid(asset.getJobnum());
			aec.setReceiveid(reqRecord.getFieldValue("jobNum"));
			aec.setReceiveMan(reqRecord.getFieldValue("handoverPerson"));
			aec.setAssetName(reqRecord.getFieldValue("AssetNumber"));
			aec.setSerial(assetSerNum);
			aec.setMyid(UUID.randomUUID().toString().substring(0, 12));
			aec.setCreateDate(new Date());
			aecService.addOne(aec);
			log.error("变更固定资产,失败,{}",e.getMessage());
			e.printStackTrace();
		}
		log.info("{}:变更固定资产,结束...",LocalDateTime.now());
		log.info("固定资产转移成功......................");
		return null;
		
	//执行请求
	
	}
	
	private AssetPd createPd(Asset asset, AssetPd pd) {
		pd.setAssetNumber(asset.getAssetNumber());
		pd.setSerialNumber(asset.getSerialNumber());
		pd.setUserName(asset.getUsername());
		pd.setDepartment(asset.getWorkCenterName());
		pd.setOrganizationName(asset.getOrganizationName());
		pd.setJobNum(asset.getJobnum());
		pd.setDescription(asset.getDescription());
		pd.setAssetModel(asset.getAssetmodel());
		pd.setAllocation(asset.getAllocation());
		return pd;
	}
	
	@Deprecated
	private static IMsgObject executeReQuest(IMsgObject reqMo) throws Exception {
		IServiceRequester requester = BaseServiceRequester.getInstance();
		return requester.execute(reqMo);
	}

}
