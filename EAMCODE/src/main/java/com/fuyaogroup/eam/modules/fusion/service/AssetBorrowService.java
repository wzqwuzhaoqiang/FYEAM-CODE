package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.AssetBorrow;

@Service
public interface AssetBorrowService extends IBaseService<AssetBorrow> {

	//查询所有借用单记录
	public List<AssetBorrow> queryList();
}
