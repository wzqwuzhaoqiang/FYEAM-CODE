package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.RepairRecord;

import io.lettuce.core.dynamic.annotation.Param;

@Mapper
public interface RepairRecordMapper extends BaseMapper<RepairRecord> {

	@Select("select * from ASSET_REPAIR_TRACKRECORD")
	public List<RepairRecord> queryList();

	@Select("select * from ASSET_REPAIR_TRACKRECORD where ASSETNUMBER = #{assetNumber}")
	public List<RepairRecord> queryListByAssetNumber(@Param("assetNumber") String assetNumber);
}
