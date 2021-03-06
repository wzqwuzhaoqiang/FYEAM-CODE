package com.fuyaogroup.eam.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuyaogroup.eam.common.enums.AssetTypeEnum;
import com.fuyaogroup.eam.common.enums.OrgEnum;
import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.common.model.Page;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetLifeRecored;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.model.QtfwThing;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.WindowServerService;
import com.fuyaogroup.eam.util.EnumUtil;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
import com.fuyaogroup.eam.util.ReadExcel;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class AssetManageController {
	@Autowired
	AssetService assetSevice;
	
	@Autowired
	AssetPdService assetPdService;
	
	@Autowired
	AssetPdBatService assetPdBat;
	
	@Autowired
	WindowServerService wss;
	
	@Autowired
	private FusionEAMAPIUtil fuEAMUtil=new FusionEAMAPIUtil();

	public Page page;
	private String ERROR_MESSAGE = "";//导入EXCEL页面提示
	private static Integer PageSize = 10;

	static Integer totalRow;//getLifeRecoredBySerialize
	
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	
	@RequestMapping(value = "/getLifeRecoredByNum",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody
	List<AssetLifeRecored> getLifeRecoredBySerialize(@RequestBody String request) throws IOException{

		JSONObject ap = (JSONObject) JSONArray.parse(request);
		String assetNumber = (String)ap.get("assetNumber");
		System.out.println("传入参数为：++++++"+assetNumber);
		List<AssetLifeRecored> list = new ArrayList<AssetLifeRecored>();
		List<AssetLifeRecored> repair = new ArrayList<AssetLifeRecored>();
		List<AssetLifeRecored> borrow = new ArrayList<AssetLifeRecored>();
		List<AssetLifeRecored> scrap = new ArrayList<AssetLifeRecored>();
		AssetLifeRecored as = assetSevice.getAssetByNumberr(assetNumber);
		if(as!=null) {
			as.setAction("接收启用");
			list.add(as);
		}
		//借用信息获取
		borrow = assetSevice.getBorrowRecored(assetNumber);
		if(!CollectionUtils.isEmpty(borrow)) {
			for(AssetLifeRecored alr:borrow) {
				alr.setAction("借出");
				alr.setStartDate(alr.getBorrowOutDate());
			}
			list.addAll(borrow);
		}
		//维修信息获取
		repair = assetSevice.getRepairRecored(assetNumber);
		if(!CollectionUtils.isEmpty(repair)) {
			for(AssetLifeRecored alr:repair) {
				alr.setAction("维修");
			}
			list.addAll(repair);
		}
		//报废信息获取
		scrap = assetSevice.getScrapRecored(assetNumber);
		if(!CollectionUtils.isEmpty(scrap)) {
			for(AssetLifeRecored alr:scrap) {
				alr.setAction("报废");
			}
			list.addAll(scrap);
		}
		
		list.sort(as);
		return list;
	}
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/assetList",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody
	List<Asset> list(HttpServletRequest request, HttpServletResponse response) throws IOException{
//		page = new Page();
		List<Asset> list = new ArrayList<Asset>();
		//页面初始化
//		page.setCurrentPage(1);
//		page.setPageSize(PageSize);
//		page.setStar(0);
//		list = assetSevice.getAllCmpAssetByPage(AssetTypeEnum.CMP_ASSET,page);
//		 totalRow = assetSevice.queryTotalRows(AssetTypeEnum.CMP_ASSET);
//		page.setTotalPage(totalRow/PageSize+(list.size()%PageSize>0?1:0));
//		page.setDataList(list);
		list = assetSevice.getAllCmpAsset(AssetTypeEnum.CMP_ASSET);
		return list;
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/updateAssetData",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody String getAssetData(@RequestBody String request) throws IOException{
		System.out.println("///////////////////////////////----------------------------");
		System.out.println("///////////////////////////////----------------------------");
		System.out.println("///////////////////////////////----------------------------");
		System.out.println("///////////////////////////////----------------------------");
		System.out.println("///////////////////////////////----------------------------");
		System.out.println("///////////////////////////////-"+request);
		log.info("{}:修改资产信息,开始...",LocalDateTime.now());
		List<Asset> atList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,Asset.class);
		if(CollectionUtil.isEmpty(atList)){
			log.error("{}:修改资产信息,失败:转换失败！");
			return "修改资产信息,失败:转换失败";
		}
		try {
			boolean teg = true;
			System.out.println("///////////////////////////////----------------------------");
			//fuEAMUtil.updateOneAsset(atList.get(0));
			//fuEAMUtil.updateAssetDescriptiveFields(atList.get(0), false);
			
			
			Date nowDate  = myFormatter.parse(myFormatter.format(new Date()));
			List<AssetPdBat> nowTimeList = assetPdBat.getAllBDate(nowDate);
			if(CollectionUtils.isEmpty(nowTimeList)) {
				atList.get(0).setUpdateTime(new Date());
				atList.get(0).setChangeTime(new Date());
				assetSevice.updateOne(atList.get(0));
				return "修改成功";
			}else {
				for(AssetPdBat apd :nowTimeList) {
					AssetPd pd = assetPdService.getBySerialNumAndBatid(atList.get(0).getSerialNumber(),apd.getPdBatId());
					if(pd!=null) {
						if(pd.getOrganizationName().equals(atList.get(0).getOrganizationName())) {
							//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
							pd = this.createPd(atList.get(0),pd);
							assetPdService.updateAssetPd(pd);
						}else {
							assetPdService.deleteAssetPd(pd.getPdCode().toString());
							//盘点里的组织不等于更改后的组织，所以要删除此条盘点信息
						}
						Asset asset = atList.get(0);
						if(asset.getStatus()==2) {
							assetPdService.deleteAssetPd(pd.getPdCode().toString());
						}
					}
				}
				Asset asset = atList.get(0);
				if(asset.getStatus()!=2) {
					for(AssetPdBat apd :nowTimeList) {
						if(apd.getOrgList().contains(atList.get(0).getOrganizationName())) {
							//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
							AssetPd pd = assetPdService.getBySerialNumAndBatid(asset.getSerialNumber(),apd.getPdBatId());
							if(pd==null) {
								assetPdService.createAllAssetPd(Long.parseLong(apd.getPdBatId()), atList);
							}
							
						}
				}
				}
				
				//修改的资产不在盘点范围内
				atList.get(0).setUpdateTime(new Date());
				atList.get(0).setChangeTime(new Date());
				assetSevice.updateOne(atList.get(0));
			}
//			List<AssetPdBat> list = assetPdBat.queryByDateOfAssetPdBat(nowDate,atList.get(0).getOrganizationName());
//			if(!CollectionUtils.isEmpty(list)) {
//				assetPdService.createAllAssetPd(Long.parseLong(list.get(0).getPdBatId()), atList);
//			}
//			List<AssetPd> pdList = assetPdService.getBySerialNum(atList.get(0).getSerialNumber(), PdStatusEnum.ASSET_PD_WAITING.getCode());
//			if(!CollectionUtil.isEmpty(pdList)){
//				AssetPd pd= pdList.get(0);
//				pd = this.createPd(atList.get(0),pd);
//				assetPdService.updateAssetPd(pd);
//				}else{
//					List<Asset> assets = assetSevice.getByAssetNumber(atList.get(0).getAssetNumber());
//					if(CollectionUtils.isNotEmpty(assets)){
//						if(assets.get(0).getSerialNumber()!=null) {
//							pdList = assetPdService.getBySerialNum(assets.get(0).getSerialNumber(), PdStatusEnum.ASSET_PD_WAITING.getCode());
//						}else {
//							pdList = assetPdService.getBySerialNum(atList.get(0).getSerialNumber(), PdStatusEnum.ASSET_PD_WAITING.getCode());
//						}
//					
//					if(!CollectionUtil.isEmpty(pdList)){
//						AssetPd pd= pdList.get(0);
//						pd = this.createPd(atList.get(0),pd);
//						assetPdService.updateAssetPd(pd);
//						}
//					}
//				}
		} catch (Exception e) {
			log.error("{}:修改资产信息,失败:",e.getMessage());
			System.out.println(e.getMessage());
			return "修改失败："+e.getMessage();

		}
			log.info("{}:修改资产信息,结束...",LocalDateTime.now());
			return "修改成功";
	}

	/**
	 * 软件信息修改控制器
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/softAssetUpdateSubmit",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody String softAssetUpdateSubmit(@RequestBody String request) throws IOException{
		System.out.println("///////////////////////////////----------------------------");
		System.out.println("///////////////////////////////-"+request);
		log.info("{}:修改软件资产信息,开始...",LocalDateTime.now());
		List<Asset> atList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,Asset.class);
		if(CollectionUtil.isEmpty(atList)){
			log.error("{}:修改资产信息,失败:转换失败！");
			return "修改资产信息,失败:转换失败";
		}
		try {
			
			System.out.println("///////////////////////////////----------------------------");
			Asset asset=atList.get(0);
			
			if(asset.getWarrantyreminderdate()==null) {
				
				Date dt  = asset.getWarrantdate();
				String d1 = myFormatter.format(dt);
				Calendar calendar1 = Calendar.getInstance();
				calendar1.setTime(dt);
				calendar1.add(Calendar.DATE, -20);
				dt = calendar1.getTime();
            asset.setWarrantyreminderdate(dt);
            asset.setWarrantdate(myFormatter.parse(d1));
			}
			asset.setUpdateTime(new Date());
			asset.setChangeTime(new Date());
			assetSevice.updateSoftAssetOne(asset);
		} catch (Exception e) {
			log.error("{}:修改资产信息,失败:",e.getMessage());
			System.out.println(e.getMessage());
			return "修改失败："+e.getMessage();

		}
			log.info("{}:修改资产信息,结束...",LocalDateTime.now());
			return "修改成功";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/addAssetData",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody String addAssetData(@RequestBody String request) throws IOException{
		log.info("{}:新增资产信息,开始...",LocalDateTime.now());
		List<Asset> atList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,Asset.class);
		if(CollectionUtil.isEmpty(atList)){
			log.error("{}:新增资产信息,失败:转换失败！");
			return "新增资产信息,失败:转换失败";
		}
		try {
			//Asset asset = this.AddOneAssetToFusion(atList.get(0));
			Asset asset = atList.get(0);
			asset.setAssetType(AssetTypeEnum.CMP_ASSET.getCode());
			assetSevice.createOne(asset);
			Date nowDate  = myFormatter.parse(myFormatter.format(new Date()));
			List<AssetPdBat> nowTimeList = assetPdBat.getAllBDate(nowDate);
			if(!CollectionUtils.isEmpty(nowTimeList)) {
				for(AssetPdBat apd :nowTimeList) {
					if(apd.getOrgList().contains(asset.getOrganizationName())) {
						//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
						assetPdService.createAllAssetPd(Long.parseLong(apd.getPdBatId()), atList);
					}
			}
			}
		} catch (Exception e) {
			log.error("{}:增加资产信息,失败:",e.getMessage());
			return "增加失败："+e.getMessage();

		}
			log.info("{}:修改资产信息,结束...",LocalDateTime.now());
			return "增加成功";
	}

	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/softAssetAddSubmit",method = RequestMethod.POST)
	@ResponseBody
	public String softAssetAddSubmit(@RequestBody String request) {
		log.info("{}:新增软件资产信息,开始...",LocalDateTime.now());
		System.out.println("{}:新增软件资产信息,开始..."+LocalDateTime.now());
		
		List<Asset> atList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,Asset.class);
		if(CollectionUtil.isEmpty(atList)){
			log.error("{}:新增资产信息,失败:转换失败！");
			return "新增资产信息,失败:转换失败";
		}
		try {
			
			//Asset asset = this.AddOneAssetToFusion(atList.get(0));
			Asset asset = atList.get(0);
			asset.setAssetType(AssetTypeEnum.CMP__SOFT_ASSET.getCode());
if(asset.getWarrantyreminderdate()==null) {
				
				Date dt  = asset.getWarrantdate();
				String d1 = myFormatter.format(dt);
				Calendar calendar1 = Calendar.getInstance();
				calendar1.setTime(dt);
				calendar1.add(Calendar.DATE, -20);
				dt = calendar1.getTime();
				
            asset.setWarrantyreminderdate(dt);
            asset.setWarrantdate(myFormatter.parse(d1));
			}
			assetSevice.createOne(asset);
		} catch (Exception e) {
			log.error("{}:增加资产信息,失败:",e.getMessage());
			return "增加失败："+e.getMessage();

		}
			log.info("{}:修改资产信息,结束...",LocalDateTime.now());
			return "增加成功";
		
	}
	
	
	
	
	private AssetPd createPd(Asset asset, AssetPd pd) {
		pd.setAssetNumber(asset.getAssetNumber());
		pd.setSerialNumber(asset.getSerialNumber());
		pd.setUserName(asset.getUsername());
		pd.setDepartment(asset.getWorkCenterName());
		pd.setOrganizationName(asset.getOrganizationName());
		pd.setJobNum(asset.getJobnum());
		pd.setDescription(asset.getDescription());
		pd.setAssetModel(asset.getAssetmodel());
		pd.setAllocation(asset.getAllocation());
		return pd;
	}
	public @ResponseBody Page turnPage(HttpServletRequest request, HttpServletResponse response) throws IOException{

		Integer turnpage = Integer.parseInt(request.getParameter("turnpage"));
		List<Asset> list = new ArrayList<Asset>();
		page.setCurrentPage(turnpage);
		page.setPageSize(PageSize);
		page.setStar((turnpage-1)*PageSize);
		list = assetSevice.getAllCmpAssetByPage(AssetTypeEnum.CMP_ASSET,page);
//		Integer totalRow = assetSevice.queryTotalRows(AssetTypeEnum.CMP_ASSET);
		page.setTotalPage(totalRow/PageSize+(list.size()%PageSize>0?1:0));
		page.setDataList(list);
		return page;
	}


		@RequestMapping(value = "/importAssets",method = RequestMethod.POST, produces = "application/json; charset=utf-8")//, produces = "application/json; charset=utf-8")
	public @ResponseBody String importAssets(HttpServletRequest request) throws IOException{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	String msg = "添加成功";
	// 获得文件
	MultipartFile multipartFile = multipartRequest.getFile("file");// 与前端设置的fileDataName
	
		List<Asset> list = new ArrayList<Asset>();
		String fileName=multipartFile.getOriginalFilename();
		try {
			list = new ReadExcel().getExcelInfo(fileName, multipartFile);
		} catch (Exception e) {
			msg = e.getMessage();
			return msg;
		}
		List<Asset> assetList = this.createAllAssetToFusion(list);
		if(assetList.size()>0)
			this.createAllAsset(assetList);
		else{
			this.createAllAsset(list);
		}
		
		if(ERROR_MESSAGE.isEmpty()){
			return msg;
		}
		return ERROR_MESSAGE;
	}
		
		/**
		 * 前台资产导入
		 * @param request
		 * @return
		 * @throws IOException
		 */
		@RequestMapping(value = "/importAssetsqtzc",method = RequestMethod.POST, produces = "application/json; charset=utf-8")//, produces = "application/json; charset=utf-8")
		public @ResponseBody String importAssetsqtzc(HttpServletRequest request) throws IOException{
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String msg = "添加成功";
		// 获得文件
		MultipartFile multipartFile = multipartRequest.getFile("file");// 与前端设置的fileDataName
		
			List<QtfwThing> list = new ArrayList<QtfwThing>();
			String fileName=multipartFile.getOriginalFilename();
			try {
				list = new ReadExcel().getExcelInfoqtzc(fileName, multipartFile);
				for(QtfwThing qft:list) {
					wss.addqtfw(qft);
				}
			} catch (Exception e) {
				msg = e.getMessage();
				return msg;
			}
			if(ERROR_MESSAGE.isEmpty()){
				return msg;
			}
			return ERROR_MESSAGE;
		}
	
		@RequestMapping(value = "/importSoftAssets",method = RequestMethod.POST, produces = "application/json; charset=utf-8")//, produces = "application/json; charset=utf-8")
		public @ResponseBody String importSoftAssets(HttpServletRequest request) throws IOException{
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String msg = "添加成功";
		// 获得文件
		MultipartFile multipartFile = multipartRequest.getFile("file");// 与前端设置的fileDataName
		
		 
			List<Asset> list = new ArrayList<Asset>();
			String fileName=multipartFile.getOriginalFilename();
			System.out.println("文件名：、、、、、、、、、"+fileName);
			try {
				list = new ReadExcel().getExcelInfoOfSoft(fileName, multipartFile);
				System.out.println("内容数据--------------"+list.toString());
			} catch (Exception e) {
				msg = "导入数据转换出错";
				return msg;
			}
			this.createAllSoftAsset(list);
//			List<Asset> assetList = this.createAllAssetToFusion(list);
//			if(assetList.size()>0)
//				this.createAllAsset(assetList);
//			else{
//				this.createAllAsset(list);
//			}
			
			if(ERROR_MESSAGE.isEmpty()){
				return msg;
			}
			return ERROR_MESSAGE;
		}
		
		
		
		
	@Async
	private void createAllAsset(List<Asset> list) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for(Asset asset:list){
			try {
				asset.setAssetType(AssetTypeEnum.CMP_ASSET.getCode());
				assetSevice.createOne(asset);
			} catch(DataAccessException d1){
				sb.append("增加资产："+asset.getAssetNumber()+"失败,原因"+d1.getMessage()+";");
				log.error(d1.toString());
			}
			
		}
	}
	
	@Async
	private void createAllSoftAsset(List<Asset> list) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for(Asset asset:list){
			try {
				asset.setAssetType(AssetTypeEnum.CMP__SOFT_ASSET.getCode());
				assetSevice.createOne(asset);
			} catch(DataAccessException d1){
				sb.append("增加资产："+asset.getAssetNumber()+"失败,原因"+d1.getMessage()+";");
				log.error(d1.toString());
			}
			
		}
	}
	
	

	@Async
	private List<Asset> createAllAssetToFusion(List<Asset> list) {
		List<Asset> assetList = new ArrayList<Asset>();
		StringBuffer sb = new StringBuffer();
		for(Asset asset:list){
			try{
			Asset as=this.AddOneAssetToFusion(asset);
			assetList.add(as);
			}catch(Exception e){
				sb.append("增加资产："+asset.getAssetNumber()+"失败,原因"+e.getMessage()+";");
				log.error("增加资产："+asset.getAssetNumber()+"失败,原因"+e.toString());
			}
			
		}
		ERROR_MESSAGE = sb.toString();
		return assetList;
		
	}

	

	private Asset AddOneAssetToFusion(Asset asset) throws Exception {
		log.info("增加资产："+asset.getAssetNumber()+",开始！");
		Asset asset_new;
		try {
			asset.setLocationOrganizationId(EnumUtil.getByCode(asset.getOrganizationName(), OrgEnum.class).getMessage());
			asset.setOrganizationCode(asset.getLocationOrganizationId());
			Asset assetFromFusion=fuEAMUtil.getOneAssetBySerNum(asset.getSerialNumber());
			if(assetFromFusion==null){
				 asset_new = fuEAMUtil.createOneAsset(asset);
			}else{
				asset.setAssetId(assetFromFusion.getAssetId());
				asset.setAssetNumber(assetFromFusion.getAssetNumber());
//				fuEAMUtil.updateOneAsset(asset);
				asset.setAssetType(AssetTypeEnum.CMP_ASSET.getCode());
				fuEAMUtil.createAssetDescriptiveField(asset);
				asset_new=asset;
			}
			log.info("增加资产："+asset.getAssetNumber()+",结束！");
			
			return asset_new;
		} catch (Exception e) {
			throw e;
		}
		
	}
	@RequestMapping(value = "/assetSoftList",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody List<Asset> softlist(HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<Asset> list = new ArrayList<Asset>();
		list = assetSevice.getAllCmpAsset(AssetTypeEnum.CMP__SOFT_ASSET);
		return list;
	}
	
	@RequestMapping(value = "/getBeShowData",method = RequestMethod.POST)
	public @ResponseBody List<Map<String,Object>> getBeShowData(HttpServletRequest request, HttpServletResponse response) throws IOException{
		System.out.println("zhixxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	
		return assetSevice.getBeShowData();
	}
	
	
	@RequestMapping(value = "/deleteSoftAsset",method = RequestMethod.POST)
	public @ResponseBody String deleteSoftAsset(@RequestBody String request) throws IOException{
		System.out.println("zhixxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
	
		List<Asset> atList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,Asset.class);
		Asset asset = atList.get(0);
		int result = assetSevice.deleteSoft(asset.getAssetNumber());
		if(result>0) {
			return "软件删除成功";
		}else {
			return "删除失败";
		}
	}
	
	
}
