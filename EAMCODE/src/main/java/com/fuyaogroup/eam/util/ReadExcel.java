package com.fuyaogroup.eam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.QtfwThing;

@Service
public class ReadExcel {
	private static  SimpleDateFormat simpleDateFormat3=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static  SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-ddHHmmss");
	private static  SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyy-MM-dd");

    //总行数
    private int totalRows = 0;  
    //总条数
    private int totalCells = 0; 
    //错误信息接收器
    private String errorMsg;
    
    @Value("${file.path}")
	private  String filePath;
    
    //构造方法
    public ReadExcel(){}
    //获取总行数
    public int getTotalRows()  { return totalRows;} 
    //获取总列数
    public int getTotalCells() {  return totalCells;} 
    //获取错误信息
    public String getErrorInfo() { return errorMsg; }  
    
    String[] values = {"organizationName","financialCode","jobnum","username","description","assetNumber","htcIncredible","assetmodel","serialNumber","allocation","displayer","macaddress","serviceid","mouse","wifimac","keyboard","poweradapt","usingstarttime","workCenterName","manufacturer","warrantyperiod","amount","status","remark"};

    
  /**
   * 验证EXCEL文件
   * @param filePath
   * @return
   */
  public boolean validateExcel(String filePath){
        if (filePath == null || !(WDWUtil.isExcel2003(filePath) || WDWUtil.isExcel2007(filePath))){  
            errorMsg = "文件名不是excel格式";  
            return false;  
        }  
        return true;
  }
    
  /**
   * 读EXCEL文件，获取客户信息集合
   * @param fielName
   * @return
 * @throws Exception 
   */
  public List<Asset> getExcelInfo(String fileName,MultipartFile Mfile) throws Exception{
      if(null==filePath){
    	  filePath="D:/data/eamapi/fileupdate/";
      }
      //把spring文件上传的MultipartFile转换成CommonsMultipartFile类型
       File file = new  File(filePath);
       //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
       if (!file.exists()) file.mkdirs();
       //新建一个文件
       File file1 = new File(filePath ,simpleDateFormat.format(new Date()) + ".xlsx"); 
       //将上传的文件写入新建的文件中
       try {
    	   Mfile.transferTo(file1);
       } catch (Exception e) {
    	   System.out.print("上传文件失败");
    	   e.printStackTrace();
    	   throw e;
           
       }
       
       //初始化客户信息的集合    
       List<Asset> AssetList=new ArrayList<Asset>();
       //初始化输入流
       InputStream is = null;  
       try{
          //验证文件名是否合格
          if(!validateExcel(fileName)){
              return null;
          }
          //根据文件名判断文件是2003版本还是2007版本
          boolean isExcel2003 = true; 
          if(WDWUtil.isExcel2007(fileName)){
              isExcel2003 = false;  
          }
          //根据新建的文件实例化输入流
          is = new FileInputStream(file1);
          //根据excel里面的内容读取客户信息
          AssetList = getExcelInfo(is, isExcel2003); 
          is.close();
      }catch(Exception e){
          e.printStackTrace();
      } finally{
          if(is !=null)
          {
              try{
                  is.close();
              }catch(IOException e){
                  is = null;    
                  e.printStackTrace();  
              }
          }
      }
      return AssetList;
  }
  
  
  /**
   * 前台借用物品
   * @param fielName
   * @return
 * @throws Exception 
   */
  public List<QtfwThing> getExcelInfoqtzc(String fileName,MultipartFile Mfile) throws Exception{
      if(null==filePath){
    	  filePath="D:/data/eamapi/fileupdate/";
      }
      //把spring文件上传的MultipartFile转换成CommonsMultipartFile类型
       File file = new  File(filePath);
       //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
       if (!file.exists()) file.mkdirs();
       //新建一个文件
       File file1 = new File(filePath ,simpleDateFormat.format(new Date()) + ".xlsx"); 
       //将上传的文件写入新建的文件中
       try {
    	   Mfile.transferTo(file1);
       } catch (Exception e) {
    	   System.out.print("上传文件失败");
    	   e.printStackTrace();
    	   throw e;
           
       }
       
       //初始化客户信息的集合    
       List<QtfwThing> AssetList=new ArrayList<QtfwThing>();
       //初始化输入流
       InputStream is = null;  
       try{
          //验证文件名是否合格
          if(!validateExcel(fileName)){
              return null;
          }
          //根据文件名判断文件是2003版本还是2007版本
          boolean isExcel2003 = true; 
          if(WDWUtil.isExcel2007(fileName)){
              isExcel2003 = false;  
          }
          //根据新建的文件实例化输入流
          is = new FileInputStream(file1);
          //根据excel里面的内容读取客户信息
          AssetList = getExcelInfoqtzc(is, isExcel2003); 
          is.close();
      }catch(Exception e){
          e.printStackTrace();
      } finally{
          if(is !=null)
          {
              try{
                  is.close();
              }catch(IOException e){
                  is = null;    
                  e.printStackTrace();  
              }
          }
      }
      return AssetList;
  }
  
  
  
  
  
  
  
  
  
