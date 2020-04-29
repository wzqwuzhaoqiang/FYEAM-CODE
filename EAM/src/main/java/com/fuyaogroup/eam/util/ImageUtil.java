package com.fuyaogroup.eam.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.fuyaogroup.eam.modules.fusion.model.Operation;
import com.fuyaogroup.eam.modules.fusion.model.WorkOrder;



public class ImageUtil {
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	static SimpleDateFormat myTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

	static SimpleDateFormat infoFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

	/**
     * 生成图片
     * @param cellsValue 以二维数组形式存放 表格里面的值
     * @param path 文件保存路径
	 * @param dragList 
	 * @param DRAG_LINE_NUM2 
     */
    public void myGraphicsGeneration(String cellsValue[][], String path,String tableName, int[] dragList, Integer DRAG_LINE_NUM) {
    	 int totalLine=0;
         for(int i:dragList){
         	totalLine+=i;
         }
         String[][] cellsValues=  new String[totalLine][];
         cellsValues=this.getImageTableDatas(cellsValue,totalLine,dragList,DRAG_LINE_NUM);
    	// 字体大小
        int fontTitileSize = 13;
        // 横线的行数
        int totalrow = cellsValue.length+1;
        // 竖线的行数
        int totalcol = 0;
        if (cellsValue[0]  != null) {
            totalcol = cellsValues[0].length;
        }
       
        // 图片宽度
        int imageWidth = 1024;
        // 行高
        int rowheight = 40;
        // 图片高度
        int imageHeight = (totalLine+1)*rowheight+50;
        // 起始高度
        int startHeight = 10;
        // 起始宽度
        int startWidth = 10;
        // 单元格宽度
        int colwidth = (int)((imageWidth-20)/(totalcol));
        BufferedImage image = new BufferedImage(imageWidth, imageHeight,BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0, imageWidth, imageHeight);
        graphics.setColor(new Color(220,240,240));
        int dragline = 0;
        //画横线drawLine(A.x,A.y,B.x,B.y)
        for(int j=0;j<totalrow; j++){
        	 graphics.setColor(Color.black);
            graphics.drawLine(startWidth, startHeight+(dragline+1)*rowheight, startWidth+colwidth*totalcol, startHeight+(dragline+1)*rowheight);
            if(j<dragList.length) dragline+=dragList[j];
        }
        //画竖线
        for(int k=0;k<totalcol+1;k++){
            graphics.setColor(Color.black);
            graphics.drawLine(startWidth+k*colwidth, startHeight+rowheight, startWidth+k*colwidth, startHeight+rowheight*(totalLine+1));
        }
        //设置字体
        Font font = new Font("微软雅黑",Font.BOLD,fontTitileSize);
        graphics.setFont(font);
        //写标题
        String title = "【"+tableName+"】";
        graphics.drawString(title, startWidth, startHeight+rowheight-10);
        //写入内容
        for(int n=0;n<cellsValues.length;n++){
                for(int l=0;l<cellsValues[n].length;l++){
                    if (n == 0) {
                        font = new Font("微软雅黑",Font.BOLD,fontTitileSize);
                        graphics.setFont(font);
                    }else if (n > 0 && l >0) {
                        font = new Font("微软雅黑",Font.PLAIN,fontTitileSize);
                        graphics.setFont(font);
                        graphics.setColor(Color.RED);
                    } else {
                        font = new Font("微软雅黑",Font.PLAIN,fontTitileSize);
                        graphics.setFont(font);
                        graphics.setColor(Color.BLACK);
                    }
                   //drawString(String,a.x,a.y)
                graphics.drawString(null==cellsValues[n][l]?"":cellsValues[n][l].toString(), startWidth+colwidth*l+5, startHeight+rowheight*(n+2)-10);
            }
        }
        // 保存图片
        createImage(image, path);
    }

private String[][] getImageTableDatas(String[][] cellsValue,int totalLine, int[] dragList,Integer DRAG_LINE_NUM) {
	String[][] cellsValues=  new String[totalLine][cellsValue[0].length];
	int addline = 0;
	for(int i=0;i<cellsValue.length;i++){
		int drag = dragList[i];
		if(drag>1){
			for(int k=0;k<drag;k++){
				for(int j=0;j<cellsValue[i].length;j++){
					StringBuffer s = new StringBuffer((cellsValue[i][j]).trim());
					int n=cellsValue[i][j].length()/DRAG_LINE_NUM;//字符窗cellsValue[i][j],1行要转换成几行
					if(n>0){
					for(int index = 0; index <= n;index++){
						if(index<n){
							cellsValues[addline+index][j]=s.substring(0+index*DRAG_LINE_NUM, DRAG_LINE_NUM*(index+1)-1);
						}else{
							cellsValues[addline+index][j]=s.substring(0+index*DRAG_LINE_NUM-1);

						}
						
					}
				}else{
					cellsValues[addline][j]=cellsValue[i][j];
				}
				
			}
			
		}
		}else{
			cellsValues[addline+drag-1]=cellsValue[i];
		}
		addline+=drag;//具体行数（第i行，属于新的数组第addline~addline+drag行）
	}
	return cellsValues;
	}

/**
     * 将图片保存到指定位置
     * @param image 缓冲文件类
     * @param fileLocation 文件位置
     */
    public void createImage(BufferedImage image, String fileLocation) {
        try {
        	//文件夹不存在，则创建
        	File f = new File(fileLocation);
        	File directory = new File(f.getParent());//设定为当前文件夹
        	if  (!directory.exists()  && !directory.isDirectory())      
        	{       
        	    System.out.println("//不存在");  
        	    directory.mkdir();    
        	} 
        	  ImageIO.write(image, "jpg", new File(fileLocation)); 
            } catch (Exception e) {
            e.printStackTrace();
            }
    }
    
