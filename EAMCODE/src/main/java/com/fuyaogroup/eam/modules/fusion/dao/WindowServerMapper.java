package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.QtfwThing;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;

@Mapper
public interface WindowServerMapper extends BaseMapper<WindowServer> {

	@Insert("INSERT INTO WINDOW_SERVER(TABLEID,BORROWERID,BORROWERNAME,TOOLS,COUNT,BORROWTIME,RETURNTIME,STATUS,BORROWCONFIRM,BACKCONFIRM,PHOTO,MOBILE,SERIAL)"
			+ "VALUES(#{tableID, jdbcType=VARCHAR},#{borrowerId, jdbcType=VARCHAR},#{borrowerName, jdbcType=VARCHAR},#{tools, jdbcType=VARCHAR},#{count, jdbcType=NUMERIC},#{borrowTime, jdbcType=VARCHAR},#{returnTime, jdbcType=VARCHAR},#{status, jdbcType=VARCHAR},#{borrowConfirm, jdbcType=VARCHAR},#{backConfirm, jdbcType=VARCHAR},#{photo, jdbcType=VARCHAR},#{mobile, jdbcType=VARCHAR},#{serial, jdbcType=VARCHAR})")
	void saveWindowServer(WindowServer ws);

	@Select("SELECT * FROM WINDOW_SERVER where BORROWERID=#{userId} and STATUS='在借' ")
	List<WindowServer> queryInBorrowThing(@Param("userId") String userId);
	
	@Select("SELECT * FROM WINDOW_SERVER where TABLEID=#{tableId}")
	WindowServer queryByTableId(@Param("tableId") String tableId);

	@Update("update WINDOW_SERVER set STATUS=#{ws.status,jdbcType=VARCHAR},BORROWCONFIRM=#{ws.borrowConfirm,jdbcType=VARCHAR},RETURNTIME=#{ws.returnTime,jdbcType=VARCHAR},BACKCONFIRM=#{ws.backConfirm, jdbcType=VARCHAR} where TABLEID = #{ws.tableID, jdbcType=VARCHAR}")
	void updateByOBJ(@Param("ws") WindowServer wsobj);

	@Delete("DELETE FROM WINDOW_SERVER WHERE TABLEID=#{id}")
	void deleteByOBJ(@Param("id") String id);

	@Select("SELECT * FROM WINDOW_SERVER where BORROWERID=#{userId} and STATUS='在借' ")
	List<WindowServer> queryListByUserId(@Param("userId") String userId);

	@Select("SELECT * FROM WINDOW_SERVER_THING where SERIAL=#{serial}")
	QtfwThing getQtfwThingById(@Param("serial") String command);

	@Select("SELECT * FROM WINDOW_SERVER where SERIAL=#{serial} and STATUS='在借'")
	WindowServer queryInBorrowThingOne(@Param("serial")String command);

	@Select("SELECT * FROM WINDOW_SERVER where STATUS='在借' ORDER BY BORROWTIME DESC ")
	List<WindowServer> queryListByCondition();

	@Insert("INSERT INTO WINDOW_SERVER_THING(SERIAL,NAME,DEPARTNAME)VALUES(#{serial, jdbcType=VARCHAR},#{name, jdbcType=VARCHAR},#{departName, jdbcType=VARCHAR})")
	void addqtfw(QtfwThing qft);
	
	

}