  /**
   * 导入软件信息
   * @param fileName
   * @param Mfile
   * @return
   * @throws Exception
   */
  public List<Asset> getExcelInfoOfSoft(String fileName,MultipartFile Mfile) throws Exception{
      if(null==filePath){
    	  filePath="D:/data/eamapi/fileupdate/";
      }
      //把spring文件上传的MultipartFile转换成CommonsMultipartFile类型
       File file = new  File(filePath);
       //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
       if (!file.exists()) file.mkdirs();
       //新建一个文件
       File file1 = new File(filePath ,simpleDateFormat.format(new Date()) + ".xlsx"); 
       //将上传的文件写入新建的文件中
       try {
    	   Mfile.transferTo(file1);
       } catch (Exception e) {
    	   System.out.print("上传文件失败");
    	   e.printStackTrace();
    	   throw e;
           
       }
       
       //初始化客户信息的集合    
       List<Asset> AssetList=new ArrayList<Asset>();
       //初始化输入流
       InputStream is = null;  
       try{
          //验证文件名是否合格
          if(!validateExcel(fileName)){
              return null;
          }
          //根据文件名判断文件是2003版本还是2007版本
          boolean isExcel2003 = true; 
          if(WDWUtil.isExcel2007(fileName)){
              isExcel2003 = false;  
          }
          //根据新建的文件实例化输入流
          is = new FileInputStream(file1);
          //根据excel里面的内容读取客户信息
          AssetList = getExcelInfoOfSoft(is, isExcel2003); 
          is.close();
      }catch(Exception e){
          e.printStackTrace();
      } finally{
          if(is !=null)
          {
              try{
                  is.close();
              }catch(IOException e){
                  is = null;    
                  e.printStackTrace();  
              }
          }
      }
      return AssetList;
  }
  
  /**
   * 根据excel里面的内容读取客户信息
   * @param is 输入流
   * @param isExcel2003 excel是2003还是2007版本
   * @return
 * @throws ParseException 
   * @throws IOException
   */
  public  List<Asset> getExcelInfoOfSoft(InputStream is,boolean isExcel2003) throws ParseException{
       List<Asset> AssetList=null;
       try{
           /** 根据版本选择创建Workbook的方式 */
           Workbook wb = null;
           //当excel是2003时
           if(isExcel2003){
               wb = new HSSFWorkbook(is); 
           }
           else{//当excel是2007时
               wb = new XSSFWorkbook(is); 
           }
           //读取Excel里面客户的信息
           AssetList=readExcelValueOfSoft(wb);
       }
       catch (IOException e)  {  
           e.printStackTrace();  
       }  
       return AssetList;
  }
  
