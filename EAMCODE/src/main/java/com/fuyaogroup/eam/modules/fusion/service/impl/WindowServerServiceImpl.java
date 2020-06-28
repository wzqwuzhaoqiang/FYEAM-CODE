package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
