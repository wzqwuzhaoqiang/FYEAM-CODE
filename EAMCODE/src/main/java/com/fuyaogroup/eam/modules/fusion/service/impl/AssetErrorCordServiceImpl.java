package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetErrorCordMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetErrorCord;
import com.fuyaogroup.eam.modules.fusion.service.AssetErrorCordService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetErrorCordServiceImpl extends BaseServiceImpl<AssetErrorCordMapper, AssetErrorCord> implements AssetErrorCordService {

	@Autowired
	AssetErrorCordMapper aecMapper;
	
	@Override
	public void addOne(AssetErrorCord aec) {
		// TODO Auto-generated method stub
		aecMapper.addOne(aec);
	}

	@Override
	public List<AssetErrorCord> queryList() {
		// TODO Auto-generated method stub
		return aecMapper.queryList();
	}


}
