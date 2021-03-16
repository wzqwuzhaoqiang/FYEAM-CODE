package com.fuyaogroup.eam.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.common.model.Page;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetErrorCord;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.model.AssetTransfer;
import com.fuyaogroup.eam.modules.fusion.service.AssetErrorCordService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.AssetTransferService;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
@Slf4j
@Controller
@CrossOrigin(origins = "*")
public class AssetTransferManageController {
	
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	static SimpleDateFormat batFormatter = new SimpleDateFormat("yyMMddHHmm", Locale.CHINA);

	static SimpleDateFormat infoFormatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	@Autowired
	AssetPdBatService assetPdBat;
	
	
	String startDate;
	String endDate;
	String assetNum;
	String centerName;
	List<AssetTransfer> list=null;
	List<AssetPd> listPd=null;
	static Integer PAGE_SIZE=10;//每页的条数-10条
	Page page = new Page();
	String currentPage;
	
	 @Value("${file.path}")
		private  String filePath;
	
	@Autowired
	AssetTransferService assetTransferSevice;
	
	@Autowired
	AssetErrorCordService aecService;
	
	@Autowired
	AssetService assetService;
	
	@Autowired
	AssetPdService assetPdService;
	
	@Autowired
	private FusionEAMAPIUtil fuEAMUtil=new FusionEAMAPIUtil();
	
