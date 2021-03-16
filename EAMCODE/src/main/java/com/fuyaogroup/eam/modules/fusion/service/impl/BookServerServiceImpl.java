package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.BookServerMapper;
import com.fuyaogroup.eam.modules.fusion.model.BookBorrowDetial;
import com.fuyaogroup.eam.modules.fusion.model.BookEntry;
import com.fuyaogroup.eam.modules.fusion.model.vo.BookInfoVo;
import com.fuyaogroup.eam.modules.fusion.service.BookServerService;

@Service
public class BookServerServiceImpl extends BaseServiceImpl<BookServerMapper, BookBorrowDetial> implements BookServerService{

	@Autowired
	private BookServerMapper bsm;
	
	@Override
	public void insertBookDetial(BookInfoVo b) {
		bsm.insertBookDetial(b);
		
	}

	@Override
	public BookEntry getBookDetial(String id) {
		
		return bsm.getBookDetial(id);
	}

	@Override
	public int selectCount(String command, int i) {
		// TODO Auto-generated method stub
		return bsm.selectCount(command,i);
	}

	@Override
	public void saveBook(BookBorrowDetial ws) {
		// TODO Auto-generated method stub
		bsm.saveBook(ws);
	}

	@Override
	public int selectCountManId(String userId, int i) {
		// TODO Auto-generated method stub
		return bsm.selectCountManId(userId,i);
	}

	@Override
	public List<BookBorrowDetial> selectlistManId(String userId, int i) {
		// TODO Auto-generated method stub
		return bsm.selectlistManId(userId,i);
	}

	@Override
	public BookBorrowDetial selectGetById(String command) {
		// TODO Auto-generated method stub
		return bsm.selectGetById(command);
	}

	@Override
	public void updateBookById(BookBorrowDetial ws) {
		// TODO Auto-generated method stub
		bsm.updateBoookById(ws);
	}

	@Override
	public List<BookBorrowDetial> selectList(int i) {
		// TODO Auto-generated method stub
		return bsm.updateBookById(i);
	}

	
	@Override
	public void updateBook(BookBorrowDetial wss) {
		// TODO Auto-generated method stub
		bsm.updateBook(wss);
	}



}
