package com.fuyaogroup.eam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuyaogroup.eam.modules.fusion.model.AssetBorrow;
import com.fuyaogroup.eam.modules.fusion.service.AssetBorrowService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class AssetBorrowController {

	@Autowired
	private AssetBorrowService abs;
	@PostMapping("/getBorrowRecordList")
	public List<AssetBorrow> getBorrowRecordList(){
		
		return abs.queryList();
	}
}
