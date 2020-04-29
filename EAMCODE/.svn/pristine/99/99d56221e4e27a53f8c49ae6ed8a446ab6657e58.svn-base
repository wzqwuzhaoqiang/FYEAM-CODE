package com.fuyaogroup.eam.modules.mes.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.mes.model.AndonHis;
/**
 * 
 * @author fuyao
 *
 */
public interface AndonHisMapper extends BaseMapper<AndonHis>{
	/**
	 * 
	 * @param itemId
	 * @param organizationId
	 * @param categoryId
	 * @param categorySetId
	 * @return
	 */
    @Select("SELECT * FROM HMCS.HCM_ANDON_HIS_FOR_EAM_V " +
            "a where a.andon_status = #{andon_status} ")//and t_time > #{starttime} 
    @Results({
        @Result(property ="SERVER_ID",column = "server_id"),
             @Result(property ="server_name",column = "SERVER_NAME"),
             @Result(property ="event_id",column = "EVENT_ID"),
             @Result(property ="plant_code",column = "PLANT_CODE"),
             @Result(property ="plant_desc",column = "PLANT_DESC"),
             @Result(property ="andon_code",column = "ANDON_CODE"),
             @Result(property ="andon_desc",column = "ANDON_DESC"),
             @Result(property ="prod_line_code",column = "PROD_LINE_CODE"),
             @Result(property ="line_desc",column = "LINE_DESC"),
             @Result(property ="workcell_code",column = "WORKCELL_CODE"),
             @Result(property ="wkc_desc",column = "WKC_DESC"),
             @Result(property ="btype_code",column = "BTYPE_CODE"),
             @Result(property ="btype_desc",column = "BTYPE_DESC"),
             @Result(property ="prod_calendar_day",column = "PROD_CALENDAR_DAY"),
             @Result(property ="prod_shift_code",column = "PROD_SHIFT_CODE"),
             @Result(property ="andon_status",column = "ANDON_STATUS"),
             @Result(property ="solution_status",column = "SOLUTION_STATUS"),
             @Result(property ="respond_value",column = "RESPOND_VALUE"),
             @Result(property ="manage_value",column = "MANAGE_VALUE"),
             @Result(property ="t_time",column = "T_TIME"),
             @Result(property ="r_time",column = "R_TIME"),
             @Result(property ="c_time",column = "C_TIME"),
             @Result(property ="remark",column = "REMARK"),
    })
    List<AndonHis> queryByStatus(@Param("andon_status")String andon_status);

    @Select("SELECT * FROM HMCS.HCM_ANDON_HIS_FOR_EAM_V " +
            "a where  a.solution_status = #{solution_status} and a.solution_status = #{solution_status} and t_time > #{starttime}")//and t_time > #{starttime} 
    @Results({
        @Result(property ="SERVER_ID",column = "server_id"),
             @Result(property ="server_name",column = "SERVER_NAME"),
             @Result(property ="event_id",column = "EVENT_ID"),
             @Result(property ="plant_code",column = "PLANT_CODE"),
             @Result(property ="plant_desc",column = "PLANT_DESC"),
             @Result(property ="andon_code",column = "ANDON_CODE"),
             @Result(property ="andon_desc",column = "ANDON_DESC"),
             @Result(property ="prod_line_code",column = "PROD_LINE_CODE"),
             @Result(property ="line_desc",column = "LINE_DESC"),
             @Result(property ="workcell_code",column = "WORKCELL_CODE"),
             @Result(property ="wkc_desc",column = "WKC_DESC"),
             @Result(property ="btype_code",column = "BTYPE_CODE"),
             @Result(property ="btype_desc",column = "BTYPE_DESC"),
             @Result(property ="prod_calendar_day",column = "PROD_CALENDAR_DAY"),
             @Result(property ="prod_shift_code",column = "PROD_SHIFT_CODE"),
             @Result(property ="andon_status",column = "ANDON_STATUS"),
             @Result(property ="solution_status",column = "SOLUTION_STATUS"),
             @Result(property ="respond_value",column = "RESPOND_VALUE"),
             @Result(property ="manage_value",column = "MANAGE_VALUE"),
             @Result(property ="t_time",column = "T_TIME"),
             @Result(property ="r_time",column = "R_TIME"),
             @Result(property ="c_time",column = "C_TIME"),
             @Result(property ="remark",column = "REMARK"),
    })
    List<AndonHis> queryBySolStatus(@Param("andon_status")String andon_status,@Param("solution_status") String solStatus,@Param("starttime")  Date starttime);

    @Select("SELECT * FROM HMCS.HCM_ANDON_HIS_FOR_EAM_V " +
            "a where  a.t_time > #{starttime}")//and t_time > #{starttime} 
    @Results({
        @Result(property ="SERVER_ID",column = "server_id"),
             @Result(property ="server_name",column = "SERVER_NAME"),
             @Result(property ="event_id",column = "EVENT_ID"),
             @Result(property ="plant_code",column = "PLANT_CODE"),
             @Result(property ="plant_desc",column = "PLANT_DESC"),
             @Result(property ="andon_code",column = "ANDON_CODE"),
             @Result(property ="andon_desc",column = "ANDON_DESC"),
             @Result(property ="prod_line_code",column = "PROD_LINE_CODE"),
             @Result(property ="line_desc",column = "LINE_DESC"),
             @Result(property ="workcell_code",column = "WORKCELL_CODE"),
             @Result(property ="wkc_desc",column = "WKC_DESC"),
             @Result(property ="btype_code",column = "BTYPE_CODE"),
             @Result(property ="btype_desc",column = "BTYPE_DESC"),
             @Result(property ="prod_calendar_day",column = "PROD_CALENDAR_DAY"),
             @Result(property ="prod_shift_code",column = "PROD_SHIFT_CODE"),
             @Result(property ="andon_status",column = "ANDON_STATUS"),
             @Result(property ="solution_status",column = "SOLUTION_STATUS"),
             @Result(property ="respond_value",column = "RESPOND_VALUE"),
             @Result(property ="manage_value",column = "MANAGE_VALUE"),
             @Result(property ="t_time",column = "T_TIME"),
             @Result(property ="r_time",column = "R_TIME"),
             @Result(property ="c_time",column = "C_TIME"),
             @Result(property ="remark",column = "REMARK"),
    })
	List<AndonHis> queryByDate(@Param("starttime")  Date starttime);

}
