package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.BookBorrowDetial;
import com.fuyaogroup.eam.modules.fusion.model.BookEntry;
import com.fuyaogroup.eam.modules.fusion.model.vo.BookInfoVo;

public interface BookServerService extends IBaseService<BookBorrowDetial>{
	

	public void insertBookDetial(BookInfoVo b);
	
	public BookEntry getBookDetial(String id);

	public int selectCount(String command, int i);

	public void saveBook(BookBorrowDetial ws);

	public int selectCountManId(String userId, int i);

	public List<BookBorrowDetial> selectlistManId(String userId, int i);

	public BookBorrowDetial selectGetById(String command);

	public void updateBookById(BookBorrowDetial ws);

	public List<BookBorrowDetial> selectList(int i);

	public void updateBook(BookBorrowDetial wss);
	
}
