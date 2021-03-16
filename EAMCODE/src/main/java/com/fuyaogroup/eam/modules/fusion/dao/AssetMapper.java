package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.dao.DataAccessException;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetLifeRecored;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;

@Mapper
public interface AssetMapper extends BaseMapper<Asset>{

	@Select("SELECT /*+ INDEX(USER ID) */  * FROM ASSET " +
            "a where a.assetType = #{assetType}  ")
	List<Asset> queryByType(@Param("assetType") int assetType);
	
//	@Select("SELECT * FROM ASSET " +
//            "a where a.assetType = #{assetType} and a.OrganizationName in = (#{orgList})")
	 @Select({
         "<script>",
             "SELECT * FROM ASSET t where t.assetType = #{assetType} and STATUS!=2 and t.OrganizationName in",
             "<foreach collection='orgList' item='item' open='(' separator=',' close=')'>",
                 "#{item}",
             "</foreach>",
         "</script>"
 })
	List<Asset> queryByOrg(@Param("assetType") Integer assetType,@Param("orgList") List<String> list);

	 @Select("SELECT * FROM ASSET " +
	            "a where a.jobNum = #{jobNum} and a.assetType = #{assetType}")
	List<Asset> QueryByJobNum(@Param("assetType") Integer assetType, @Param("jobNum") String userId);

	 @Select("SELECT * FROM ASSET " +
	            "a where a.SerialNumber = #{SerialNumber}")
	List<Asset> getBySerialNumber(@Param("SerialNumber") String SerialNumber);
	 
	 @Select("SELECT * FROM ASSET " +
	            "a where a.AssetNumber = #{AssetNumber}")
	List<Asset> getByAssetNumber(@Param("AssetNumber") String AssetNumber);
	 
	 @Insert("INSERT INTO ASSET("
				+ "ItemId,AssetNumber,"
				+ "LocationOrganizationId,SerialNumber,"
				+ "AssetId,"
				+ "Description,WorkCenterId,ItemNumber,"
				+ "ItemDescription,WorkCenterName,OrganizationCode,"
				+ "OrganizationName,Configuration,"
				+ "financialCode,jobnum,username,"
				+ "htcIncredible,assetmodel,allocation,"
				+ "displayer,macaddress,serviceid,"
				+ "mouse,wifimac,keyboard,"
				+ "poweradapt,usingstarttime,warrantdate,warrantyreminderdate,"
				+ "remark,manufacturer,warrantyperiod,"
				+ "warrantStatus,amount,status,assetType,softType,OABillINum,source,softwarestatus,suite,contractId"
				+")"+" VALUES("
				+"#{ItemId, jdbcType=VARCHAR},#{AssetNumber, jdbcType=VARCHAR},#{LocationOrganizationId, jdbcType=VARCHAR},#{SerialNumber, jdbcType=VARCHAR},#{AssetId, jdbcType=VARCHAR},#{Description, jdbcType=VARCHAR},#{WorkCenterId, jdbcType=VARCHAR},#{ItemNumber, jdbcType=VARCHAR},#{ItemDescription, jdbcType=VARCHAR},#{WorkCenterName, jdbcType=VARCHAR},#{OrganizationCode, jdbcType=VARCHAR},#{OrganizationName, jdbcType=VARCHAR},#{Configuration, jdbcType=VARCHAR},#{financialCode, jdbcType=VARCHAR},#{jobnum,jdbcType=NUMERIC},#{username, jdbcType=VARCHAR},#{htcIncredible,jdbcType=NUMERIC},#{assetmodel, jdbcType=VARCHAR},#{allocation, jdbcType=VARCHAR},#{displayer, jdbcType=VARCHAR},#{macaddress, jdbcType=VARCHAR},#{serviceid, jdbcType=VARCHAR},#{mouse, jdbcType=VARCHAR},#{wifimac, jdbcType=VARCHAR},#{keyboard, jdbcType=VARCHAR},#{poweradapt, jdbcType=VARCHAR},#{usingstarttime, jdbcType=DATE},#{warrantdate, jdbcType=DATE},#{warrantyreminderdate, jdbcType=DATE},#{remark, jdbcType=VARCHAR},#{manufacturer, jdbcType=VARCHAR},#{warrantyperiod,jdbcType=NUMERIC},#{warrantStatus,jdbcType=NUMERIC},#{amount,jdbcType=NUMERIC},#{status,jdbcType=NUMERIC},#{assetType,jdbcType=NUMERIC},#{softType,jdbcType=NUMERIC},#{OABillINum,jdbcType=VARCHAR},#{source,jdbcType=NUMERIC},#{softwarestatus,jdbcType=NUMERIC},#{suite,jdbcType=NUMERIC},#{contractId, jdbcType=VARCHAR})")
	void createOne(Asset asset) throws DataAccessException;;

