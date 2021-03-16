package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.WindowServerMapper;
import com.fuyaogroup.eam.modules.fusion.model.QtfwThing;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
import com.fuyaogroup.eam.modules.fusion.service.WindowServerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WindowServerServiceImpl extends BaseServiceImpl<WindowServerMapper, WindowServer> implements WindowServerService{
	
	@Autowired
	private WindowServerMapper wsm;
	
	@Override
	public void saveWindowServer(WindowServer ws) {
		// TODO Auto-generated method stub
		wsm.saveWindowServer(ws);
	}

	@Override
	public List<WindowServer> queryInBorrowThing(String userId) {
		// TODO Auto-generated method stub
		return wsm.queryInBorrowThing(userId);
	}

	@Override
	public WindowServer queryByTableId(String idvalue) {
		// TODO Auto-generated method stub
		return wsm.queryByTableId(idvalue);
	}

	@Override
	public void comfirmBorrow(WindowServer wsobj) {
		wsm.updateByOBJ(wsobj);
		
	}

	@Override
	public void cancel(String id) {
		wsm.deleteByOBJ(id);
		
	}

	@Override
	public List<WindowServer> queryListByUserId(String userId) {
		// TODO Auto-generated method stub
		return wsm.queryListByUserId(userId);
	}

	@Override
	public QtfwThing getQtfwThingById(String command) {
		// TODO Auto-generated method stub
		return wsm.getQtfwThingById(command);
	}

	@Override
	public WindowServer queryInBorrowThingOne(String command) {
		// TODO Auto-generated method stub
		return wsm.queryInBorrowThingOne(command);
	}

	@Override
	public List<WindowServer> queryListByCondition() {
		// TODO Auto-generated method stub
		return wsm.queryListByCondition();
	}

	@Override
	public void addqtfw(QtfwThing qft) {
		// TODO Auto-generated method stub
		wsm.addqtfw(qft);
	}

	@Override
	public WindowServer getSQrecord(String command) {
		// TODO Auto-generated method stub
		return wsm.getSQrecord(command);
	}

	@Override
	public List<WindowServer> queryAllBorrowByCondition() {
		// TODO Auto-generated method stub
		return wsm.queryAllBorrowByCondition();
	}

	@Override
	public List<WindowServer> queryAllReturnByCondition() {
		// TODO Auto-generated method stub
		return wsm.queryAllReturnByCondition();
	}

	@Override
	public WindowServer getReturnSQrecord(String command) {
		// TODO Auto-generated method stub
		return wsm.getReturnSQrecord(command);
	}

	@Override
	public List<WindowServer> queryAll() {
		// TODO Auto-generated method stub
		
		return wsm.queryAll();
	}

	@Override
	public List<WindowServer> queryAllByDescBorrowTime() {
		// TODO Auto-generated method stub
		return wsm.queryAllByDescBorrowTime();
	}

	@Override
	public Map<String, Object> getHistoryList(Page<WindowServer> pageCourse) {
		// TODO Auto-generated method stub
		
		QueryWrapper<WindowServer> wrapper = new QueryWrapper<>();
		wrapper.orderByAsc("BORROWTIME");
        baseMapper.selectPage(pageCourse, wrapper);

        List<WindowServer> records = pageCourse.getRecords();
        //当前页数
        long current = pageCourse.getCurrent();
        //总页数
        long pages = pageCourse.getPages();
        //每页记录数
        long size = pageCourse.getSize();
        //总记录数
        long total = pageCourse.getTotal();
        //是否有下一页
        boolean hasNext = pageCourse.hasNext();
        //是否有上一页
        boolean hasPrevious = pageCourse.hasPrevious();

        //把分页数据放到map集合
        HashMap<String, Object> map = new HashMap<>();
        map.put("items",records );
        map.put("current",current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);
        return map;
	}

	@Override
	public void updateWindowServer(WindowServer ws) {
		wsm.updateWindowServer(ws);
		
	}

	@Override
	public void updateWindowServerThing(QtfwThing qtfwThing) {
		// TODO Auto-generated method stub
		wsm.updateWindowServerThing(qtfwThing);
	}

}
