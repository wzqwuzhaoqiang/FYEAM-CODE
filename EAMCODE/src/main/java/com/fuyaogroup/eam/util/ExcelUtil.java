package com.fuyaogroup.eam.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ExcelUtil {
	 public static  HashMap<String,String> readExcel(String filePath,boolean flag) throws BiffException, IOException
	   {
		   HashMap<String,String> map = new HashMap<String,String>();
	      File xlsFile = new File(filePath);
	      // 获得工作簿对象
	      Workbook workbook = Workbook.getWorkbook(xlsFile);
	      // 获得所有工作表
	      Sheet[] sheets = workbook.getSheets();
	      // 遍历工作表
	      if (sheets != null)
	      {
	         for (Sheet sheet : sheets)
	         {
	            // 获得行数
	            int rows = sheet.getRows();
	            // 获得列数
	            int cols = sheet.getColumns();
	            // 读取数据
	            if(flag){
	            for (int row = 1; row < rows; row++)
	            {
	               map.put(sheet.getCell(0, row).getContents(), sheet.getCell(1, row)==null?"":sheet.getCell(1, row).getContents());
	            }
	            }else{
	            	 for (int row = 1; row < rows; row++)
	                 {
	                    map.put(sheet.getCell(0, row).getContents(), "");
	                 }
	            	}
	            }
	         }
	      workbook.close();
		return map;
	   }
	 
	 public static  List<HashMap<String,String>> readALLExcel(String filePath,int flag) throws BiffException, IOException
	   {
		   
		   List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	      File xlsFile = new File(filePath);
	      // 获得工作簿对象
	      Workbook workbook = Workbook.getWorkbook(xlsFile);
	      // 获得所有工作表
	      Sheet[] sheets = workbook.getSheets();
	      // 遍历工作表
	      if (sheets != null)
	      {
	         for (Sheet sheet : sheets)
	         {
	            // 获得行数
	            int rows = sheet.getRows();
	            // 获得列数
	            int cols = sheet.getColumns();
	            // 读取数据
	            	for (int row = 1; row < rows; row++){
	            {
	            	HashMap<String,String> map = new HashMap<String,String>();
	            	 for(int col=0;col<cols;col++){
	            		map.put(sheet.getCell(col, 0).getContents(), sheet.getCell(col,row).getContents());
	            	}
	            	list.add(map);
	            }
	            }
	            }
	         }
	      workbook.close();
		return list;
	   }
	   /**
	    * 获取 一行数据
	    * @param filePath
	    * @return
	    * @throws BiffException
	    * @throws IOException
	    */
	   public static  List<String> readExcelList(String filePath,int col) throws BiffException, IOException
	   {
		   List<String> list = new ArrayList<String>();
	      File xlsFile = new File(filePath);
	      // 获得工作簿对象
	      Workbook workbook = Workbook.getWorkbook(xlsFile);
	      // 获得所有工作表
	      Sheet[] sheets = workbook.getSheets();
	      // 遍历工作表
	      if (sheets != null)
	      {
	         for (Sheet sheet : sheets)
	         {
	            // 获得行数
	            int rows = sheet.getRows();
	            // 获得列数
	            int cols = sheet.getColumns();
	            // 读取数据
	            	 for (int row = 1; row < rows; row++)
	                 {
	                   list.add(sheet.getCell(col, row).getContents());
	                 }
	            	}
	         }
	      workbook.close();
		return list;
	   }
	  
	   /**
	    * 
	    * @param path
	    * @param list-只能填写一行
	    * @param rows-（1,X）当row = 1时，填写一行Y个数据
	    * @param cols-（1,Y）当col = 1时，填写一列X个数据
	    * @throws IOException
	    * @throws RowsExceededException
	    * @throws WriteException
	    */
	   public static void writeExcel(String path,List list,int rows ,int cols) throws IOException, RowsExceededException, WriteException{
		   File xlsFile = new File(path);
		   WritableWorkbook workbook;
	      // 创建一个工作簿
			    workbook = Workbook.createWorkbook(xlsFile);
	      // 创建一个工作表
	      WritableSheet sheet = workbook.createSheet("sheet4", 0);
	      for (int row = 0; row < rows; row++)
	      {
	         for (int col = 0; col < cols; col++)
	         {
	            // 向工作表中添加数据
	            sheet.addCell(new Label(col, row,(String) list.get(row) ));
	         }
	      }
	      workbook.write();
	      workbook.close();
	   }
	   
		private Boolean getExcelData(String path,String[][] data) throws WriteException, IOException {
			 //创建工作薄
		    OutputStream out = new FileOutputStream(path + "testexcel.xls");
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
	   
}
