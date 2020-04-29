package com.fuyaogroup.eam.modules.mes.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.mes.model.AndonHisTemp;
/**
 * 
 * @author fuyao
 *
 */
public interface AndonHisTempMapper extends BaseMapper<AndonHisTemp>{
	@Select("select * from HMCS.FY_ANDON_SOLUTION_ITF_EAM "
			+ "where event_id = #{event_id}")
	@Results({
			@Result(property ="fault_code",column = "FAULT_CODE"),
			@Result(property ="fault_info",column = "FAULT_INFO"),
			@Result(property ="reason",column = "REASON"),
			@Result(property ="description",column = "DESCRIPTION"),
			@Result(property ="solution",column = "SOLUTION"),
			@Result(property ="update_file",column = "UPDATE_FILE"),
			@Result(property ="falut_scrapt",column = "FALUT_SCRAPT"),
			@Result(property ="response_unit",column = "RESPONSE_UNIT"),
			@Result(property ="repair_man",column = "REPAIR_MAN"),
			@Result(property ="spare_part",column = "SPARE_PART"),
			@Result(property ="timteout_analysis",column = "TIMTEOUT_ANALYSIS"),
			@Result(property ="is_stop",column = "IS_STOP"),
			@Result(property ="operate_man",column = "OPERATE_MAN"),
			@Result(property ="status",column = "STATUS"),
			@Result(property ="remark",column = "REMARK"),
			@Result(property ="mes_process_status",column = "MES_PROCESS_STATUS"),
			@Result(property ="mes_process_msg",column = "MES_PROCESS_MSG"),
			@Result(property ="attribute1",column = "ATTRIBUTE1"),
			@Result(property ="attribute2",column = "ATTRIBUTE2"),
			@Result(property ="attribute3",column = "ATTRIBUTE3"),
			@Result(property ="attribute4",column = "ATTRIBUTE4"),
			@Result(property ="attribute5",column = "ATTRIBUTE5"),
			@Result(property ="attribute6",column = "ATTRIBUTE6"),
			@Result(property ="attribute7",column = "ATTRIBUTE7"),
			@Result(property ="attribute8",column = "ATTRIBUTE8"),
			@Result(property ="attribute9",column = "ATTRIBUTE9"),
			@Result(property ="attribute10",column = "ATTRIBUTE10")
	})
	List<AndonHisTemp> selectByEId(@Param("event_id")Number event_id);
	/**
	 * 插入按灯模块临时表
	 * @param date 
	 * @param number 
	 * @param itemId
	 * @param organizationId
	 * @param categoryId
	 * @param categorySetId
	 * @return
	 */
	@Insert("INSERT INTO HMCS.FY_ANDON_SOLUTION_ITF_EAM("
			+ "creation_date,created_by,last_updated_by, "
			+ "last_update_date,event_id,"
			+ "fault_type,fault_code,fault_info,reason,"
			+ "description,solution,update_file,falut_scrapt,"
			+ "response_unit,repair_man,spare_part,timteout_analysis,"
			+ "is_stop,operate_man,status,remark,mes_process_status,attribute8)  "
			+ "VALUES(#{creationDate}, #{createdBy}, "
			+ "#{lastUpdatedBy}, #{ lastUpdateDate},"
			+ " #{event_id}, #{fault_type},"
			+ " #{fault_code}, #{fault_info}, #{reason},"
			+ " #{description}, #{solution}, #{update_file},"
			+ " #{falut_scrapt}, #{response_unit}, #{repair_man},"
			+ " #{spare_part}, #{timteout_analysis}, #{is_stop},"
			+ " #{operate_man}, #{status}, #{remark}, "
			+ "#{mes_process_status},#{attribute8})")
    void insertTempById(AndonHisTemp temp);


}
