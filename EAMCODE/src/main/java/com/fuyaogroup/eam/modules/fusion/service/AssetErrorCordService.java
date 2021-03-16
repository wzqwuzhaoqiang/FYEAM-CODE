package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.AssetErrorCord;

@Service
public interface AssetErrorCordService extends IBaseService<AssetErrorCord> {

	void addOne(AssetErrorCord aec);

	List<AssetErrorCord> queryList();

}