	@RequestMapping(value = "/assetTransferList",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AssetTransfer> list(HttpServletRequest request, HttpServletResponse response) throws IOException{
		log.info("{}:查询资产转移表,开始...",LocalDateTime.now());
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");//增加后，页面中文不乱码
		
		//显示列表
		try {
			list=assetTransferSevice.getAll();//不是下载
			if(CollectionUtil.isEmpty(list)){
				list = new ArrayList<AssetTransfer>();
			}
			} catch (Exception e) {
				log.error(this.getClass().getName()+"：查询列表失败："+e.getMessage());
			}
		return list;
		
	}
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/tranferSubmit",method = RequestMethod.POST)
	public @ResponseBody String toAddAssetTrans(@RequestBody String request) throws Exception{
		log.info("增加资产转移表开始！");
		Asset asset = null; 
		AssetTransfer at = new AssetTransfer();
		List<Asset> assets = assetService.getByAssetNumber(at.getAssetNumber());
		List<AssetTransfer> atList = (new FusionEAMAPIUtil()).getFusionListFromOjbect(request,AssetTransfer.class);
		at = atList.get(0);
		if(CollectionUtil.isEmpty(atList)){
			return "异常错误";
		}
		try{
		
		if(CollectionUtil.isEmpty(assets)){
			
			
			return "资产："+at.getAssetNumber()+",不存在！";
//			 asset = fuEAMUtil.getOneAssetByAssetNum(at.getAssetNumber());
//			if(asset==null){
//				return "资产："+at.getAssetNumber()+",不存在！";
//			}
		}
		asset = assets.get(0);
		}catch(Exception e){
			return "资产："+at.getAssetNumber()+"查询,不存在！";
		}
		if(at.getOABillNum()==null||at.getOABillNum()==""){
			asset.setOABillINum(at.getSerialNumber()+batFormatter.format(new Date()));
		}else{
			asset.setOABillINum(at.getOABillNum());
		}
		if(at.getDepartment()!=null&&!at.getDepartment().equals("")) {
			asset.setWorkCenterName(at.getDepartment());
		}
		asset.setJobnum(at.getJobNum());
		asset.setUsername(at.getUserName());
		asset.setHandoverPerson(at.getHandoverPerson());
		asset.setHandoverTime(at.getHandoverTime());
		//asset.setOrganizationName(at.getHandoverCpt());
//		asset.setOABillINum("123");
		if(at.getHandoverCpt()!=null) {
			asset.setHandoverCpt(at.getHandoverCpt());
			Date nowDate  = myFormatter.parse(myFormatter.format(new Date()));
			List<AssetPdBat> nowTimeList = assetPdBat.getAllBDate(nowDate);
			if(!CollectionUtils.isEmpty(nowTimeList)) {
				for(AssetPdBat apd :nowTimeList) {
					AssetPd pd = assetPdService.getBySerialNumAndBatid(asset.getSerialNumber(),apd.getPdBatId());
					if(pd!=null) {
						if(pd.getOrganizationName().equals(asset.getOrganizationName())) {
							//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
							pd = this.createPd(asset,pd);
							assetPdService.updateAssetPd(pd);
						}else {
							assetPdService.deleteAssetPd(pd.getPdCode().toString());
							//盘点里的组织不等于更改后的组织，所以要删除此条盘点信息
						}
					}
				}	
				for(AssetPdBat apd :nowTimeList) {
					if(apd.getOrgList().contains(asset.getOrganizationName())) {
						//盘点里的组织等于更改后的组织，只要更新盘点的资产信息就好
						assetPdService.createAllAssetPd(Long.parseLong(apd.getPdBatId()), assets);
					}
			}
		}}
        try{
//        fuEAMUtil.updateOneAsset(asset);
//		fuEAMUtil.updateAssetDescriptiveFields(asset, true);
		at.setOABillNum(asset.getOABillINum());
		assetTransferSevice.createOne(at);
		assetService.updateOne(asset);
		List<AssetPd> pdList = assetPdService.getBySerialNum(asset.getSerialNumber(), PdStatusEnum.ASSET_PD_WAITING.getCode());
		if(CollectionUtil.isEmpty(pdList)){
			return "增加成功！";
		}
		assetPdService.updateAssetPd(pdList.get(0));
        }catch(Exception e){
            return "资产："+at.getAssetNumber()+"更新出错,"+e.getMessage();
        }
		log.info("增加资产转移表结束！");
		return "增加成功！";

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
	@RequestMapping(value = "/uploadFile",method = RequestMethod.POST, produces = "application/json; charset=utf-8")//, produces = "application/json; charset=utf-8")
	public @ResponseBody String importAssets(HttpServletRequest request) throws IOException{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	String msg = "上传成功";
	// 获得文件
	MultipartFile multipartFile = multipartRequest.getFile("file");// 与前端设置的fileDataName
	
		try {
			this.downloadFile(multipartFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "上传失败，原因"+e.getMessage();
		}
		return msg;
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
    
    private void downloadFile(MultipartFile file) throws Exception {
		String message = "";
	
		FileOutputStream out=null;
        if (null == file || file.isEmpty()) {
            log.info("文件不能为空！"); 

        }else{
        	message= "文件上传成功！";
            //这里以序列号作为文件夹
            
          //创建文件夹
            File file1 = new File(filePath);
            if(!file1.exists()){
            	boolean flag = file1.mkdirs();
            	if(flag)  System.out.println("创建成功");
            	else System.out.println("创建失败");
            } 
            String url = file1.getPath();
            try {
            //获得二进制流并输出
            	File file2 = new File(url,file.getOriginalFilename());
                byte[] f = file.getBytes();
                out = new FileOutputStream(file2);
                out.write(f);
//                return fileUrl+pdCode;
            } catch (IOException e) {
                System.out.println("上传失败");
                throw new Exception("文件保存失败,原因："+e.toString());
            }finally {
                // 完毕，关闭所有链接
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("关闭流失败");
                }
            }
        }
	}
    
    
    
    
    @RequestMapping(value = "/assetTransError",method = RequestMethod.POST)//, produces = "application/json; charset=utf-8")
	public @ResponseBody List<AssetErrorCord> assetTransError(HttpServletRequest request, HttpServletResponse response) throws IOException{

    	System.out.println(aecService.queryList());
    	
		return aecService.queryList();
		
	}
    
    
    
    
}