    public static String[][] getTableData(List<WorkOrder> list, String[] datas, String[] col) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		String[][] dataInfo = new String[list.size()+1][datas.length] ;
		dataInfo[0]=datas;
		for(int i = 0 ;i<list.size();i++){
		for(int j=0;j<col.length;j++){
			String str = col[j];
			if(str.contentEquals("optList")){
				String strs = "" ;
				int k=1;
				for(Operation oprt:list.get(i).getOptList()){
					strs+=k+"."+oprt.getOperationName()+";";
					k++;
				}
				if(strs!=null){
				dataInfo[i+1][j]=strs;
				}
				
			}else{
				Field[] flds = WorkOrder.class.getDeclaredFields();
				for(Field fld:flds){
					fld.setAccessible(true);
					if(col[j].contentEquals(fld.getName())){
						Object obj= fld.get(list.get(i));
						 if(str.contains("Date")){
							 try{
//								Date t = new Date();
								 Date t=infoFormatter.parse(obj.toString());
								String dateTime = myFormatter.format(t);
								dataInfo[i+1][j]=dateTime==null?"":dateTime;
							 }catch(Exception e){
								 System.out.println(e.getMessage());
							 }
						}else{
							dataInfo[i+1][j]=(obj==null?"":obj.toString());
						}
						
						break;
					}
					
				}
				
			}
		}
		}
		return dataInfo;
	}
    
    
    /**
	 * 
	 * 压缩图片，并等比缩小。
	 * 
	 * @author aren
	 * @param data
	 *            输入图片数据的byte[]。
	 * @param width
	 *            最大输出宽度，但是最后会根据图片本身比例调整。推荐值800。
	 * @param height
	 *            最大输出高度，但是最后会根据图片本身比例调整。推荐值600。
	 * @param type
	 *            指定最后存储的图片类型，支持字符串jpg,png,gif,bmp,jpeg。如果为null，则默认输出jpg格式图片。
	 * @param maxSize
	 *            指定最大输出图片的容量大小。可以为null表示不指定压缩容量大小。不要小于10000，推荐100000。
	 * @return 输出图片数据的byte[]。
	 * @throws Exception
	 */
	public static byte[] zipImageToScaledSize(byte[] data, int width, int height, String type, Integer maxSize)
			throws Exception {
		if (data == null) {
			return null;
		}
		if (width <= 0 || height <= 0) {
			width = 800;
			height = 600;
		}
		// 设定输出格式
		String[] supportType = new String[] { "jpg", "png", "bmp", "jpeg", "gif" };
		if (type == null || !ArrayUtils.contains(supportType, type)) {
			type = "jpg";
		}
		int pointedHeight;
		int pointedWidth;
		ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
		BufferedImage bufferedImage = ImageIO.read(inputStream);
		inputStream.close();
		int originalHeight = bufferedImage.getHeight();
		int originalWidth = bufferedImage.getWidth();
		// 设定等比例压缩。
		if ((originalHeight / (double) height) > (originalWidth / (double) width)) {
			pointedHeight = NumberUtils.min(height, originalHeight);
			pointedWidth = -1;
		} else {
			pointedHeight = -1;
			pointedWidth = NumberUtils.min(width, originalWidth);
		}
		// 压缩图片，此处附上颜色类型BufferedImage.TYPE_INT_RGB。Color.WHITE，可以有效避免png转jpg时图片发红的问题。
		Image newImage = bufferedImage.getScaledInstance(pointedWidth, pointedHeight, Image.SCALE_SMOOTH);
		BufferedImage newBufferedImage = new BufferedImage(newImage.getWidth(null), newImage.getHeight(null),
				BufferedImage.TYPE_INT_RGB);		
		newBufferedImage.getGraphics().drawImage(newImage, 0, 0,Color.WHITE, null);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(newBufferedImage, type, byteArrayOutputStream);
		byteArrayOutputStream.close();
		data = byteArrayOutputStream.toByteArray();
		if (maxSize != null && data.length > maxSize) {
			// 设定递归的保险，以免图片质量太差
			if (maxSize < 5000 && (data.length > 10 * maxSize)) {
				maxSize = 5000;
			}
			// 递归压缩
			double scale = Math.max(Math.pow(maxSize / (double) data.length, 0.5), 0.9);
			return zipImageToScaledSize(data, (int) (width * scale), (int) (height * scale), type, maxSize);
		} else {
			return data;
		}
}

}
