package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetScrap;

@Mapper
public interface AssetScrapMapper extends BaseMapper<AssetScrap> {

	@Select("select * from ASSET_SCRAP_TRACKRECORD")
	public List<AssetScrap> queryScrapList();


	@Select("select a.*,rownum rn " +
			"from (select * from asset) a where a.assetType = #{assetType} and rownum <#{rowEndNum}) where rn>=#{rowStartNum}")
	public List<AssetScrap> queryScrapListByPage(@Param("rowStartNum") Integer rowStartNum, @Param("rowEndNum") Integer rowEndNum);

}