  /**
   * 根据excel里面的内容读取客户信息
   * @param is 输入流
   * @param isExcel2003 excel是2003还是2007版本
   * @return
 * @throws ParseException 
   * @throws IOException
   */
  public  List<Asset> getExcelInfo(InputStream is,boolean isExcel2003) throws ParseException{
       List<Asset> AssetList=null;
       try{
           /** 根据版本选择创建Workbook的方式 */
           Workbook wb = null;
           //当excel是2003时
           if(isExcel2003){
               wb = new HSSFWorkbook(is); 
           }
           else{//当excel是2007时
               wb = new XSSFWorkbook(is); 
           }
           //读取Excel里面客户的信息
           AssetList=readExcelValue(wb);
       }
       catch (IOException e)  {  
           e.printStackTrace();  
       }  
       return AssetList;
  }
  
  /**
   * 根据excel里面的内容读取客户信息
   * @param is 输入流
   * @param isExcel2003 excel是2003还是2007版本
   * @return
 * @throws ParseException 
   * @throws IOException
   */
  public  List<QtfwThing> getExcelInfoqtzc(InputStream is,boolean isExcel2003) throws ParseException{
       List<QtfwThing> AssetList=null;
       try{
           /** 根据版本选择创建Workbook的方式 */
           Workbook wb = null;
           //当excel是2003时
           if(isExcel2003){
               wb = new HSSFWorkbook(is); 
           }
           else{//当excel是2007时
               wb = new XSSFWorkbook(is); 
           }
           //读取Excel里面客户的信息
           AssetList=readExcelValueqtzc(wb);
       }
       catch (IOException e)  {  
           e.printStackTrace();  
       }  
       return AssetList;
  }
  
