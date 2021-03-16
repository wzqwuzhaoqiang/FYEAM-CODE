package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetrPdBatMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;

@Service
public class AssetPdBatServiceImpl extends BaseServiceImpl<AssetrPdBatMapper, AssetPdBat> implements AssetPdBatService{
	
	  public static String BatId="201909" ;//TODO 先测试Bat

	  @Autowired
		private AssetrPdBatMapper assetrPdBat;
	  
	@Override
	public String getDefaultBatId(String orgName) {
		if(BatId==null){
			List<AssetPdBat> list = assetrPdBat.queryByDate(new Date());
			if(CollectionUtil.isNotEmpty(list)){
				for(AssetPdBat apb:list) {
					if(apb.getOrgList().contains(orgName)) {
						BatId = apb.getPdBatId();
						return BatId;
					}
				}
				return null;
			}else{
				return null;
			}
		}
		 
		return BatId;
	}

	@Override
	public List<AssetPdBat> getAll() {
		
		return assetrPdBat.queryAll();
	}

	@Override
	public AssetPdBat getByBatId(Long id) {
		List<AssetPdBat> list =assetrPdBat.queryById(id);
		return CollectionUtil.isNotEmpty(list)?list.get(0):null;
	}

	@Override
	public AssetPdBat insertOne(AssetPdBat bat) {
		assetrPdBat.insertPdBat(bat);
		return bat;
	}

	@Override
	public List<AssetPdBat> getAllBDate(Date nowDate) {
		
		return assetrPdBat.queryByDate(nowDate);
	}

	@Override
	public List<AssetPdBat> queryByDateOfAssetPdBat(Date nowDate, String organizationName) {
		// TODO Auto-generated method stub
		return assetrPdBat.queryByDateOfAssetPdBat(nowDate,organizationName);
	}

}
