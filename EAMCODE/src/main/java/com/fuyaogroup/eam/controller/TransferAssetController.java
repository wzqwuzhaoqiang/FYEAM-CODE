package com.fuyaogroup.eam.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.model.AssetTransfer;
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
	private FusionEAMAPIUtil fuEAMUtil=new FusionEAMAPIUtil();
	
	
	//测试
	private static  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	
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
			Asset asset = fuEAMUtil.getOneAssetBySerNum(assetSerNum);
			if(asset==null){
				return null;
			}
			asset.setJobnum(reqRecord.getFieldValue("jobNum"));
			asset.setOABillINum(reqRecord.getFieldValue("OABillINum"));
			asset.setHandoverCpt(reqRecord.getFieldValue("handoverCpt"));
			asset.setHandoverPerson(reqRecord.getFieldValue("handoverPerson"));//需要工号
			asset.setHandoverTime(simpleDateFormat.parse(reqRecord.getFieldValue("handoverTime")));
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
			if (fuEAMUtil != null) {
				asset = fuEAMUtil.updateAssetDescriptiveFields(asset,true);
				assetSevice.updateOne(asset);
				assetTransferSevice.createOne(assettf);
				List<AssetPd> pdList = assetPdService.getBySerialNum(asset.getSerialNumber(), PdStatusEnum.ASSET_PD_WAITING.getCode());
				if(!CollectionUtil.isEmpty(pdList)){
				assetPdService.updateAssetPd(pdList.get(0));
				}
				List<GroupRecord> grs = new ArrayList<GroupRecord>();
				grs.add(GroupRecordTool.convertToGroupRecord(asset));
				reqMo.setResGroupRecord(grs);
				log.info("{}:变更固定资产,结束...",LocalDateTime.now());
				return reqMo;
			}
		} catch (Exception e) {
			log.error("变更固定资产,失败,{}",e.getMessage());
			e.printStackTrace();
		}
		log.info("{}:变更固定资产,结束...",LocalDateTime.now());
		return null;
		
	//执行请求
	
	}
	
	
	@Deprecated
	private static IMsgObject executeReQuest(IMsgObject reqMo) throws Exception {
		IServiceRequester requester = BaseServiceRequester.getInstance();
		return requester.execute(reqMo);
	}

}
