package com.fuyaogroup.eam.modules.mes.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.mes.model.AndonHis;


/**
 * 按灯视图数据库调用
 * @author fuyao
 *
 */
@Service
public interface AndonService extends IBaseService<AndonHis>{
	/**
	 * 根据状态和开始时间查询按灯列表
	 * 状态-触发
	 * 时间-10天内
	 */
	public List<AndonHis> getAndonHisByStatus(String status,Date starttime) throws Exception;
	/**
	 * 状态、录入状态和开始时间查询按灯列表
	 * @param status
	 * @param solStatus 
	 * @param starttime
	 * @return
	 * @throws Exception
	 */
	public List<AndonHis> getAndonHisBySolStatus(String status,String solStatus, Date starttime) throws Exception;

	/**
	 * 查找24小时之内的按灯数据
	 * @param starttime
	 * @return
	 * @throws Exception
	 */
	public List<AndonHis> getAndonHisByDate(Date starttime) throws Exception;

}
