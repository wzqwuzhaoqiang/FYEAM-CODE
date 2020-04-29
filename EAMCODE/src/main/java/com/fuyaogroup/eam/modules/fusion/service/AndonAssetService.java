package com.fuyaogroup.eam.modules.fusion.service;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.AndonAsset;


/**
 * 按灯编码对应表数据库调用
 * @author fuyao
 *
 */
@Service
public interface AndonAssetService extends IBaseService< AndonAsset>{
	/**
	 * 根据编码查询设备列表
	 */
	public AndonAsset getAndonAssetByCode(String code) throws Exception;

}
