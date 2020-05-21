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
		
//		String str = "insert into asset_borrow_trackrecord (OAID,company,formDate,borrower,department,contactInfor,borrowThing,number,purpose,assertNumber,assertName,model,serialNumber,configInfo,startDate,borrowOutDate,borrowUseDate,borrowOutman,returnIs,returntwoIs,renewDateNumber,thingSituation,reciver,returnDate)values(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)";
//		System.out.println(str.toUpperCase());
		return abs.queryList();
	}
}
