package com.fuyaogroup.eam.modules.fusion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.common.service.IBaseService;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;

@Service
public interface WindowServerService extends IBaseService<WindowServer>{

	void saveWindowServer(WindowServer ws);

	List<WindowServer> queryInBorrowThing(String userId);

	WindowServer queryByTableId(String idvalue);

	void comfirmBorrow(WindowServer wsobj);

	void cancel(String id);

}
