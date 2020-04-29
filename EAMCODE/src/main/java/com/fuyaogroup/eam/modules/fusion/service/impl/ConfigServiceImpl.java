package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.ConfigMapper;
import com.fuyaogroup.eam.modules.fusion.model.Config;
import com.fuyaogroup.eam.modules.fusion.service.ConfigService;
@Service
public class ConfigServiceImpl extends BaseServiceImpl<ConfigMapper, Config> implements ConfigService{

	@Autowired
		 ConfigMapper configMapper;

	@Override
	public List<Config> getOrgList() {
		// TODO Auto-generated method stub
		return configMapper.queryByType(1);//1代表测试环境的组织列表
	}

	@Override
	public List<Config> getEmailList(Integer confType) {
		return configMapper.queryByType(confType);
	}

	@Override
	public List<Config> getUser(Integer confType, String confName) {
		return configMapper.queryUserByType(confType,confName);
	}


}
