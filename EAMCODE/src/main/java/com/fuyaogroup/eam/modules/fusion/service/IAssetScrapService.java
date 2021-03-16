package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.model.Page;
import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.AssetScrap;

public interface IAssetScrapService extends IBaseService<AssetScrap> {

	public List<AssetScrap> queryScrapList();

	public List<AssetScrap> getAllScrapByPage(Page page);
	
	public List<AssetScrap> getUnDoMessager();
	
	public int update(String AssetNumber);

}
