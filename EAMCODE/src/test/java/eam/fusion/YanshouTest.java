package eam.fusion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fuyaogroup.eam.EamApplication;
import com.fuyaogroup.eam.controller.AddAssetController;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.QtfwThing;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.WindowServerService;
import com.fuyaogroup.eam.util.MyEmail;
import com.fuyaogroup.eam.util.WDWUtil;
import com.ibm.icu.text.SimpleDateFormat;
import com.soa.eis.adapter.framework.message.IMsgObject;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = EamApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class YanshouTest {
	
	@Autowired
	private WindowServerService wss;
	@Autowired
	private AssetService as;
	
	static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
	@Test
	public void ttest() throws Exception {
//		String str = "insert into asset_borrow_trackrecord (OAID,company,formDate,borrower,department,contactInfor,borrowThing,number,purpose,assertNumber,assertName,model,serialNumber,configInfo,startDate,borrowOutDate,borrowUseDate,borrowOutman,returnIs,returntwoIs,renewDateNumber,thingSituation,reciver,returnDate)values(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1)";
//		System.out.println(str.toUpperCase());
//		Calendar ct = Calendar.getInstance();
//		
//		ct.add(Calendar.DATE, -20);
//		Date dt = ct.getTime();
//		
//		System.out.println("///////////////////"+dt);
		String userName = "noreply@fuyaogroup.com"; // 发件人邮箱  
        String password = "Fuyao2018"; // 发件人密码  
        String smtpHost = "mail.fuyaogroup.com"; // 邮件服务器  SMTP.foxmail.com
  
        String to = "plm_system@fuyaogroup.com,zhaoqiang.wu@fuyaogroup.com,xiulong.ye@fuyaogroup.com"; // 收件人，多个收件人以半角逗号分隔  
        String cc = ""; // 抄送，多个抄送以半角逗号分隔  
        String subject = "通知"; // 主题  
        String body = "{测试批次盘点}您的计算机等资产还未在指定时间内盘点，是否丢失？请尽快在微信上盘点，否则财务可能做盘亏处理\n"
        		+ "有任何疑问，请联系叶修龙（18811579184）；-福耀集团公众号-EAM系统通知-盘点扫码\n"
        		+ "福耀集团信息部:"+simpleDateFormat.format(new Date()); // 正文，可以用html格式的哟  
        List<String> attachments = new ArrayList<String>();
        MyEmail email = MyEmail.entity(smtpHost, userName, password, to, cc, subject, body, attachments);  
  
        email.send(); // 发送！
		
      //Arrays.asList("C:\\Users\\gnb781\\Desktop\\01.jpg", "C:\\Users\\gnb781\\Desktop\\集团未盘点名单.xls"); // 附件的路径，多个附件也不怕  
        
        
	}
	@Test
	public void tet() {
		WindowServer ws = new WindowServer();
		ws.setTableID((UUID.randomUUID().toString()).substring(0,8));
		ws.setBorrowerId("userid");
		ws.setBorrowerName(("userName"));
		ws.setTools(("thingName"));
		ws.setBorrowTime(("currentTime"));
		ws.setCount(1);
		ws.setStatus("在借");
		//System.out.println(ws.toString());
		//wss.saveWindowServer(ws);
		List<WindowServer> wsl = new ArrayList<WindowServer>();
		wsl = wss.queryInBorrowThing("101798");
		for(WindowServer qser:wsl) {
			System.out.println("借用数据："+qser.toString());
		}
		//wss.saveOrUpdate(ws);
		//wss.save(ws);
	}
	
	
//	@Test
//	public void publ() {
//		int totalRows=0;
//		int totalCells=0;
//		// TODO Auto-generated method stub
//		System.out.println("文件导入程序开始运作---------------------------------");
//		
//	    String  filePath="C:\\Users\\gnb781\\Desktop\\2020年上半年报废清单20201026.xlsx";
//	   // C:\Users\gnb781\Desktop\2020年上半年报废清单20201026.xlsx
//
//	      //把spring文件上传的MultipartFile转换成CommonsMultipartFile类型
//	    File file = new  File(filePath);
//	       //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
//	    if (!file.exists()) file.mkdirs();
//	       
//	       //初始化输入流
//	       InputStream is = null;  
//	       try{
//	          //根据文件名判断文件是2003版本还是2007版本
//	          boolean isExcel2003 = true; 
//	          if(WDWUtil.isExcel2007(filePath)){
//	              isExcel2003 = false;  
//	          }
//	          //根据新建的文件实例化输入流
//	          is = new FileInputStream(file);
//	          //根据excel里面的内容读取客户信息
//	          Workbook wb = null;
//	           //当excel是2003时
//	           if(isExcel2003){
//	               wb = new HSSFWorkbook(is); 
//	           }
//	           else{//当excel是2007时
//	               wb = new XSSFWorkbook(is); 
//	           }
//
//	           //得到第一个shell  
//	            Sheet sheet=wb.getSheetAt(0);
//	            
//	           //得到Excel的行数
//	            totalRows=sheet.getPhysicalNumberOfRows();
//	            
//	           //得到Excel的列数(前提是有行数)
//	            if(totalRows>=1 && sheet.getRow(0) != null){
//	                 totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
//	            }
//	            System.out.println("-----------"+totalRows+"------------------"+totalCells+"---------------");
//	           //循环Excel行数,从第二行开始。标题不入库
////	            Map<String, Integer> tmap = new HashMap<String, Integer>();
////	            tmap.put("笔记本", 1);
////	            tmap.put("移动工作站", 2);
////	            tmap.put("台式工作站", 3);
////	            tmap.put("台式机", 4);
//	            for(int r=1;r<totalRows;r++){
//	                Row row = sheet.getRow(r);
//	                if (row == null) continue;
//	                String num="";
//	                String type="";
//	                //循环Excel的列   
//	                    Cell cell = row.getCell(7);
//	                    if (null != cell){
//	                     	   cell.setCellType(CellType.STRING);
//	                     	  type = cell.getStringCellValue();
//	                     	 List<Asset> asList = as.getBySerialNumber(type);
//	                     	 
//	                     	if(!CollectionUtils.isEmpty(asList)) {
////	                    		int htc = tmap.get(type.trim())==null?0:tmap.get(type.trim());
//	                    		Asset a = asList.get(0);
//	                    		a.setStatus(2);
//	                    		as.updateOne(a);
//	                    	}
//	                     	 
//	                     	   System.out.print(cell.getStringCellValue()+"    ---    ");
//	                    }
////	                    if (null != cell){
////	                     	   cell.setCellType(CellType.STRING);
////	                     	  type = cell.getStringCellValue();
////	                     	   System.out.print(cell.getStringCellValue()+"    ---    ");
////	                    }
////	                    cell = row.getCell(9);
////	                    if (null != cell){
////	                     	   cell.setCellType(CellType.STRING);
////	                     	  num = cell.getStringCellValue();
////	                     	   System.out.println(cell.getStringCellValue());
////	                    }
////	                    if(num!=null && !"".equals(num)) {
////	                    	List<Asset> asList = as.getBySerialNumber(num);
////	                    	if(!CollectionUtils.isEmpty(asList)) {
////	                    		int htc = tmap.get(type.trim())==null?0:tmap.get(type.trim());
////	                    		Asset a = asList.get(0);
////	                    		a.setHtcIncredible(htc);
////	                    		as.updateOne(a);
////	                    	}
////	                    }
//	                    
//	            }
//	          is.close();
//	      }catch(Exception e){
//	          e.printStackTrace();
//	      } finally{
//	          if(is !=null)
//	          {
//	              try{
//	                  is.close();
//	              }catch(IOException e){
//	                  is = null;    
//	                  e.printStackTrace();  
//	              }
//	          }
//	      }
//	}
}
