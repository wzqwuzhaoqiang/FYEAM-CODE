package com.fuyaogroup.eam.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuyaogroup.eam.modules.fusion.model.RepairRecord;
import com.fuyaogroup.eam.modules.fusion.service.IRepairRecordService;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.util.StringUtil;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class RepairRecordController {

	@Autowired
	private IRepairRecordService irrs;
	
	@RequestMapping(value = "/getRepairRecordList",method = RequestMethod.POST)
	public @ResponseBody List<RepairRecord> list(HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<RepairRecord> resultList = new ArrayList<RepairRecord>();
		resultList = irrs.queryList();
		return resultList;
	}
	
	@RequestMapping(value = "/getOneByAssetNumber",method = RequestMethod.POST)
	public @ResponseBody List<RepairRecord> listByAssetNumber(@RequestBody String request) throws IOException{
		List<RepairRecord> resultList = new ArrayList<RepairRecord>();
		if(!StringUtil.isEmpty(request)) {
			JSONObject ap = (JSONObject) JSONArray.parse(request);
			String assetNumber = (String)ap.get("assetNumber");
			resultList = irrs.queryListByAssetNumber(assetNumber);
		}
		return resultList;
	}
}
