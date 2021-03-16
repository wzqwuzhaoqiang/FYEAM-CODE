package com.fuyaogroup.eam.modules.fusion.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuyaogroup.eam.modules.fusion.model.BookBorrowDetial;
import com.fuyaogroup.eam.modules.fusion.model.BookEntry;
import com.fuyaogroup.eam.modules.fusion.model.vo.BookInfoVo;

@Mapper
public interface BookServerMapper extends BaseMapper<BookBorrowDetial>{

	@Insert("INSERT INTO \"book_detial\"(\"bid\",\"bookName\",\"clazzName\",\"bookCode\",\"department\",\"state\") VALUES(#{bf.id, jdbcType=VARCHAR},#{bf.bookName, jdbcType=VARCHAR},#{bf.clazzName, jdbcType=VARCHAR},#{bf.bookCode, jdbcType=VARCHAR},#{bf.department, jdbcType=VARCHAR},#{bf.state, jdbcType=VARCHAR})")
	void insertBookDetial(@Param("bf")BookInfoVo b);

	@Select("SELECT * FROM \"book_detial\" where \"bookCode\"=#{bookCode} and \"state\"='0' ")
	BookEntry getBookDetial(String id);

	@Select("SELECT count(\"bid\") FROM \"book_borrowDetial\" where \"bookID\"=#{command} and \"state\"!=#{i} ")
	int selectCount(@Param("command")String command, @Param("i")int i);

	@Insert("INSERT INTO \"book_borrowDetial\" VALUES(#{bf.bid, jdbcType=VARCHAR},#{bf.manID, jdbcType=VARCHAR},#{bf.name, jdbcType=VARCHAR},#{bf.bookName, jdbcType=VARCHAR},#{bf.bookID, jdbcType=VARCHAR},"
			+ "#{bf.telphone, jdbcType=VARCHAR},#{bf.btime, jdbcType=VARCHAR},"
			+ "#{bf.rtime, jdbcType=VARCHAR},#{bf.state, jdbcType=NUMERIC})")
	void saveBook(@Param("bf")BookBorrowDetial ws);
	
	@Select("SELECT count(\"bid\") FROM \"book_borrowDetial\" where \"manID\"=#{command} and \"state\"!=#{i} ")
	int selectCountManId(@Param("command")String userId, @Param("i")int i);
	
	@Select("SELECT * FROM \"book_borrowDetial\" where \"manID\"=#{command} and \"state\"!=#{i} ")
	List<BookBorrowDetial> selectlistManId(@Param("command")String userId, @Param("i")int i);

	@Select("SELECT * FROM \"book_borrowDetial\" where \"bid\"=#{command}")
	BookBorrowDetial selectGetById(@Param("command")String command);

	@Update("update \"book_borrowDetial\" set \"rtime\"=#{bf.rtime, jdbcType=VARCHAR},\"state\"=#{bf.state, jdbcType=NUMERIC} where \"bid\"=#{bf.bid, jdbcType=VARCHAR}")
	void updateBoookById(@Param("bf")BookBorrowDetial ws);

	@Select("SELECT * FROM \"book_borrowDetial\" where \"state\"=#{i}")
	List<BookBorrowDetial> updateBookById(int i);

	@Update("update \"book_borrowDetial\" set \"state\"=#{bf.state, jdbcType=NUMERIC} where \"bid\"=#{bf.bid, jdbcType=VARCHAR}")
	void updateBook(@Param("bf")BookBorrowDetial wss);

	

	

}
