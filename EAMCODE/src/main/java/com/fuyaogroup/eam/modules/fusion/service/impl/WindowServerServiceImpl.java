package com.fuyaogroup.eam.modules.fusion.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.WindowServerMapper;
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

}