	 @Update(" update ASSET SET ItemId=#{asset.ItemId, jdbcType=VARCHAR},AssetNumber=#{asset.AssetNumber, jdbcType=VARCHAR},SerialNumber=#{asset.SerialNumber, jdbcType=VARCHAR},LocationOrganizationId=#{asset.LocationOrganizationId, jdbcType=VARCHAR},Description=#{asset.Description, jdbcType=VARCHAR},WorkCenterId=#{asset.WorkCenterId, jdbcType=VARCHAR},ItemNumber=#{asset.ItemNumber, jdbcType=VARCHAR},ItemDescription=#{asset.ItemDescription, jdbcType=VARCHAR},WorkCenterName=#{asset.WorkCenterName, jdbcType=VARCHAR},OrganizationCode=#{asset.OrganizationCode, jdbcType=VARCHAR},OrganizationName=#{asset.OrganizationName,jdbcType=VARCHAR},Configuration=#{asset.Configuration, jdbcType=VARCHAR},financialCode=#{asset.financialCode, jdbcType=VARCHAR},jobnum=#{asset.jobnum,jdbcType=NUMERIC},htcIncredible=#{asset.htcIncredible,jdbcType=NUMERIC},assetmodel=#{asset.assetmodel, jdbcType=VARCHAR},allocation=#{asset.allocation, jdbcType=VARCHAR},displayer=#{asset.displayer, jdbcType=VARCHAR},macaddress=#{asset.macaddress, jdbcType=VARCHAR},serviceid=#{asset.serviceid, jdbcType=VARCHAR},mouse=#{asset.mouse, jdbcType=VARCHAR},wifimac=#{asset.wifimac, jdbcType=VARCHAR},keyboard=#{asset.keyboard, jdbcType=VARCHAR},poweradapt=#{asset.poweradapt, jdbcType=VARCHAR},usingstarttime=#{asset.usingstarttime, jdbcType=DATE},updateTime=#{asset.updateTime, jdbcType=DATE},remark=#{asset.remark, jdbcType=VARCHAR},manufacturer=#{asset.manufacturer, jdbcType=VARCHAR},warrantyperiod=#{asset.warrantyperiod,jdbcType=NUMERIC},warrantStatus=#{asset.warrantStatus,jdbcType=NUMERIC},amount=#{asset.amount,jdbcType=NUMERIC},status=#{asset.status,jdbcType=NUMERIC},softType=#{asset.softType,jdbcType=NUMERIC},changeRemark=#{asset.changeRemark,jdbcType=VARCHAR},changeTime=#{asset.changeTime,jdbcType=DATE},updateName=#{asset.updateName,jdbcType=VARCHAR},OABillINum=#{asset.OABillINum,jdbcType=VARCHAR},USERNAME=#{asset.username,jdbcType=VARCHAR},contractId=#{asset.contractId,jdbcType=VARCHAR}"
	 +" where  AssetNumber=#{asset.AssetNumber,jdbcType=VARCHAR} or SerialNumber=#{asset.SerialNumber,jdbcType=VARCHAR} ")
	void updateOne(@Param("asset") Asset asset);
	 
	 @Select("SELECT * FROM ASSET " +
	            "a where a.assetid = #{assetId}")
	List<Asset> getByAssetId(String assetId);

	@Select("select a.*,rownum rn " +
			"from (select * from asset) a where a.assetType = #{assetType} and rownum <#{rowEndNum}) where rn>=#{rowStartNum}")
	List<Asset> queryByTypeByPage(@Param("assetType") int assetType, @Param("rowStartNum") Integer rowStartNum, @Param("rowEndNum") Integer rowEndNum);

	@Select("select /*+ INDEX(USER ID) */ count(*) FROM Asset  t where t.assetType = #{assetType}")
	Integer queryTotalRows(@Param("assetType") int assetType);

	@Select("select AssetNumber,AssetModel as assetName,USINGSTARTTIME as startDate,AssetNumber as formNumber,\r\n" + 
			"jobnum as userName\r\n" + 
			"FROM Asset where AssetNumber=#{assetNumber}")
	AssetLifeRecored getAssetByNumberr(@Param("assetNumber") String assetNumber);

