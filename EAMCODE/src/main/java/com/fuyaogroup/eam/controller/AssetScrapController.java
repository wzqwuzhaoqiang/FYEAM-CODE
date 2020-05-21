package com.fuyaogroup.eam.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fuyaogroup.eam.common.enums.AssetTypeEnum;
import com.fuyaogroup.eam.common.model.Page;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetScrap;
import com.fuyaogroup.eam.modules.fusion.service.IAssetScrapService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin(origins = "*")
public class AssetScrapController {

	
	@Autowired
	private IAssetScrapService iass;
	
	public Page page;
	//private String ERROR_MESSAGE = "";//导入EXCEL页面提示
	private static Integer PageSize = 10;

	static Integer totalRow;
	
	@RequestMapping(value = "/getScrapRecordList",method = RequestMethod.POST)
	public @ResponseBody List<AssetScrap> getScrapRecordList(HttpServletRequest request, HttpServletResponse response){
		System.out.println("开始");
		return iass.queryScrapList();
	}
	public @ResponseBody Page turnPage(HttpServletRequest request, HttpServletResponse response) throws IOException{

		Integer turnpage = Integer.parseInt(request.getParameter("turnpage"));
		List<AssetScrap> list = new ArrayList<AssetScrap>();
		page.setCurrentPage(turnpage);
		page.setPageSize(PageSize);
		page.setStar((turnpage-1)*PageSize);
		list = iass.getAllScrapByPage(page);
//		Integer totalRow = assetSevice.queryTotalRows(AssetTypeEnum.CMP_ASSET);
		page.setTotalPage(totalRow/PageSize+(list.size()%PageSize>0?1:0));
		page.setDataList(list);
		return page;
	}
}
