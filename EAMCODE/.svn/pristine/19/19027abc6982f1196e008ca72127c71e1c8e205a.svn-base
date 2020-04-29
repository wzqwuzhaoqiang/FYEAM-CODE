package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetTransfer;

@Mapper
public interface AssetTransferMapper extends BaseMapper<AssetTransfer>{

	@Select("SELECT * FROM TRANS_ASSET " +
            "a where 1=1")
	List<AssetTransfer> queryAll();
	
	
	
	@Insert("INSERT INTO TRANS_ASSET("
			+ "AssetNumber,SerialNumber,"
			+ "handoverCpt,"
			+ "jobnum,username,"
			+ "handoverTime,handoverPerson,OABILLINUM"
			+")"+" VALUES("
			+"#{AssetNumber, jdbcType=VARCHAR},#{SerialNumber, jdbcType=VARCHAR},#{handoverCpt, jdbcType=VARCHAR},#{jobNum,jdbcType=NUMERIC},#{userName, jdbcType=VARCHAR},#{handoverTime,jdbcType=DATE},#{handoverPerson,jdbcType=VARCHAR},#{OABillNum,jdbcType=VARCHAR})")
	void createOne(AssetTransfer assettf);
	
}
