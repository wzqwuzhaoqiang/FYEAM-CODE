package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetrPdMapper;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.util.ChineseToEnglish;
import com.fuyaogroup.eam.util.Email;

@Service
public class AssetPdServiceImpl extends BaseServiceImpl<AssetrPdMapper, AssetPd> implements AssetPdService{
	  @Autowired
		private AssetrPdMapper assetrPd;
	  
	  
	
	public List<AssetPd> getbyBatId(Long batId) {
		if(batId==null){
			return assetrPd.queryAll();
		}
		return assetrPd.queryAllByBatId(batId);
	}

	@Override
	public void updateAssetPd(AssetPd assetPd) {
		 assetrPd.updatePd(assetPd);
	}

	@Override
	@Transactional
	public void createAllAssetPd(Long batId,List<Asset> assets) {
		List<Long> pdcode = this.queryMaxPdcode(batId);
		int index =0;
		if(pdcode.size()>0) {
			index = (pdcode.get(0)).intValue()-batId.intValue()*100+1;
			for(int i=0;i<assets.size();i++){
				if(!"总裁办".equals(assets.get(i).getWorkCenterName())) {
					if(assets.get(i).getStatus()!=2||assets.get(i).getAssetType()!=2){
						AssetPd assetPd = this.createPdFromAsset(assets.get(i),batId,index+i);
						assetrPd.insertPdRecord(assetPd);
					}
				}
			}
		}else {
			for(int i=0;i<assets.size();i++){
				if(!"总裁办".equals(assets.get(i).getWorkCenterName())) {
					if(assets.get(i).getStatus()!=2||assets.get(i).getAssetType()!=2){
						AssetPd assetPd = this.createPdFromAsset(assets.get(i),batId,i);
						assetrPd.insertPdRecord(assetPd);
					}
				}
			}
		}
		
	}

	private AssetPd createPdFromAsset(Asset asset, Long batId, int i) {
		AssetPd pd = new AssetPd();
		pd.setAllocation(asset.getAllocation());
		pd.setAssetModel(asset.getAssetmodel());
		pd.setAssetNumber(asset.getAssetNumber());
		pd.setDepartment(asset.getWorkCenterName());
		pd.setDescription(asset.getDescription());
		pd.setJobNum(asset.getJobnum());
		pd.setOrganizationName(asset.getOrganizationName());
		pd.setSerialNumber(asset.getSerialNumber());
		pd.setUserName(asset.getUsername());
		pd.setStatus(PdStatusEnum.ASSET_PD_WAITING.getCode());
		pd.setPdBatId(batId);
		pd.setPdCode(batId*100+i);
		return pd;
	}

	@Override
	public List<AssetPd> getBySerialNum(String serialNum, Integer status) {

		if(status!=null){
			return assetrPd.queryByCode(serialNum, status);
		}
		return assetrPd.querySN(serialNum);
	}

	@Override
	public void insertPdRecord(AssetPd assetPd) {
		assetrPd.insertPdRecord(assetPd);
		
	}

	@Override
	public void updateAssetStatus(int status, Date pdTime, Long pdCode,
			String pdImgPath) {
		assetrPd.updatePdRecord(status, pdTime, pdCode, pdImgPath);
		
	}

	@Override
	public List<AssetPd> queryAllUnDoByBatId(Long batId) {
		// TODO Auto-generated method stub
		return assetrPd.queryAllUnDoByBatId(batId);
	}
	
	
	
	@Transactional
	public void createbujiuAssetPd(Long batId,List<Asset> assets) {
		for(int i=0;i<assets.size();i++){
			AssetPd assetPd = this.createbujiuPdFromAsset(assets.get(i),batId,i);
			assetrPd.insertPdRecord(assetPd);
		}
	}

	private AssetPd createbujiuPdFromAsset(Asset asset, Long batId, int i) {
		AssetPd pd = new AssetPd();
		pd.setAllocation(asset.getAllocation());
		pd.setAssetModel(asset.getAssetmodel());
		pd.setAssetNumber(asset.getAssetNumber());
		pd.setDepartment(asset.getWorkCenterName());
		pd.setDescription(asset.getDescription());
		pd.setJobNum(asset.getJobnum());
		pd.setOrganizationName(asset.getOrganizationName());
		pd.setSerialNumber(asset.getSerialNumber());
		pd.setUserName(asset.getUsername());
		pd.setStatus(PdStatusEnum.ASSET_PD_WAITING.getCode());
		pd.setPdBatId(batId);
		pd.setPdCode(batId*100+300+i);
		return pd;
	}

	@Override
	public List<AssetPd> sendAgain(String pdBatId) {
		// TODO Auto-generated method stub
		return assetrPd.sendAgain(pdBatId);
	}

	@Override
	public List<Long> queryMaxPdcode(Long batId) {
		// TODO Auto-generated method stub
		return assetrPd.queryMaxPdcode(batId);
	}

	@Override
	public AssetPd getBySerialNumAndBatid(String serialNum, String batid) {
		// TODO Auto-generated method stub
		return assetrPd.getBySerialNumAndBatid(serialNum,batid);
	}

	@Override
	public void deleteAssetPd(String pdcode) {
		// TODO Auto-generated method stub
		assetrPd.deleteAssetPd(pdcode);
	}

	@Override
	public boolean sendEmail(String result) {
		// TODO Auto-generated method stub
		String userName = "noreply@fuyaogroup.com"; // 发件人邮箱  
        String password = "Fuyao2018"; // 发件人密码  
        String smtpHost = "mail.fuyaogroup.com"; // 邮件服务器  
  
        
        String to =result; // 收件人，多个收件人以半角逗号分隔  
        String cc = ""; // 抄送，多个抄送以半角逗号分隔  plm_system@fuyaogroup.com
        String subject = "计算机未盘点，是否丢失"; // 主题  
        String body = "{测试批次盘点}您的计算机等资产还未在指定时间内盘点，是否丢失？请尽快在微信上盘点，负责财务可能做盘亏处理"; // 正文，可以用html格式的哟  
        List<String> attachments = new ArrayList<String>();
        		//Arrays.asList(""); // 附件的路径，多个附件也不怕  C:\\Users\\gnb781\\Desktop\\01.jpg", "C:\\Users\\gnb781\\Desktop\\集团未盘点名单.xls
  
        Email email = Email.entity(smtpHost, userName, password, to, cc, subject, body, attachments);  
  
        try {
			email.send();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}


	
	
	
	
	
	
	

}
