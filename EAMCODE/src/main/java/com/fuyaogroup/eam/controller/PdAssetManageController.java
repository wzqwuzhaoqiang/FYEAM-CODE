package com.fuyaogroup.eam.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.mapper.util.StringUtil;
import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.common.model.Page;
import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.common.service.WeixinService;
import com.fuyaogroup.eam.modules.fusion.dao.AssetrPdMapper;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;

@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class PdAssetManageController {
	
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	static SimpleDateFormat batFormatter = new SimpleDateFormat("yyMMdd", Locale.CHINA);

	static SimpleDateFormat infoFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
	static String EXCEL_PATH="./excel/";
//	static String IMG_PATH="./data/eamapi/img/";
	static String IMG_PATH="D:\\data\\eamapi\\img\\";
	
	   @Value("${file.img.path}")
		private  String filePath;
	   
	   @Value("${file.RBimg.path}")
		private  String rbfilePath;
	   
	   @Value("${file.RBimg.url}")
		private  String rbfileUrl;
		
	   @Value("${file.img.url}")
	 		private  String fileUrl;
	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	String startDate;
	String endDate;
	String assetNum;
	String centerName;
	List<AssetPdBat> list=null;
	List<AssetPd> listPd=null;
	static Integer PAGE_SIZE=10;//每页的条数-10条
	Page page = new Page();
	String currentPage;
	
	@Autowired
	WeixinMessageService wxService;
	
	@Autowired
	WeixinService wx;
	
	@Autowired
	AssetPdBatService assetPdBatSevice;
	
	@Autowired
	AssetService assetSevice;
	
	@Autowired
	AssetPdService assetPdSevice;
	
	@Autowired
	AssetrPdMapper assetPd;
	
	@Autowired
	private FusionEAMAPIUtil fuEAMUtil=new FusionEAMAPIUtil();
	
	@RequestMapping(value = "/assetPdBatList",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AssetPdBat> list(HttpServletRequest request, HttpServletResponse response) throws IOException{
		log.info("{}:查询盘点批次列表,开始...",LocalDateTime.now());
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");//增加后，页面中文不乱码
		
		//显示列表
		try {
			list=assetPdBatSevice.getAll();//不是下载
			if(CollectionUtil.isEmpty(list)){
				list = new ArrayList<AssetPdBat>();
			}
			} catch (Exception e) {
				log.error(this.getClass().getName()+"：查询列表失败："+e.getMessage());
			}
		for(int i=0;i<list.size();i++) {
			System.out.println("时间："+list.get(i).toString());
		}
		return list;
		 //刚开始的页面为第一页
//	    if (page.getCurrentPage() == null){
//	        page.setCurrentPage(1);
//	    } else {
//	        page.setCurrentPage(page.getCurrentPage());
//	    }
//	    //设置每页数据为十条
//	    page.setPageSize(PAGE_SIZE);
//	    //每页的开始数
//	    page.setStar((page.getCurrentPage() - 1) * page.getPageSize());
//	    //list的大小
//	    int count = list.size();
//	    //设置总页数
//	    page.setTotalPage(count % 10 == 0 ? count / 10 : count / 10 + 1);
//	    //设置作用域
//	   page.setDataList(list);
//	    request.setAttribute("paging", page);  
//	    try {
//			request.getRequestDispatcher("WEB-INF/view/assetPdBat.jsp").forward(request, response);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	    
//	    log.info("{}:查询盘点批次列表,结束...",LocalDateTime.now());
//	    return page;
//	    request.setAttribute("startDate", startDate);
//		request.setAttribute("endDate", endDate);

	}
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/assetPdList",method = RequestMethod.POST)
	public @ResponseBody List<AssetPd> pdList(@RequestBody String request) throws IOException{
		log.info("{}:查询盘点列表,开始...",LocalDateTime.now());
		//显示列表
		String batId = null;
		if(StringUtil.isNotEmpty(request)){
			JSONObject ap = (JSONObject) JSONArray.parse(request);
			batId = ap.get("pdBatId")==null?null:ap.get("pdBatId").toString();
		}
			//显示列表
			try {
				listPd=assetPdSevice.getbyBatId(batId==null?null:Long.parseLong(batId));
				
				if(CollectionUtil.isEmpty(listPd)){
					listPd = new ArrayList<AssetPd>();
				}
				} catch (Exception e) {
					log.error(this.getClass().getName()+"：查询列表失败："+e.getMessage());
				}
			log.info("{}:查询盘点列表,结束...",LocalDateTime.now());
			return listPd;
		 //刚开始的页面为第一页
//	    if (page.getCurrentPage() == null){
//	        page.setCurrentPage(1);
//	    } else {
//	        page.setCurrentPage(page.getCurrentPage());
//	    }
//	    //设置每页数据为十条
//	    page.setPageSize(PAGE_SIZE);
//	    //每页的开始数
//	    page.setStar((page.getCurrentPage() - 1) * page.getPageSize());
//	    //list的大小
//	    int count = listPd.size();
//	    //设置总页数
//	    page.setTotalPage(count % 10 == 0 ? count / 10 : count / 10 + 1);
//	    //设置作用域
//	   page.setDataList(listPd.subList(page.getStar(),page.getTotalPage()-page.getStar()>page.getPageSize()?page.getStar()+page.getPageSize():page.getTotalPage()));
//	   request.setCharacterEncoding("UTF-8");
//	    request.setAttribute("paging", page);  
//	    try {
//			request.getRequestDispatcher("WEB-INF/view/assetPdList.jsp").forward(request, response);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	    
//	    return listPd;
	}
	
	@RequestMapping(value = "/turnPdPage",method = RequestMethod.POST)
	public @ResponseBody Page turnPage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");//增加后，页面中文不乱码
		
		String str=request.getParameter("currentPage");
		String turnPage = request.getParameter("page");
		if(turnPage.contentEquals("0")){
			page.setCurrentPage(1);
		}else if(turnPage.contentEquals("1")){
			page.setCurrentPage(page.getCurrentPage()+1);
		}else if(turnPage.contentEquals("-1")){
			page.setCurrentPage(page.getCurrentPage()-1);
		}
		page.setCurrentPage(page.getCurrentPage());
		//每页的开始数
	    page.setStar((page.getCurrentPage() - 1) * page.getPageSize());
			page.setDataList(list);
	   
		 return page;
		
	}
	@RequestMapping(value = "/toPdBatPage",method = RequestMethod.GET)
	public void toPage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();                
        out.println("<html>");       
        out.println("<script>");
        out.println("window.open ('" + request.getContextPath() + "/eam/assetPdBatList");
        out.println("</script>");
        out.println("</html>");

	}
	
	@RequestMapping(value = "/toAddPdBatPage",method = RequestMethod.GET)
	public String toAddBatPage(HttpServletRequest request,HttpServletResponse response){
		//初始化开始日期和结束日期，查询7天以内数据
		 return "WEB-INF/view/addBat.jsp";
	}
	
	@RequestMapping(value = "/udpateStatus",method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	public String udpateStatus(@RequestBody String request) throws Exception{
		JSONObject ap = (JSONObject) JSONArray.parse(request);
		log.info("{}更新状态为已盘点，开始！",ap.get("pdCode"));
		if(ap.get("pdCode")==null){
			log.error("编码为空，更新失败!");
			return "编码为空，更新失败!";
		}
		try{
		List<AssetPd> pdList = assetPd.queryByPdCode( Long.parseLong(ap.get("pdCode").toString()));
		if(CollectionUtil.isNotEmpty(pdList)){
			AssetPd pd = pdList.get(0);
			assetPd.updatePdRecord(PdStatusEnum.ASSET_PD_FINISH.getCode(),new Date(),pd.getPdCode(),"");
			 return "redirect:/assetPdList";//重定向
		}
		}catch(Exception e){
			return "修改失败！";
		}
		log.info("{}更新状态为已盘点，结束！",ap.get("pdCode"));
		 return "redirect:/assetPdList";//重定向
		
	}
	@RequestMapping(value = "/toAddCheckBat",method = RequestMethod.POST)
	@CrossOrigin(origins = "*")
	@ResponseBody
	public String toAddBat(@RequestBody String request) throws Exception{
		log.info("增加盘点批次开始！");
		
		Date nowDate = new Date();
		String batId = batFormatter.format(nowDate) ;
		List<AssetPdBat> batList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,AssetPdBat.class);
		AssetPdBat bat = batList.get(0);
		if(DateUtils.isSameDay(nowDate,bat.getPdStartDate())||nowDate.after(bat.getPdStartDate())){
			return "增加失败，盘点开始日期，不能小于当前日期";
		}
		Integer i= new java.util.Random().nextInt(900)+100;
		String orgList = bat.getOrgList();
		orgList = orgList.substring(1,orgList.length()-1);
		orgList = orgList.replaceAll("\"", "");
		bat.setPdBatId(batId+i.toString());
		bat.setPdBatCode(batId.substring(2));
		bat.setOrgList(orgList);
		bat.setISAll("1");//都选不是
		if(isCanAddBat(bat)){
			assetPdBatSevice.insertOne(bat);
			this.createChecks(bat);
			return "增加成功！";
		}
		log.info("增加盘点批次结束！");
		return "增加失败，盘点范围："+orgList+"已在盘点！";

	}
	

	private boolean isCanAddBat(AssetPdBat bat) {
		List<AssetPdBat> batList = null;
			batList = assetPdBatSevice.getAllBDate(bat.getPdStartDate());
			if(CollectionUtil.isNotEmpty(assetPdBatSevice.getAllBDate(bat.getPdEndDate())))
					batList.addAll(assetPdBatSevice.getAllBDate(bat.getPdEndDate()));
			
			if(CollectionUtil.isEmpty(batList)) return true;
		
		for(AssetPdBat pdBat:batList ){
			String orgList = pdBat.getOrgList();
			String[] orgs  = orgList.split(",");
			for(String org:orgs){
				if(bat.getOrgList().contains(org)){
					return false;
				}
			}
			
		}
		return true;
	}
	@Async
	public void createChecks(AssetPdBat bat) {
		List<Asset> assetList =null;
//		try {
//			 assetList = fuEAMUtil.getAllAsset();
//		} catch (Exception e) {
//			log.error("查询全部资产失败，原因："+e.getMessage());
//		}
		if(CollectionUtil.isNotEmpty(assetList)){
			assetPdSevice.createAllAssetPd(Long.parseLong(bat.getPdBatId()), assetList);

		}else{
			List<Asset> list = assetSevice.getAssetsByOrgList(bat.getOrgList());
			assetPdSevice.createAllAssetPd(Long.parseLong(bat.getPdBatId()), list);
		}
//		List<Asset> list = assetSevice.getAssetsByOrgList(bat.getOrgList());
//		assetPdSevice.createAllAssetPd(Long.parseLong(bat.getPdBatId()), list);
		
	}
	public void download(HttpServletResponse response,String filename) throws IOException{
		 BufferedInputStream bis = null;
		    BufferedOutputStream bos = null;
		    InputStream is = null;
		    File file = new File(filename);
		    try {
		        is = new FileInputStream(file);
		        response.reset();
		        response.setContentType("application/x-msdownload");
		        response.setHeader("Content-Length", String.valueOf(file.length()));
		        response.setHeader(
		                "Content-disposition",
		                "attachment; filename="
		                        + new String(filename.getBytes("GBK"),
		                        "ISO8859-1"));
		        bis = new BufferedInputStream(is);
		        bos = new BufferedOutputStream(response.getOutputStream());
		        byte[] buff = new byte[2048];
		        int bytesRead;
		        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
		            bos.write(buff, 0, bytesRead);
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        response.setContentType("text/html");
		        response.getWriter().print("download failed");
		    } finally {
		        try {
		            if (is != null)
		                is.close();
		            if (bis != null)
		                bis.close();
		            if (bos != null)
		                bos.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
	}
	
	 @RequestMapping(value="/getBiImg")
	    public void getBiImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
	         response.setHeader("Pragma", "no-cache");  
	            response.setHeader("Cache-Control", "no-cache");  
	            response.setDateHeader("Expires", 0);  
	            response.setContentType("image/jpg");  
	            String resp = request.getParameter("pdCode");
	            File dirFile = new File(filePath);
	            if(!dirFile.exists()){
	            	dirFile.mkdir();
	            }
	            File file=new File(filePath+resp+".jpg");//获取图片这个文件
	            InputStream is=new FileInputStream(file);
	            BufferedImage bi=ImageIO.read(is);
	            ImageIO.write(bi, "jpg", response.getOutputStream());
	    }
	 
	 @RequestMapping(value="/getRBBiImg")
	 @ResponseBody
	    public void getRBBiImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		 
		        log.info("jinru图片展示：");
	            response.setHeader("Pragma", "no-cache");  
	            response.setHeader("Cache-Control", "no-cache");  
	            response.setDateHeader("Expires", 0);  
	            response.setContentType("image/jpg");  
	            String resp = request.getParameter("pdCode");
	            File dirFile = new File(rbfilePath);
	            if(!dirFile.exists()){
	            	dirFile.mkdir();
	            }
	            File file=new File(rbfilePath+resp+".jpg");//获取图片这个文件
	            InputStream is=new FileInputStream(file);
	            BufferedImage bi=ImageIO.read(is);
	            ImageIO.write(bi, "jpg", response.getOutputStream());
	            log.info("jinru图片展示结束：");
	    }
	
	 
	 @RequestMapping(value = "/addExceptionBat",method = RequestMethod.POST)
		@CrossOrigin(origins = "*")
		@ResponseBody
		public String addExceptionBat(@RequestBody String request) throws Exception{
			log.info("增加盘点批次开始！");
			AssetPdBat bat = null;
			Date nowDate = new Date();
			List<AssetPdBat> batList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,AssetPdBat.class);
			if(CollectionUtil.isNotEmpty(batList)) {
				bat = batList.get(0);
			}else {
				return "操作失败，盘点批次读取异常";
			}
			if(!nowDate.after(bat.getPdEndDate())){
				return "增加失败，原盘点还未结束";
			}
			String batId = bat.getPdBatId();
			List<AssetPd> resultlist = new ArrayList<AssetPd>();
			resultlist = assetPdSevice.queryAllUnDoByBatId(Long.valueOf(batId));
			if(CollectionUtil.isEmpty(resultlist)) {
				return "增加异常盘点失败，没有异常！已全部完成盘点";
			}else {

				String batIdstr = batFormatter.format(nowDate) ;
				Integer i= new java.util.Random().nextInt(900)+100;
//				String orgList = bat.getOrgList();
//				orgList = orgList.substring(1,orgList.length()-1);
//				orgList = orgList.replaceAll("\"", "");
				bat.setPdStartDate(nowDate);
				Calendar c = Calendar.getInstance();
				c.setTime(nowDate);
				c.add(Calendar.DATE, 14);
				bat.setPdEndDate(c.getTime());
				bat.setPdBatId(batIdstr+i.toString());
				bat.setPdBatCode(batIdstr.substring(2));
				bat.setISAll("1");//都选不是
				bat.setHeadId(batId);
				assetPdBatSevice.insertOne(bat);
				for(AssetPd apd:resultlist){
					apd.setPdBatId(Long.valueOf(bat.getPdBatId()));
					assetPdSevice.updateAssetPd(apd);
					
					wx.getAccessToken();
					 wxService.send(apd.getJobNum(), "", "<"+myFormatter.format(bat.getPdStartDate())+"~"+myFormatter.format(bat.getPdEndDate())+"限期计算机资产盘点通知>\n您需要盘点资产如下:\n"
					 +"资产编号:"+apd.getAssetNumber()+"\n"
					 +"序列号:"+apd.getSerialNumber()+"\n"
					 +"型号:"+apd.getAssetModel()+"\n"
					 +"配置:"+apd.getAllocation()+"\n"
					 		+ "(内部测试  )");
					 //由于此次盘点时间结束！您还没完成资产盘点。 请点击微信下方“扫一扫”，扫描您的办公资产上粘贴的二维码
					
				}
				//this.createChecks(bat);//添加盘点
				return "增加成功！";
			}
			
			
			
			
			
			//List<AssetPdBat> batList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,AssetPdBat.class);
			
			
			

		}
	 
}