	@Select("select OAID as formNumber,ASSERTNUMBER as assetNumber,ASSERTNAME as assetName,BORROWOUTDATE as borrowOutDate,RETURNDATE as returnDate,BORROWER as borrower,CONTACTINFOR as contactInfor,BORROWUSEDATE as borrowUseDate,BORROWOUTMAN as borrowOutman,RETURNIS as returnIs,RETURNTWOIS as returntwoIs,RENEWDATENUMBER as renewDateNumber,THINGSITUATION as thingSituation,RECIVER as reciver,RETURNDATE as returnDate from ASSET_BORROW_TRACKRECORD where ASSERTNUMBER=#{assetNumber}")
	List<AssetLifeRecored> getBorrowRecored(@Param("assetNumber") String assetNumber);

	@Select("select OAID as formNumber,ASSETNUMBER as assetNumber,ASSETNAME as assetName,CREATEDATE as startDate,CONNECTPHONE as contactInfor,FAULTPHENOMENON as faultPhenomenon,HANDLEOPINIONS as handleOpinions,REPAIRCHECKANDACCEPT as repairCheckAndAccept,DOCUMENTNUMBER as documentNumber from ASSET_REPAIR_TRACKRECORD where ASSETNUMBER=#{assetNumber}")
	List<AssetLifeRecored> getRepairRecored(@Param("assetNumber") String assetNumber);

	@Select("select OAID as formNumber,ASSETNUMBER as assetNumber,ASSETNAME as assetName,CREATEDATE as startDate,REASON as reason,MEASURES as measures,HANDINGSUGGESTIONS as handleOpinions from ASSET_SCRAP_TRACKRECORD where ASSETNUMBER=#{assetNumber}")
	List<AssetLifeRecored> getScrpRecored(@Param("assetNumber") String assetNumber);

	@Select("SELECT * FROM ASSET where ORGANIZATIONNAME in ('福耀浮法集团','福清机械制造') and ASSETNUMBER not in (SELECT ASSETNUMBER from ASSETPD WHERE PDBATID= 200615708 )")
	List<Asset> getbujiuAssets();

	@Select("SELECT * FROM ASSETPD where status <>1")
	List<AssetPd> getUnpdAsset();

	@Update("update ASSET SET ORGANIZATIONNAME=#{asset.organizationName, jdbcType=VARCHAR},JOBNUM=#{asset.jobnum, jdbcType=VARCHAR},DESCRIPTION=#{asset.description, jdbcType=VARCHAR},ASSETNUMBER=#{asset.assetNumber, jdbcType=VARCHAR},ASSETMODEL=#{asset.assetmodel, jdbcType=VARCHAR},SOFTTYPE=#{asset.softType, jdbcType=NUMERIC},MANUFACTURER=#{asset.manufacturer, jdbcType=VARCHAR},SOFTWARESTATUS=#{asset.softwarestatus, jdbcType=NUMERIC},SOURCE=#{asset.source, jdbcType=NUMERIC},SUITE=#{asset.suite, jdbcType=NUMERIC},remark=#{asset.remark, jdbcType=VARCHAR},contractId=#{asset.contractId,jdbcType=VARCHAR},usingstarttime = #{asset.usingstarttime ,jdbcType=DATE},warrantdate = #{asset.warrantdate ,jdbcType=DATE},warrantyreminderdate = #{asset.warrantyreminderdate ,jdbcType=DATE}  where  ASSETNUMBER=#{asset.AssetNumber,jdbcType=VARCHAR} or SERIALNUMBER=#{asset.SerialNumber,jdbcType=VARCHAR}")
	void updateSoftAssetOne(@Param("asset")Asset asset);

	@Select("select * from asset where ASSETTYPE = 2 and WARRANTYREMINDERDATE is not null ORDER BY DESCRIPTION")
	List<Asset> getAllSoftAsset();

	@Select("select to_char(USINGSTARTTIME,'yyyy') as year,COUNT(ASSETNUMBER) as num from (SELECT * from ASSET where USINGSTARTTIME is not null ) group by to_char(USINGSTARTTIME,'yyyy') ORDER BY year ASC")
	List<Map<String,Object>> getBeShowData();

	@Delete("DELETE ASSET where ASSETNUMBER =#{assetNumber}")
	int deleteSoft(@Param("assetNumber")String assetNumber);

}