  /**
   * 读取Excel里面客户的信息
   * @param wb
   * @return
 * @throws ParseException 
   */
  private List<Asset> readExcelValue(Workbook wb) throws ParseException{ 
      //得到第一个shell  
       Sheet sheet=wb.getSheetAt(0);
       
      //得到Excel的行数
       this.totalRows=sheet.getPhysicalNumberOfRows();
       
      //得到Excel的列数(前提是有行数)
       if(totalRows>=1 && sheet.getRow(0) != null){
            this.totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
       }
       
       List<Asset> AssetList=new ArrayList<Asset>();
      //循环Excel行数,从第二行开始。标题不入库
       for(int r=1;r<totalRows;r++){
           Row row = sheet.getRow(r);
           if (row == null) continue;
           Asset asset= new Asset();
           
           //循环Excel的列
           for(int c = 0; c <this.totalCells; c++){    
               Cell cell = row.getCell(c);
               if (null != cell){
                   if(c==0){//第一列不读
                	   cell.setCellType(CellType.STRING);
                	   asset.setOrganizationName(cell.getStringCellValue());
                   } else if(c==1){
                	   cell.setCellType(CellType.STRING);
                   asset.setFinancialCode(cell.getStringCellValue());}
                   else if(c==2){
                	   cell.setCellType(CellType.STRING);
                   asset.setJobnum(cell.getStringCellValue());}
                   else if(c==3){
                	   cell.setCellType(CellType.STRING);
                   asset.setUsername(cell.getStringCellValue());}
                   else if(c==4){
                	   cell.setCellType(CellType.STRING);
                   asset.setDescription(cell.getStringCellValue());}
                   else if(c==5){
                	   cell.setCellType(CellType.STRING);
                   asset.setAssetNumber(cell.getStringCellValue());}
                   else if(c==6){
                   asset.setHtcIncredible(new Integer((int) cell.getNumericCellValue()));}
                   else if(c==7){
                	   cell.setCellType(CellType.STRING);
                   asset.setAssetmodel(cell.getStringCellValue());}
                   else if(c==8){
                	   cell.setCellType(CellType.STRING);
                   asset.setSerialNumber(cell.getStringCellValue());}
                   else if(c==9){
                	   cell.setCellType(CellType.STRING);
                   asset.setAllocation(cell.getStringCellValue());}
                   else if(c==10){
                	   cell.setCellType(CellType.STRING);
                   asset.setDisplayer(cell.getStringCellValue());}
                   else if(c==11){
                	   cell.setCellType(CellType.STRING);
                   asset.setMacaddress(cell.getStringCellValue());}
                   else if(c==12){
                	   cell.setCellType(CellType.STRING);
                   asset.setServiceid(cell.getStringCellValue());}
                   else if(c==13){
                	   cell.setCellType(CellType.STRING);
                   asset.setMouse(cell.getStringCellValue());}
                   else if(c==14){
                	   cell.setCellType(CellType.STRING);
                   asset.setWifimac(cell.getStringCellValue());}
                   else if(c==15){
                	   cell.setCellType(CellType.STRING);
                   asset.setKeyboard(cell.getStringCellValue());}
                   else if(c==16){
                	   cell.setCellType(CellType.STRING);
                   asset.setPoweradapt(cell.getStringCellValue());}
                   else if(c==17){
                	   //cell.setCellType(CellType.STRING);
//                	   if(cell.getStringCellValue()!=""&&cell.getStringCellValue()!=null)
//                		   	asset.setUsingstarttime(simpleDateFormat2.parse(cell.getStringCellValue()));
                	   Date d  =  cell.getDateCellValue();
                	   asset.setUsingstarttime(d);
				 }
                   else if(c==18){
                	   cell.setCellType(CellType.STRING);
                   asset.setWorkCenterName(cell.getStringCellValue());}
                   else if(c==19){
                	   cell.setCellType(CellType.STRING);
                   asset.setManufacturer(cell.getStringCellValue());}
                   else if(c==20){
                   asset.setWarrantyperiod((new Double(cell.getNumericCellValue())).intValue());}
                   else if(c==21){
                   asset.setAmount(new BigDecimal(cell.getNumericCellValue()));}
                   else if(c==22){
                   asset.setStatus((new Double(cell.getNumericCellValue())).intValue());}
                   else if(c==23){
                	   cell.setCellType(CellType.STRING);
                   asset.setRemark(cell.getStringCellValue());}

               }
           }
           //添加客户
           AssetList.add(asset);
       }
       return AssetList;
  }
  

  
  /**
   * 读取Excel里面客户的信息
   * @param wb
   * @return
 * @throws ParseException 
   */
  private List<QtfwThing> readExcelValueqtzc(Workbook wb) throws ParseException{ 
      //得到第一个shell  
       Sheet sheet=wb.getSheetAt(0);
       
      //得到Excel的行数
       this.totalRows=sheet.getPhysicalNumberOfRows();
       
      //得到Excel的列数(前提是有行数)
       if(totalRows>=1 && sheet.getRow(0) != null){
            this.totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
       }
       
       List<QtfwThing> AssetList=new ArrayList<QtfwThing>();
      //循环Excel行数,从第二行开始。标题不入库
       for(int r=1;r<totalRows;r++){
           Row row = sheet.getRow(r);
           if (row == null) continue;
           QtfwThing asset= new QtfwThing();
           
           //循环Excel的列
           for(int c = 0; c <this.totalCells; c++){    
               Cell cell = row.getCell(c);
               if (null != cell){
                   if(c==0){//第一列不读
                	   cell.setCellType(CellType.STRING);
                	   asset.setName(cell.getStringCellValue());
                   } else if(c==1){
                	   cell.setCellType(CellType.STRING);
                   asset.setSerial(cell.getStringCellValue());}
                   else if(c==2){
                	   cell.setCellType(CellType.STRING);
                   asset.setDepartName(cell.getStringCellValue());}
                  

               }
           }
           //添加客户
           AssetList.add(asset);
       }
       return AssetList;
  }
  
  
  
  
  
