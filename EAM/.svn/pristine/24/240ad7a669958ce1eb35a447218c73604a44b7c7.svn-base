package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.Config;


/**
 * 按灯编码对应表数据库调用
 * @author fuyao
 *
 */
@Service
public interface ConfigService extends IBaseService<Config>{
	
	/**
	 * 获取组织
	 */
	public List<Config> getOrgList();
	
	public List<Config> getEmailList(Integer confType);

	public List<Config> getUser(Integer confType,String confName);
	
}
