package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;

@Mapper
public interface AssetrPdMapper extends BaseMapper<AssetPd>{
	
	 @Select("SELECT * FROM ASSETPD " +
	            "where SerialNumber = #{SerialNumber}  and status=#{status} order by pdbatid desc")//and t_time > #{starttime} 
	    List<AssetPd> queryByCode(@Param("SerialNumber")String assetNum,@Param("status") int status);
	 
	 @Select("SELECT * FROM ASSETPD " +
	            "a where a.SerialNumber = #{SerialNumber}   order by pdbatid desc")//and t_time > #{starttime} 
	    List<AssetPd> querySN(@Param("SerialNumber")String assetNum);
	 
	 @Select("SELECT * FROM ASSETPD " +
	            "a where a.pdCode = #{pdCode} ")//and t_time > #{starttime} 
	    List<AssetPd> queryByPdCode(@Param("pdCode")Long pdCode);
	 
	 
	 @Select("SELECT * FROM ASSETPD " +
	            "a where a.pdBatId = #{pdBatId} ")
	 List<AssetPd> queryAllByBatId(@Param("pdBatId")Long pdBatId);
	 
	 @Select("SELECT * FROM ASSETPD where 1=1" )
		List<AssetPd> queryAll();
	 
	 @Select("SELECT * FROM ASSETPD " +
	            "a where a.pdBatCode = #{pdBatCode} ")
	 List<AssetPd> queryAllByBatCode(@Param("pdBatCode")Long pdBatCode);
	 
	 @Insert("INSERT INTO ASSETPD("
				+ "OrganizationName,department,jobNum,userName,Description, "
				+"AssetNumber,assetModel,allocation,SerialNumber,pdTime,pdBatId,status,pdCode) "+" VALUES("
			+ "#{OrganizationName, jdbcType=VARCHAR}, #{ department, jdbcType=VARCHAR},"
			+ " #{jobNum, jdbcType=VARCHAR}, #{userName, jdbcType=VARCHAR},"
			+ " #{Description, jdbcType=VARCHAR}, #{AssetNumber, jdbcType=VARCHAR}, #{assetModel, jdbcType=VARCHAR},"
			+ " #{allocation, jdbcType=VARCHAR}, #{SerialNumber, jdbcType=VARCHAR}, #{pdTime, jdbcType=DATE},"
			+ " #{pdBatId},#{status,jdbcType=NUMERIC}, #{pdCode})")
	    void insertPdRecord(AssetPd pd);
	 
	 @Update("update ASSETPD SET status=#{status},pdTime=#{pdTime},pdImgPath=#{pdImgPath}"
         +" where pdCode=#{pdCode}")
	 void updatePdRecord(@Param("status") int status,@Param("pdTime") Date pdTime,@Param("pdCode")Long pdCode,@Param("pdImgPath")String pdImgPath);
	 
	 @Update("update ASSETPD SET status=#{pd.status,jdbcType=NUMERIC}, jobNum=#{pd.jobNum,jdbcType=VARCHAR},SerialNumber=#{pd.SerialNumber,jdbcType=VARCHAR},AssetNumber=#{pd.AssetNumber,jdbcType=VARCHAR},userName=#{pd.userName, jdbcType=VARCHAR},OrganizationName=#{pd.OrganizationName, jdbcType=VARCHAR},department=#{pd.department, jdbcType=VARCHAR},pdBatId=#{pd.pdBatId,jdbcType=NUMERIC},Description=#{pd.Description, jdbcType=VARCHAR},assetModel=#{pd.assetModel, jdbcType=VARCHAR},allocation=#{pd.allocation, jdbcType=VARCHAR},REMARK=#{pd.remark, jdbcType=VARCHAR},UPDATEMAN=#{pd.updateMan, jdbcType=VARCHAR}"
	         +" where pdCode=#{pd.pdCode}")
		 void updatePd(@Param("pd") AssetPd pd);
	 
	 
	 
	 @Select("SELECT * FROM ASSETPD a where a.pdBatId = #{pdBatId} and a.status='0'")
	 List<AssetPd> queryAllUnDoByBatId(@Param("pdBatId")Long pdBatId);

	 @Select("SELECT * FROM ASSETPD  where PDBATID=#{pdBatId}  AND STATUS != 1")
	List<AssetPd> sendAgain(@Param("pdBatId")String pdBatId);

	@Select("SELECT PDCODE FROM ASSETPD where PDBATID=#{batId} ORDER BY PDCODE desc")
	List<Long> queryMaxPdcode(@Param("batId") Long batId);

	@Select("SELECT * FROM ASSETPD " +
            "a where a.SerialNumber = #{serialNum} and a.pdbatid=#{batid}")
	AssetPd getBySerialNumAndBatid(@Param("serialNum")String serialNum, @Param("batid")String batid);

	@Delete("DELETE ASSETPD where PDCODE=#{pdcode}")
	void deleteAssetPd(@Param("pdcode")String pdcode);


	 

}
