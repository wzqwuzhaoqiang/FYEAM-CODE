package com.fuyaogroup.eam.modules.fusion.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fuyaogroup.eam.common.service.BaseServiceImpl;
import com.fuyaogroup.eam.modules.fusion.dao.AssetBorrowMapper;
import com.fuyaogroup.eam.modules.fusion.model.AssetBorrow;
import com.fuyaogroup.eam.modules.fusion.service.AssetBorrowService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AssetBorrowServiceImpl extends BaseServiceImpl<AssetBorrowMapper, AssetBorrow> implements AssetBorrowService {
	
	@Autowired
	private AssetBorrowMapper abm;
	
	@Override
	public List<AssetBorrow> queryList() {
		// TODO Auto-generated method stub
		return abm.queryList();
	}


	
}
