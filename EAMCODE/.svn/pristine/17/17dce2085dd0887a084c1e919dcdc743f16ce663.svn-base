package com.fuyaogroup.eam.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tk.mybatis.mapper.util.StringUtil;
import cn.hutool.core.collection.CollectionUtil;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fuyaogroup.eam.common.model.Page;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;
import com.fuyaogroup.eam.modules.fusion.service.WorkOrderService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
import com.fuyaogroup.eam.util.ImageUtil;
import com.google.gson.Gson;
@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class WorkOrderManageController {
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	static SimpleDateFormat infoFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
	static String EXCEL_PATH="./excel/";
	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	@Autowired
	WorkOrderService woService;
	
	
	String startDate;
	String endDate;
	String assetNum;
	String assetName;
	String workOrderStatus;
	String workOrderType;
	String centerName;
	List<WorkOrder> list=null;
	static Integer PAGE_SIZE=10;//每页的条数-10条
	Page page = new Page();
	String currentPage;
	
	@RequestMapping(value = "/workorderlist",method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException{
		log.info("{}:查询维修列表,开始...",LocalDateTime.now());
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");//增加后，页面中文不乱码
		
		Date nowDate = new Date();
		startDate = request.getParameter("startDate");
		endDate = request.getParameter("endDate");
		assetNum=request.getParameter("assetNum");
		assetName = request.getParameter("assetName");
		centerName = request.getParameter("centerName");
		workOrderType = request.getParameter("workOrderType");
		workOrderStatus = request.getParameter("workOrderStatus");
		
		if(StringUtil.isEmpty(startDate)){
			 startDate = myFormatter.format(DateUtils.addDays(nowDate,-7));//测试改成0，原值为-1
		}
		if(StringUtil.isEmpty(endDate)){
			 endDate=myFormatter.format(DateUtils.addDays(nowDate,1));
		}
		//显示列表
		try {
			list=frutil.getWkOdrs(workOrderType, workOrderStatus,startDate, endDate, assetNum,false );//不是下载
			} catch (Exception e) {
				log.error(this.getClass().getName()+"：查询列表失败："+e.getMessage());
			}
		if(StringUtils.isNotEmpty(assetName)||StringUtils.isNotEmpty(centerName)){
			for(WorkOrder wo:list){
				if(wo.getAssetName().contains(assetName)||wo.getWorkcenter().equals(centerName)){
					list.remove(wo);
				}
			}
		}
		 //刚开始的页面为第一页
	    if (page.getCurrentPage() == null){
	        page.setCurrentPage(1);
	    } else {
	        page.setCurrentPage(page.getCurrentPage());
	    }
	    //设置每页数据为十条
	    page.setPageSize(PAGE_SIZE);
	    //每页的开始数
	    page.setStar((page.getCurrentPage() - 1) * page.getPageSize());
	    //list的大小
	    int count = list.size();
	    //设置总页数
	    page.setTotalPage(count % 10 == 0 ? count / 10 : count / 10 + 1);
	    //设置作用域
	    List<WorkOrder> dataList;
		try {
			//对list进行截取
			dataList = frutil.getAssetAndDescription(list.subList(page.getStar(),count-page.getStar()>page.getPageSize()?page.getStar()+page.getPageSize():count));
			page.setDataList(dataList);
		} catch (Exception e) {
			log.error("获取说明性弹性域失败:"+e.getMessage());
			response.getWriter().println(Boolean.FALSE);
		}
	   
	    request.setAttribute("paging", page);  
	    Gson gson=new Gson();
	    PrintWriter out = response.getWriter();
	    response.setContentType("text/html;charset=UTF-8");
	    String str = gson.toJson(page);
	    out.print( str);
	    log.info("{}:查询维修列表,结束...",LocalDateTime.now());
//	    request.setAttribute("startDate", startDate);
//		request.setAttribute("endDate", endDate);
//	    response.getWriter().println(0);
//	    return true;

	}
	
	@RequestMapping(value = "/woList",method = RequestMethod.POST)
	@ResponseBody
	public List<WorkOrder> wolist(HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<WorkOrder> list = woService.getAllByType(1);//1-计算机
		return list;
	}
	
	@RequestMapping(value = "/turnPage",method = RequestMethod.POST)
	@ResponseBody
	public void turnPage(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");//增加后，页面中文不乱码
		
		String str=request.getParameter("paging");
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
	    //设置作用域
	    List<WorkOrder> dataList;
		try {
			//对list进行截取
			dataList = frutil.getAssetAndDescription(list.subList(page.getStar(),page.getTotalPage()-page.getStar()>page.getPageSize()?page.getStar()+page.getPageSize():page.getTotalPage()));
			page.setDataList(dataList);
		} catch (Exception e) {
			log.info("获取说明性弹性域失败:"+e.getMessage());
		}
	   
			Gson gson=new Gson();
		    PrintWriter out = response.getWriter();
		    response.setContentType("text/html;charset=UTF-8");
		    String resStr = gson.toJson(page);
		    out.print( resStr);
//		 return "WEB-INF/view/workorderreport.jsp";
		
	}
	@RequestMapping(value = "/toPage",method = RequestMethod.GET)
	public String toPage(HttpServletRequest request,HttpServletResponse response){
		//初始化开始日期和结束日期，查询7天以内数据
		Date nowDate = new Date();
		 startDate = myFormatter.format(DateUtils.addDays(nowDate,-5));//测试改成0，原值为-1
		 endDate=myFormatter.format(DateUtils.addDays(nowDate,1));
		 request.setAttribute("startDate", startDate);
		 request.setAttribute("endDate", endDate);
		 return "WEB-INF/view/workorderreport.jsp";
	}
	
	@RequestMapping(value = "/download",method = RequestMethod.POST)
	 public void  downlaodFile(HttpServletRequest request,HttpServletResponse response){
		log.info("{}:下载维修列表,开始...",LocalDateTime.now());
		try {
			if(CollectionUtil.isEmpty(list)){
				list=frutil.getWkOdrs("", "ORA_CLOSED",startDate, endDate, "" ,true);
			}else{
				list=frutil.getAssetAndDescription(list);
			}
			//表头
			String[] datas = {"维护编码","维护内容","设备编码","设备名称","设备中心","检修时间","维修人员","故障原因","错误编码","触发时间","关闭时间","处理时间","停机时间","故障类型","故障废品","解决方案","超时分析","维修部位"};
		    //数据键名或者MODEL类字段名
		    String[] Col = {"WorkOrderId","WorkOrderDescription","AssetNumber","assetName","workcenter","PlannedStartDate","repairMan","reason","faultcode","TTIME","CTIME","RTIME","MANAGEVALUE","faulttype","faultScrapt","solution","timeoutAnalysis","equipmentPart"};
		    
	        String[][] data = ImageUtil.getTableData(list,datas,Col);
	        if(this.getExcelData(data)){
	        	File pathDir = new File(EXCEL_PATH);
	        	if(!pathDir.exists()){
	        		pathDir.mkdirs();
	        	}
	        	this.download(response, EXCEL_PATH + "testexcel.xls");
	        	}
			} catch (Exception e) {
			log.error(this.getClass().getName()+"：下载报表失败："+e.getMessage());
}
		log.info("{}:下载维修列表,结束...",LocalDateTime.now());
		 
   }
	private Boolean getExcelData(String[][] data) throws Exception {
		 //创建工作薄
	    OutputStream out = new FileOutputStream(EXCEL_PATH + "testexcel.xls");
        WritableWorkbook workbook = Workbook.createWorkbook(out);
		try{
        //创建新的一页
        WritableSheet sheet = workbook.createSheet("First Sheet",0);
		for(int i=0;i<data.length;i++){
    	   for(int j=0;j<data[i].length;j++){
    		   Label line = new Label(j,i,data[i][j]);
				sheet.addCell(line);
			}}
    		   workbook.write();
    		   return true;
    		   }catch(Exception e){
    			 log.error("生成Excel文件出错："+e.getMessage());
   				throw e;
    		   }finally{
    			   if(workbook!=null){
   					workbook.close();
   				}
				if(out!=null){
					out.close();
				}
				
				}
		
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
