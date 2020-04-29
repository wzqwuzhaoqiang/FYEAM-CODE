package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.Config;
/**
 * 
 * @author fuyao
 *
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config>{
	
	//TODO 查询公司列表
    @Select("SELECT * FROM config " +
            "a where a.config_Type = #{configType}")
    List<Config> queryByType(Integer configType);

    @Select("SELECT * FROM config " +
            "a where a.config_Type = #{configType} and a.configname=#{configName}")
    List<Config> queryUserByType(@Param("configType") Integer configType,@Param("configName") String configName);


    

}
