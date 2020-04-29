package com.fuyaogroup.eam.modules.mes.service;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.mes.model.AndonHisTemp;


/**
 * 灯临时表记录-用来同步
 * @author fuyao
 *
 */
@Service
public interface AndonTempService extends IBaseService<AndonHisTemp>{
	/**
	 * 插入按灯临时表记录
	 * @param temp
	 * @return
	 * @throws Exception
	 */
	public void insertAndonTemp(AndonHisTemp temp) throws Exception;
	
	/**
	 * 获取临时表数据
	 * @param temp
	 * @return
	 * @throws Exception
	 */
	public AndonHisTemp getAndonTempByEId(Number eventId) throws Exception;

}
