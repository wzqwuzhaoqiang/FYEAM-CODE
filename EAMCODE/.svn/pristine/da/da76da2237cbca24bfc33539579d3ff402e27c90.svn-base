package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.AndonAsset;
/**
 * 
 * @author fuyao
 *
 */
@Mapper
public interface AndonAssetMapper extends BaseMapper<AndonAsset>{
	/**
	 * 
	 * @param itemId
	 * @param organizationId
	 * @param categoryId
	 * @param categorySetId
	 * @return
	 */
    @Select("SELECT * FROM andon_asset " +
            "a where a.andon_code = #{andon_code} ")//and t_time > #{starttime} 
    @Results({
        @Result(property ="andon_code",column = "andon_code"),
             @Result(property ="asset_num",column = "asset_num"),
             @Result(property ="is_use",column = "is_use"),
    })
    List<AndonAsset> queryByCode(@Param("andon_code")String andon_code);

}
