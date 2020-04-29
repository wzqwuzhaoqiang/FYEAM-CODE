package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder>{

	@Select("SELECT * FROM WORK_ORDER " +
            " a where a.woType = #{woType}")
	List<WorkOrder> queryAll(@Param("woType") Integer woType);
	
	
	 
}