  /**
   * 读取Excel里面客户的信息
   * @param wb
   * @return
 * @throws ParseException 
   */
  private List<Asset> readExcelValueOfSoft(Workbook wb) throws ParseException{ 
      //得到第一个shell  
       Sheet sheet=wb.getSheetAt(0);
       
      //得到Excel的行数
       this.totalRows=sheet.getPhysicalNumberOfRows();
       
      //得到Excel的列数(前提是有行数)
       if(totalRows>=1 && sheet.getRow(0) != null){
            this.totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
       }
       
       List<Asset> AssetList=new ArrayList<Asset>();
      //循环Excel行数,从第二行开始。标题不入库
       for(int r=1;r<totalRows;r++){
           Row row = sheet.getRow(r);
           if (row == null) continue;
           Asset asset= new Asset();
           Date d = new Date();
           //循环Excel的列
           for(int c = 0; c <this.totalCells; c++){    
               Cell cell = row.getCell(c);
               if (null != cell){
                   if(c==0){//第一列不读
                	   cell.setCellType(CellType.STRING);
                	   asset.setOrganizationName(cell.getStringCellValue());
                   } else if(c==1){
                	   cell.setCellType(CellType.STRING);
                	   asset.setJobnum(cell.getStringCellValue());
        }
                   else if(c==2){
                	   cell.setCellType(CellType.STRING);
                   asset.setDescription(cell.getStringCellValue());}
                   else if(c==3){
                	   cell.setCellType(CellType.STRING);
                   asset.setAssetNumber(cell.getStringCellValue());}
                   else if(c==4){
                	   cell.setCellType(CellType.STRING);
                   asset.setAssetmodel(cell.getStringCellValue());}
                   else if(c==5){
                	   cell.setCellType(CellType.STRING);
                   asset.setSoftType(Integer.valueOf(cell.getStringCellValue()));}
                   else if(c==6){
                	   cell.setCellType(CellType.STRING);
                   asset.setOABillINum(cell.getStringCellValue());}
                   else if(c==7){
                	   cell.setCellType(CellType.STRING);
                   asset.setManufacturer(cell.getStringCellValue());}
                   else if(c==8){
                	   cell.setCellType(CellType.STRING);
                   asset.setSoftwarestatus(Integer.valueOf(cell.getStringCellValue()));}
                   else if(c==9){
                	   cell.setCellType(CellType.STRING);
                   asset.setSource(Integer.valueOf(cell.getStringCellValue()));}
                   else if(c==10){
                	   cell.setCellType(CellType.STRING);
                	   String starttime = cell.getStringCellValue();
                	   if(starttime!=null&&!"".equals(starttime)) {
                		   Date date  = simpleDateFormat3.parse(starttime);
                           asset.setUsingstarttime(date);}
                	   }
                	   
                   else if(c==11){
                	   
                	   cell.setCellType(CellType.STRING);
                	   String starttime = cell.getStringCellValue();
                	   if(starttime!=null&&!"".equals(starttime)) {
                		   Date dt  = simpleDateFormat3.parse(starttime);
                		   asset.setWarrantdate(dt);
                		   int mm = dt.getMonth();
                    	   if(mm>3) {
                    		   dt.setMonth(mm-3);
                    	   }else {
                    		   dt.setYear(dt.getYear()-1);
                    		   dt.setMonth(dt.getMonth()+9);
                    	   }
                       asset.setWarrantyreminderdate(dt);
                           }
                	   }
                   else if(c==12){
                	  // Date time =  d;
                	   }
                   else if(c==13){
                	   cell.setCellType(CellType.STRING);
                   asset.setSuite(Integer.valueOf(cell.getStringCellValue()));}
                   else if(c==14){
                	   cell.setCellType(CellType.STRING);
                   asset.setRemark(cell.getStringCellValue());}
                   
           }
           
       }
         //添加客户
           AssetList.add(asset);
       }
       return AssetList;}
}
