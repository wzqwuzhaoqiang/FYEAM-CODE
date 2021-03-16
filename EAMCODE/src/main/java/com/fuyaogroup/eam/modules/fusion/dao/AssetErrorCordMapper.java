package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetErrorCord;

@Mapper
public interface AssetErrorCordMapper extends BaseMapper<AssetErrorCord> {

	@Insert("INSERT INTO ASSETERRORCORD(MYID,OABILLINUM,TRANMAN,TRANID,RECEIVEMAN,RECEIVEID,ASSETNAME,SERIAL,CREATEDATE)VALUES("
			+ "#{myid, jdbcType=VARCHAR},#{oabilliNum, jdbcType=VARCHAR},#{tranMan, jdbcType=VARCHAR},#{tranid, jdbcType=VARCHAR},#{receiveMan, jdbcType=VARCHAR},#{receiveid, jdbcType=VARCHAR},#{assetName, jdbcType=VARCHAR},#{serial, jdbcType=VARCHAR},#{createDate, jdbcType=DATE})")
	void addOne(AssetErrorCord aec);

	@Select("select * from ASSETERRORCORD order by CREATEDATE DESC")
	List<AssetErrorCord> queryList();

}
