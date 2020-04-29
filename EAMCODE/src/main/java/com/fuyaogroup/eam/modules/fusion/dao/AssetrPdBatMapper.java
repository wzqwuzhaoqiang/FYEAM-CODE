package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;

@Mapper
public interface AssetrPdBatMapper extends BaseMapper<AssetPdBat>{
	 @Select("SELECT * FROM ASSETPDBAT " +
	            "a where a.pdBatId = #{pdBatId} ")//and t_time > #{starttime} 
	 @Results({
		 @Result(property ="pdBatId",column = "PDBATID"),
		 @Result(property ="pdBatCode",column = "PDBATCODE"),
		 @Result(property ="pdStartDate",column = "PDSTARTDATE"),
		 @Result(property ="pdEndDate",column = "PDENDDATE"),
		 @Result(property ="iSAll",column = "ISALL"),
		 @Result(property ="orgList",column = "ORGLIST"),
	    })
	 List<AssetPdBat> queryById(@Param("pdBatId")Long pdBatId);
	 
	 @Select("SELECT * FROM ASSETPDBAT  ")
	 @Results({
		 @Result(property ="pdBatId",column = "PDBATID"),
		 @Result(property ="pdBatCode",column = "PDBATCODE"),
		 @Result(property ="pdStartDate",column = "PDSTARTDATE"),
		 @Result(property ="pdEndDate",column = "PDENDDATE"),
		 @Result(property ="iSAll",column = "ISALL"),
		 @Result(property ="orgList",column = "ORGLIST"),
	    })
	 List<AssetPdBat> queryAll();
	 
	 @Select("SELECT * FROM ASSETPDBAT " +
	            "a where a.pdEndDate >= #{nowDate} and a.pdStartDate<=#{nowDate} ")
	 List<AssetPdBat> queryByDate(@Param("nowDate") Date nowDate);
	 
	 @Insert("INSERT INTO ASSETPDBAT("
				+ "pdBatId,pdBatCode,pdStartDate,pdEndDate ,iSAll ,orgList "
				+")"+" VALUES("
				+"#{pdBatId}, #{ pdBatCode},"
				+ " #{pdStartDate}, #{pdEndDate},"
				+ " #{iSAll},#{orgList})")
	    void insertPdBat(AssetPdBat bat);
}
