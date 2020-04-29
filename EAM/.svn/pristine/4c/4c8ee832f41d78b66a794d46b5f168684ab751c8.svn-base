package com.fuyaogroup.eam.modules.fusion.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;


/**
 * 按灯编码对应表数据库调用
 * @author fuyao
 *
 */
@Service
public interface AssetPdBatService extends IBaseService<AssetPdBat>{
	
	/**
	 * 获取默认的BatId
	 * @return
	 */
	public String getDefaultBatId();
	
	public List<AssetPdBat> getAll();
	
	public List<AssetPdBat> getAllBDate(Date nowDate);

	public AssetPdBat getByBatId(Long id);
	
	public AssetPdBat insertOne(AssetPdBat bat);
}

