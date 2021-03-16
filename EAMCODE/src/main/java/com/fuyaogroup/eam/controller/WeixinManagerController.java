package com.fuyaogroup.eam.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.hutool.core.collection.CollectionUtil;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fuyaogroup.eam.common.Exception.GlobalException;
import com.fuyaogroup.eam.common.enums.ComputerTypeEnum;
import com.fuyaogroup.eam.common.enums.PdStatusEnum;
import com.fuyaogroup.eam.common.service.WeixinMessageService;
import com.fuyaogroup.eam.common.service.WeixinService;
import com.fuyaogroup.eam.common.service.qtfwWeixinMessageService;
import com.fuyaogroup.eam.common.service.qtfwWeixinService;
import com.fuyaogroup.eam.modules.fusion.dao.AssetrPdMapper;
import com.fuyaogroup.eam.modules.fusion.model.Asset;
import com.fuyaogroup.eam.modules.fusion.model.AssetPd;
import com.fuyaogroup.eam.modules.fusion.model.AssetPdBat;
import com.fuyaogroup.eam.modules.fusion.model.QtfwThing;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
import com.fuyaogroup.eam.modules.fusion.model.vo.QtfwObject;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdBatService;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.AssetService;
import com.fuyaogroup.eam.modules.fusion.service.WindowServerService;
import com.fuyaogroup.eam.util.EnumUtil;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
import com.fuyaogroup.eam.util.ImageUtil;

@Controller
@Slf4j
public class WeixinManagerController {
	
	static String INFORM_USER_LIST = "101798|999911|083380|";
	
	@Autowired
	FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	
	@Autowired
	WeixinMessageService wxService;
	
	@Autowired
	WeixinService wx;
	
	@Autowired
	qtfwWeixinService qtfwwx;
	
	@Autowired
	qtfwWeixinMessageService qtfwService;
	
	@Autowired
	AssetService assetSevice;
	
	@Autowired
    private WindowServerService wss;
	
	@Autowired
	private AssetrPdMapper assetPd;
	
	@Autowired
	private AssetPdBatService assetPdBat;
	
	@Autowired
	private AssetPdService assetPdService;
	
	public Asset asset;//资产信息
	
	static SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

	public Long timestamp;
	public String nonceStr;
	public String signature;
	public String appId;
	public String url;
	
    @Value("${file.img.path}")
	private  String filePath;
    
    @Value("${file.RBimg.path}")
	private  String qtfwfilePath;
    
    @Value("${file.RBimg.url}")
	private  String rbfileUrl;
    
    @Value("${file.img.url}")
	private  String fileUrl;
    
	
	@RequestMapping(value = "/showPdMessage",method = RequestMethod.GET)
    public String showPdMessage(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String assetSerNum = request.getParameter("assetNum");
		String userId = session.getAttribute("userid")==null?"":session.getAttribute("userid").toString();
		boolean flag = false;//使用人是否和扫码的人一致
		try {
			//通过接口从云获取资产信息，换成从库表中直接获取
			//asset = frutil.getOneAssetBySerNum(assetSerNum);
			List<Asset> alist = assetSevice.getBySerialNumber(assetSerNum);
			if(!CollectionUtil.isEmpty(alist)) {
				asset = alist.get(0);
			}else{
				GlobalException ex = new GlobalException("序列号:"+assetSerNum+",资产不存在，请核实后，再进行盘点！",500);
				request.setAttribute("errException", ex);
				 return "WEB-INF/view/weixinError.jsp";
			}
			if(asset.getJobnum()==null||asset.getJobnum()==""){
				asset.setJobnum(assetSevice.getByAssetId(asset.getAssetId()).get(0).getJobnum());
			}else{
				asset.setJobnum(frutil.comAddZero(asset.getJobnum()));
			}
			
			if(userId.equalsIgnoreCase(asset.getJobnum().toString())){
				flag=true;
			}
			
			request.setAttribute("asset", asset);
		} catch (Exception e) {
			log.error("盘点失败："+e.toString());
		}
		if(!flag){
			//返回错误界面
			session.setAttribute("isFail", "0");//盘点失败
			session.setAttribute("assetSerNum", assetSerNum);
		}else{
			session.setAttribute("assetSerNum", assetSerNum);
			session.setAttribute("isFail", "1");
		}
		
		List<AssetPd> result = assetPdService.getBySerialNum(assetSerNum,0);
		if(result.size()>0) {
			return "WEB-INF/view/weixinPd.jsp";
		}else {
			return "WEB-INF/view/weixinError.jsp";
		}
		 
	
    }
	
	private boolean isCanCheckDate() throws ParseException {
		Date nowDate  = myFormatter.parse(myFormatter.format(new Date()));
		List<AssetPdBat> list = assetPdBat.getAllBDate(nowDate);
		if(CollectionUtil.isEmpty(list)){
			return false;
		}
		return true;
	}

	private AssetPd createPdRecord(Asset asset) {
		AssetPd pd = new AssetPd();
		pd.setAllocation(asset.getAllocation());
		pd.setAssetModel(asset.getAssetmodel());
		pd.setAssetNumber(asset.getAssetNumber());
		pd.setDepartment(asset.getWorkCenterName());
		pd.setDescription(asset.getDescription());
		pd.setJobNum(asset.getJobnum());
		pd.setUserName(asset.getUsername());
		pd.setStatus(PdStatusEnum.ASSET_PD_FINISH.getCode());
		pd.setPdBatId(Long.parseLong(assetPdBat.getDefaultBatId(asset.getOrganizationName())));
		return pd;
	}

	@RequestMapping(value = "/toPdMessage",method = RequestMethod.GET)
    public String toPdMessage(HttpServletRequest request,HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		String userId = session.getAttribute("userid")==null?"":session.getAttribute("userid").toString();
		log.info(userId+"扫码配置开始！");
		session.setAttribute("userid", userId);
		TreeMap<String,String> treeMap = this.getTreeMapSHA1();
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("appId", appId);
		request.setAttribute("signature", signature);
		log.info(userId+"扫码配置结束！");
		 return "WEB-INF/view/weixinQRCode.jsp";
    }
	
	private TreeMap<String, String> getTreeMapSHA1() {
		TreeMap<String,String> treeMap = new TreeMap<String,String>();
		timestamp=System.currentTimeMillis();
//		String time = timestamp.toString().substring(0, 10);
		nonceStr=wx.noncestr;
		appId=wx.corpid;
		
		//jsapi_ticket=JSAPITICKET&noncestr=NONCESTR&timestamp=TIMESTAMP&url=URL
		 String jsapi_ticket= wx.getJsapi_ticket();
		treeMap.put("jsapi_ticket", jsapi_ticket);
		treeMap.put("noncestr",nonceStr);
		treeMap.put("timestamp", timestamp.toString());
		treeMap.put("url",url);
		try {
			signature=wx.sha1(treeMap);
		} catch (NoSuchAlgorithmException e) {
			log.error("进去扫码失败:"+e.getMessage());
		}
		treeMap.put("signature", signature);
		return treeMap;
		
	}
	
	
	
	private TreeMap<String, String> getTreeMapSHA2() {
		TreeMap<String,String> treeMap = new TreeMap<String,String>();
		timestamp=System.currentTimeMillis();
//		String time = timestamp.toString().substring(0, 10);
		nonceStr=qtfwwx.noncestr;
		appId=qtfwwx.corpid;
		
		//jsapi_ticket=JSAPITICKET&noncestr=NONCESTR&timestamp=TIMESTAMP&url=URL
		 String jsapi_ticket= qtfwwx.getJsapi_ticket();
		treeMap.put("jsapi_ticket", jsapi_ticket);
		treeMap.put("noncestr",nonceStr);
		treeMap.put("timestamp", timestamp.toString());
		treeMap.put("url",url);
		try {
			signature=wx.sha1(treeMap);
		} catch (NoSuchAlgorithmException e) {
			log.error("进去扫码失败:"+e.getMessage());
		}
		treeMap.put("signature", signature);
		return treeMap;
		
	}
	

	@RequestMapping(value = "/toPdPage",method = RequestMethod.GET)
    public String toPdPage(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		log.info("{}:toPdPage,开始...",LocalDateTime.now());
		HttpSession session = request.getSession();
		if(!isCanCheckDate()){
			GlobalException ex = new GlobalException("还未开始盘点或者盘点结束，请联系管理员！",500);
			request.setAttribute("errException", ex);
			 return "WEB-INF/view/weixinError.jsp";	
		}
		String code=request.getParameter("code");
		String userId = wx.getUserid(code);
		session.setAttribute("userid", userId);
		url = request.getRequestURL()+"?"+request.getQueryString();
		log.info(userId+"扫码配置开始！");
//		session.setAttribute("userid", userId);
		 this.getTreeMapSHA1();
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("appId", appId);
		request.setAttribute("signature", signature);
		log.info(userId+",扫码配置结束！");
		 return "WEB-INF/view/weixinQRCode.jsp";
		
//		log.info("{}:toPdPage,结束...",LocalDateTime.now());
//	    return "redirect:/toPdMessage";//重定向
    }
	
	@RequestMapping(value = "/eamApprove",method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	@ResponseBody
    public Map<String, Object> eamApprove(HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String flag=session.getAttribute("isFail")==null?"2":session.getAttribute("isFail").toString();
		String assetSerNum=session.getAttribute("assetSerNum")==null?null:session.getAttribute("assetSerNum").toString();
		if(assetSerNum==null){
			flag="2";
		}
		String message="";
		Map<String, Object> resultMap = new HashMap<String, Object>();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("image");// 与前端设置的fileDataName
		if(file==null||file.getOriginalFilename()==null||file.getOriginalFilename()==""||file.isEmpty()){
			resultMap.put("message", "请先上传电脑拍照（要求：可看清固定资产卡片）");
			return resultMap;
		}
		if(flag.equals("1")){
			message="盘点成功！";
			//修改盘点状态-一条盘点表
			List<AssetPd> pdList = assetPdService.getBySerialNum(assetSerNum, null);
			if(CollectionUtil.isEmpty(pdList)){
				AssetPd pd = this.createPdRecord(asset);
				assetPdService.insertPdRecord(pd);
			}
			AssetPd pd = pdList.get(0);
			if(pd.getStatus()==PdStatusEnum.ASSET_PD_FINISH.getCode()){
				message = "已盘点";
				response.setContentType("text/html");
				resultMap.put("message", message);
				return resultMap;
			}
			
			String path =downloadImgFile(file,pd.getPdCode().toString());
			pd.setPdImgPath(path);
			assetPd.updatePdRecord(PdStatusEnum.ASSET_PD_FINISH.getCode(),new Date(),pd.getPdCode(),pd.getPdImgPath());
			response.setContentType("text/html");
			resultMap.put("message", message);
			return resultMap;
		}else if(flag.contentEquals("0")){
			//盘点失败
			message= "盘点有误，非本人操作。如已进行内部转移，请先填写OA单据：固定资产内部转移登记表。";
			//修改盘点状态-一条盘点表
			List<AssetPd> pdList = assetPdService.getBySerialNum(assetSerNum, PdStatusEnum.ASSET_PD_WAITING.getCode());

			if(CollectionUtil.isEmpty(pdList)){
//				message="盘点有误,盘点表不存在！";
				resultMap.put("message", message);
				return resultMap;
//				assetPd.insertPdRecord(pd);
			}
			AssetPd pd = pdList.get(0);
			assetPd.updatePdRecord(PdStatusEnum.ASSET_PD_FAIL.getCode(),new Date(),pd.getPdCode(),"");
			resultMap.put("message", message);
			return resultMap;
		}else{
			message="出现未知异常";
			resultMap.put("message", message);
			return resultMap;
		}
    }
	
	private String downloadImgFile(MultipartFile file, String pdCode) throws Exception {
		String message = "";
	
		FileOutputStream out=null;
        if (null == file || file.isEmpty()) {
            return "文件不能为空！";

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
            	File file2 = new File(url,pdCode+".jpg");
                byte[] f = file.getBytes();
                byte[] data=ImageUtil.zipImageToScaledSize(f,600, 800, "jpg",100000);
                out = new FileOutputStream(file2);
                out.write(data);
                return fileUrl+pdCode;
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

	
	private String qtfwdownloadImgFile(MultipartFile file, String pdCode) throws Exception {
		String message = "";
	
		FileOutputStream out=null;
        if (null == file || file.isEmpty()) {
            return "文件不能为空！";

        }else{
        	message= "文件上传成功！";
            //这里以序列号作为文件夹
            
          //创建文件夹
            File file1 = new File(qtfwfilePath);
            if(!file1.exists()){
            	boolean flag = file1.mkdirs();
            	if(flag)  System.out.println("创建成功");
            	else System.out.println("创建失败");
            } 
            String url = file1.getPath();
            try {
            //获得二进制流并输出
            	File file2 = new File(url,pdCode+".jpg");
                byte[] f = file.getBytes();
                byte[] data=ImageUtil.zipImageToScaledSize(f,600, 800, "jpg",100000);
                out = new FileOutputStream(file2);
                out.write(data);
                return rbfileUrl+pdCode;
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
	
	
	@RequestMapping(value = "/getAssetsInfo",method = RequestMethod.GET)//, produces = "application/json; charset=utf-8")
	public  String getAssetsInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session = request.getSession();
		String code=request.getParameter("code");
		String userId = wx.getUserid(code);
		session.setAttribute("userid", userId);
		
		 List<Asset> list=null;
		try{
		 list= assetSevice.getCmpByJobNum(userId);
		}catch(Exception e){
			throw e;
		}
		 if(CollectionUtil.isEmpty(list)){
			 log.info("userId:"+userId+",list:"+null);
			 GlobalException ex = new GlobalException("资产未查到，请确认资产是否已登记！",500);
				request.setAttribute("errException", ex);
				 return "WEB-INF/view/weixinError.jsp";	
		 }
		 StringBuffer sb = new StringBuffer("您名下共有"+list.size()+"个固定资产：</br>");
		 for(int i=0;i<list.size();i++ ){
			 Asset asset = list.get(i);
			 String htcstr = "";
			 if(asset.getHtcIncredible()==null||asset.getHtcIncredible()>5) {
				 htcstr="机型：未定义，请进系统修改</br>";
			 }else {
				 htcstr="机型："+EnumUtil.getByCode(asset.getHtcIncredible(), ComputerTypeEnum.class).getMessage()+",</br>" ;
			 }
			 sb.append("第"+(i+1)+"个资产：</br>");
			 sb.append("资产编号："+asset.getAssetNumber()+",</br>"
					 +"资产序列号:"+asset.getSerialNumber()+",</br>"
					 +"配置："+asset.getAllocation()+",</br>"
				     +htcstr
					 +"型号："+asset.getAssetmodel()+";</br>"
					 +"-------------------</br>");
		 }
		 GlobalException ex = new GlobalException(sb.toString(),500);
		 request.setAttribute("errException", ex);
		 return "WEB-INF/view/weixinAsset.jsp";	
	}
	
//	public AssetPd downloadImg(HttpServletRequest request,AssetPd pd){
//		String realDir = request.getSession().getServletContext().getRealPath("");
//		String contextpath = request.getContextPath();
//		String basePath = request.getScheme() + "://"
//		+ request.getServerName() + ":" + request.getServerPort()
//		+ contextpath + "/";
// 
//		try {
//		String filePath = "uploadfiles";
//		String realPath = realDir+"\\"+filePath;
//		//判断路径是否存在，不存在则创建
//		File dir = new File(realPath);
//		if(!dir.isDirectory())
//		    dir.mkdir();
// 
//		if(ServletFileUpload.isMultipartContent(request)){
// 
//		    DiskFileItemFactory dff = new DiskFileItemFactory();
//		    dff.setRepository(dir);
//		    dff.setSizeThreshold(1024000);
//		    ServletFileUpload sfu = new ServletFileUpload(dff);
//		    FileItemIterator fii = null;
//		    fii = sfu.getItemIterator(request);
//		   
//		    String fileName = "";
//			String state="SUCCESS";
//			String realFileName="";
//		    while(fii.hasNext()){
//		        FileItemStream fis = fii.next();
//		        String title = pd.getPdCode().toString();   //图片标题
//			    String url = basePath+fis.getName();    //图片地址
//			    pd.setPdImgPath(url);
//		        try{
//		            if(!fis.isFormField() && fis.getName().length()>0){
//		                fileName = fis.getName();
//						Pattern reg=Pattern.compile("[.]jpg|png|jpeg|gif$");
//						Matcher matcher=reg.matcher(fileName);
//						if(!matcher.find()) {
//							state = "文件类型不允许！";
//							break;
//						}
//						realFileName = new Date().getTime()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
//		                url = realPath+"\\"+realFileName;
// 
//		                BufferedInputStream in = new BufferedInputStream(fis.openStream());//获得文件输入流
//		                FileOutputStream a = new FileOutputStream(new File(url));
//		                BufferedOutputStream output = new BufferedOutputStream(a);
//		                Streams.copy(in, output, true);//开始把文件写到你指定的上传文件夹
//		            }else{
//		                String fname = fis.getFieldName();
// 
//		                if(fname.indexOf("pictitle")!=-1){
//		                    BufferedInputStream in = new BufferedInputStream(fis.openStream());
//		                    byte c [] = new byte[10];
//		                    int n = 0;
//		                    while((n=in.read(c))!=-1){
//		                        title = new String(c,0,n);
//		                        break;
//		                    }
//		                }
//		            }
// 
//		        }catch(Exception e){
//		            e.printStackTrace();
//		        }
//		    }
//		}
//		}catch(Exception ee) {
//			ee.printStackTrace();
//		}
//		return pd;
//
//		
//	}
	
	@RequestMapping(value = "/toReturnBackPage",method = RequestMethod.GET)
    public String toBorrowPage(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		log.info("{}:toReturnBackPage,开始...",LocalDateTime.now());
		HttpSession session = request.getSession();
		log.info("144");
//		if(!isCanCheckDate()){
//			GlobalException ex = new GlobalException("还未开始盘点或者盘点结束，请联系管理员！",500);
//			request.setAttribute("errException", ex);
//			 return "WEB-INF/view/weixinError.jsp";	
//		}
		String code=request.getParameter("code");
		log.info("145");
		String userId = qtfwwx.getUserid(code);
		log.info("146");
		session.setAttribute("userid", userId);
		log.info("147");
		url = request.getRequestURL()+"?"+request.getQueryString();
		log.info(userId+"wwwwwwwwwwzzzzzzzzzqqqqqqqqqq 扫码配置开始！");
		log.info(url+"     wwwwwwwwwwzzzzzzzzzqqqqqqqqqq URLLLLL！");
//		session.setAttribute("userid", userId);
		 this.getTreeMapSHA2();
		 log.info("148");
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("appId", appId);
		request.setAttribute("signature", signature);
		log.info(userId+",扫码配置结束！");
		 return "WEB-INF/view/weixinQRCodeOfReturnBack.jsp";
		
//		log.info("{}:toPdPage,结束...",LocalDateTime.now());
//	    return "redirect:/toPdMessage";//重定向
    }
	
	/**
	 * 点击打开扫描物品二维码
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/toBorrowSecondPage",method = RequestMethod.GET)
    public String toBorrowSecondPage(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		log.info("{}:toBorrowSecondPage,开始...",LocalDateTime.now());
		HttpSession session = request.getSession();
		log.info("ss144");
//		if(!isCanCheckDate()){
//			GlobalException ex = new GlobalException("还未开始盘点或者盘点结束，请联系管理员！",500);
//			request.setAttribute("errException", ex);
//			 return "WEB-INF/view/weixinError.jsp";	
//		}
		String code=request.getParameter("code");
		log.info("ss145");
		String userId = qtfwwx.getUserid(code);
		log.info("ss146");
		session.setAttribute("userid", userId);
		log.info("ss147");
		url = request.getRequestURL()+"?"+request.getQueryString();
		log.info(userId+"wwwwwwwwwwzzzzzzzzzqqqqqqqqqq 扫码配置开始！");
		log.info(url+"     wwwwwwwwwwzzzzzzzzzqqqqqqqqqq URLLLLL！");
//		session.setAttribute("userid", userId);
		 this.getTreeMapSHA2();
		 log.info("ss148");
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("appId", appId);
		request.setAttribute("signature", signature);
		log.info(userId+",扫码配置结束！");
		 return "WEB-INF/view/weixinQRCodeOfBorrowSecond.jsp";
		
//		log.info("{}:toPdPage,结束...",LocalDateTime.now());
//	    return "redirect:/toPdMessage";//重定向
    }
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/createReturnBackMessage",method = RequestMethod.GET)
    public String createReturnBackMessage(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String command = request.getParameter("command");
		log.info("createReturnBackMessage二维码扫描的值+++    "+command);
		String userId = session.getAttribute("userid")==null?"":session.getAttribute("userid").toString();
		String userName = qtfwwx.getUsername(userId);
		String mobile = qtfwwx.getUserMobile(userId);
		session.setAttribute("mobile", mobile);
		session.setAttribute("userName", userName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(new Date());
		session.setAttribute("currentTime", currentTime);
		if(command!=null){
			WindowServer ws = wss.queryInBorrowThingOne(command);
			log.info("ws的值为："+ws);
			if(ws!=null) {
				WindowServer wsObj = wss.getReturnSQrecord(command);
				if(wsObj!=null) {
					return "WEB-INF/view/weixinError3.jsp";
				}
				session.setAttribute("windowServer", ws);
				log.info("进行页面跳转");
				return "WEB-INF/view/weixinReturnBack.jsp";
			}else {
				GlobalException ex = new GlobalException("请扫描前台物品二维码！",500);
				request.setAttribute("errException", ex);
				log.info("进入异常跳转页面ws为null");
				 return "WEB-INF/view/weixinError.jsp";
			}
			
			
		}else {
			GlobalException ex = new GlobalException("请扫描前台物品二维码！",500);
			request.setAttribute("errException", ex);
			log.info("进入异常跳转页面command为null");
			return "WEB-INF/view/weixinError.jsp";
		}
		 
	
    }
	
	
	@RequestMapping(value = "/createBorrowSecondMessage",method = RequestMethod.GET)
    public String createBorrowSecondMessage(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String command = request.getParameter("command");
		log.info("二维码扫描的物品序列号值+++    "+command);
		String userId = session.getAttribute("userid")==null?"":session.getAttribute("userid").toString();
		String userName = qtfwwx.getUsername(userId);
		String mobile = qtfwwx.getUserMobile(userId);
		session.setAttribute("mobile", mobile);
		session.setAttribute("userName", userName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(new Date());
		session.setAttribute("currentTime", currentTime);
		if(command!=null) {
			QtfwThing qt = wss.getQtfwThingById(command);
			if(qt!=null) {
				WindowServer wsObj = wss.getSQrecord(command);
				if(wsObj!=null) {
					return "WEB-INF/view/weixinError3.jsp";
				}
				WindowServer windowServer = wss.queryInBorrowThingOne(command);
				if(windowServer==null) {
					session.setAttribute("thingName", qt.getName());
					session.setAttribute("serial", qt.getSerial());
					return "WEB-INF/view/weixinBorrow.jsp";
				}
				return "WEB-INF/view/weixinError2.jsp";
			}else {
				GlobalException ex = new GlobalException("请扫描前台物品二维码！",500);
				request.setAttribute("errException", ex);
				return "WEB-INF/view/weixinError.jsp";
			}
			
		}else {
			GlobalException ex = new GlobalException("请扫描前台物品二维码！",500);
			request.setAttribute("errException", ex);
			 return "WEB-INF/view/weixinError.jsp";	
		}
		 
	
    }
	
	
	@RequestMapping(value = "/borrowApprove",method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object> borrowApprove( HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String message="";
		WindowServer ws = new WindowServer();
		log.info("通过请求方式获取数据："+request.getParameter("userid"));
		log.info("通过请求方式获取数据："+request.getParameter("userName"));
		log.info("通过请求方式获取数据："+request.getParameter("thingName"));
		log.info("通过请求方式获取数据："+request.getParameter("count"));
		log.info("通过请求方式获取数据："+request.getParameter("currentTime"));
		log.info("通过请求方式获取数据："+request.getParameter("mobile"));
		log.info("通过请求方式获取数据："+request.getParameter("serial"));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ws.setTableID((UUID.randomUUID().toString()).substring(0,8));
		if(request.getParameter("userid")==null||"".equals(request.getParameter("userid"))) {
			resultMap.put("message", "借用失败，身份不合法");
			return resultMap;
		}
		ws.setBorrowerId(request.getParameter("userid"));
		ws.setBorrowerName(request.getParameter("userName"));
		ws.setTools(request.getParameter("thingName"));
		ws.setBorrowTime(request.getParameter("currentTime"));
		ws.setCount(Integer.parseInt(request.getParameter("count")));
		ws.setSerial(String.valueOf(session.getAttribute("serial")));
		ws.setMobile(request.getParameter("mobile"));
		ws.setStatus("借用申请");
		ws.setBorrowConfirm("待确认");
		wss.saveWindowServer(ws);
		//response.setContentType("text/html");
		resultMap.put("message", "借用成功，借用期限1天，逾期7天未归还、报废，损坏或丢失，则按照物品原价在工资中扣除");
		qtfwwx.getAccessToken();
		qtfwService.send(INFORM_USER_LIST, "", "收到新的借用请求如下:\n"
				 +"借用人工号:"+ws.getBorrowerId()+"\n"
				 +"借用人姓名:"+ws.getBorrowerName()+"\n"
				 +"物品:"+ws.getTools()+"\n"
				 +"数量:"+ws.getCount()+"\n");
				 		//+ "(请点击<a href=\"http://wxtest.fuyaogroup.com:8888/eam/bgcontroll?idvalue="+ws.getTableID()+"\">这里直接进入处理</a>)");
		return resultMap;
    }
	
	/**
	 * 归还页面，点击下一条，获取数据进行展示
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/nextData",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> nextData(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		log.info("执行nextData.....");
		HttpSession session = request.getSession();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<WindowServer> wslist = new ArrayList<WindowServer>();
		//WindowServer ws = (WindowServer) session.getAttribute("windowServer");
		wslist = (List<WindowServer>) session.getAttribute("windowServerList");
		int index = (int) session.getAttribute("currntPage");
		int total = (int) session.getAttribute("total");
		log.info("当前页："+index+"总数："+total);
		if(index<total-1) {
			WindowServer ws = wslist.get(index+1);
			resultMap.put("tableID",ws.getTableID());
			resultMap.put("userid",ws.getBorrowerId());
			resultMap.put("userName",ws.getBorrowerName());
			resultMap.put("thingName",ws.getTools());
			resultMap.put("count",ws.getCount());
			resultMap.put("borrowTime",ws.getBorrowTime());
			resultMap.put("currnt",index+1);
			session.setAttribute("windowServer",ws);
			session.setAttribute("currntPage",index+1);
			log.info("将"+index+1+"下标的数据放入session："+wslist.get(index+1).toString()+".....");
		}
		log.info("执行nextData结束.....");
		
		return resultMap;
	}
	
	
	
	@RequestMapping(value = "/returnBackApprove",method = RequestMethod.GET)//, produces = "application/json; charset=utf-8"
	@ResponseBody
    public Map<String, Object> returnBackApprove(HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String message="";
		Map<String, Object> resultMap = new HashMap<String, Object>();

//		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//		MultipartFile file = multipartRequest.getFile("image");// 与前端设置的fileDataName
//		if(file==null||file.getOriginalFilename()==null||file.getOriginalFilename()==""||file.isEmpty()){
//			resultMap.put("message", "请先上传电脑拍照（要求：可看被借物品）");
//			return resultMap;
//		}
		
		log.info("获取归还时间："+session.getAttribute("currentTime"));
		WindowServer ws = (WindowServer) session.getAttribute("windowServer");
		//String path =qtfwdownloadImgFile(file,ws.getTableID());
		ws.setReturnTime((String)session.getAttribute("currentTime"));
		ws.setBackConfirm("待确认");
		//ws.setPhoto(path);
		//log.info("获取shang图片路径："+path);
		wss.comfirmBorrow(ws);
		session.setAttribute("waitrbConfirmObj", ws);
		log.info("获取归还物品ID："+ws.getTableID());
		qtfwwx.getAccessToken();
		qtfwService.send(INFORM_USER_LIST, "", "收到新的归还请求如下:\n"
				 +"归回人工号:"+ws.getBorrowerId()+"\n"
				 +"归还人姓名:"+ws.getBorrowerName()+"\n"
				 +"物品:"+ws.getTools()+"\n"
				 +"数量:"+ws.getCount()+"\n"
				 +"联系方式:"+ws.getMobile()+"\n");
				 		//+ "(请点击<a href=\"http://wxtest.fuyaogroup.com:8888/eam/rbcontroll?idvalue="+ws.getTableID()+"\">这里直接进入处理</a>)");
		
			message="归还申请发送成功！";
//			String path =downloadImgFile(file,pd.getPdCode().toString());
//			pd.setPdImgPath(path);
//			response.setContentType("text/html");
			resultMap.put("message", message);
			log.info("返回提示消息："+resultMap);
			return resultMap;
    }
    
	
	
    @RequestMapping(value = "/bgcontroll",method = RequestMethod.GET)
    public String bgcontroll(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String idvalue = request.getParameter("idvalue");
		WindowServer wsobj = wss.queryByTableId(idvalue);
		log.info("查询ID："+idvalue+"查询结果"+wsobj);
		if(wsobj!=null&&("待确认".equals(wsobj.getBorrowConfirm()))) {
			session.setAttribute("waitConfirmObj", wsobj);
			log.info("后台获取到的ID:------"+idvalue);
			return "WEB-INF/view/qtfwBorrowControll.jsp";
		}else {
			return "WEB-INF/view/weixinjumpError.jsp";
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String currentTime = sdf.format(new Date());
		
    }
    
    /*-
     * 催还点击事件
     */
    @RequestMapping(value = "/hurrybgcontroll",method = RequestMethod.GET)
    public String hurrybgcontroll(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String idvalue = request.getParameter("idvalue");
		WindowServer wsobj = wss.queryByTableId(idvalue);
		log.info("查询ID："+idvalue+"查询结果"+wsobj);
		if(wsobj!=null) {
			qtfwService.send(wsobj.getBorrowerId(), "", "您借用的物品"+wsobj.getTools()
			 +"  还未归还，请及时归还。");
			return "WEB-INF/view/weixinInBorrowListLookByAdmin.jsp";
		}else {
			return "WEB-INF/view/weixinjumpError.jsp";
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String currentTime = sdf.format(new Date());
		
    }
    
    @Transactional
    @RequestMapping(value = "/dsbgcontroll",method = RequestMethod.GET)
    public String dsbgcontroll(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String idvalue = request.getParameter("idvalue");
		WindowServer wsobj = wss.queryByTableId(idvalue);
		log.info("查询ID："+idvalue+"查询结果"+wsobj);
		if(wsobj!=null) {
			
			wsobj.setStatus("已丢失");
			wsobj.setBackConfirm("已确认通过申请");
			wss.comfirmBorrow(wsobj);
			
			QtfwThing qThing = new QtfwThing();
			qThing.setSerial(wsobj.getSerial());
			qThing.setStatus("2");
			wss.updateWindowServerThing(qThing);
			
			qtfwService.send(wsobj.getBorrowerId(), "", "您借用的物品"+wsobj.getTools()
			 +"因不慎丢失、损坏，将按照物品原价在您次月工资中扣除，请知悉。");
			return "WEB-INF/view/weixinInBorrowListLookByAdmin.jsp";
		}else {
			return "WEB-INF/view/weixinjumpError.jsp";
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String currentTime = sdf.format(new Date());
		
    }
    
    /**
     * 归还物品申请操作界面
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/rbcontroll",method = RequestMethod.GET)
    public String rbcontroll(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String idvalue = request.getParameter("idvalue");
		WindowServer wsobj = wss.queryByTableId(idvalue);
		log.info("rb查询ID："+idvalue+"rb查询结果"+wsobj);
		if(wsobj!=null&&("待确认".equals(wsobj.getBackConfirm()))) {
			session.setAttribute("waitrbConfirmObj", wsobj);
			log.info("后台获取到的ID:------"+idvalue);
			return "WEB-INF/view/qtfwReturnBackControll.jsp";
		}else {
			return "WEB-INF/view/weixinjumpError.jsp";
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String currentTime = sdf.format(new Date());
		
    }
    
	
    @RequestMapping(value = "/borrowComfirmAction",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> comfirmAction(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//设置序列号
		String idvalue = request.getParameter("acname");
		log.info("操作命令为:------"+idvalue);
		WindowServer wsobj = (WindowServer) session.getAttribute("waitConfirmObj");
		if("pass".equals(idvalue)) {
			log.info("进入PASS操作");
			if(wsobj.getBorrowConfirm().equals("待确认")) {
				wsobj.setStatus("在借");
				wsobj.setBorrowConfirm("已确认通过申请");
				wss.comfirmBorrow(wsobj);
				session.setAttribute("waitConfirmObj", wsobj);
				resultMap.put("message", "申请成功");
				return resultMap;
			}else {
				resultMap.put("message", "操作失败，此记录已失效 ");
				return resultMap;
			}
			
		}else if("cancel".equals(idvalue)) {
			if(wsobj.getBorrowConfirm().equals("待确认")) {
				wss.cancel(wsobj.getTableID());
				resultMap.put("message", "取消成功");
				return resultMap;
			}else {
				resultMap.put("message", "取消失败，此记录已失效");
				return resultMap;
			}
			
		}else {
			resultMap.put("message", "操作失败，未知操作命令");
			return resultMap;
		}
    }
    
    
    /**
     * 归还申请通过或者不通过
     * @param request
     * @param response
     * @return
     * @throws ParseException
     */
    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "/rbcomfirmAction",method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> rbcomfirmAction(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//设置序列号
		String idvalue = request.getParameter("acname");
		String tid = request.getParameter("idvalue");
		WindowServer wsobj = wss.queryByTableId(tid);
		log.info("操作命令为:------"+idvalue);
		log.info("session对象为:------"+wsobj);
		if("pass".equals(idvalue)) {
			log.info("进入归还确认操作");
			if(wsobj!=null&&"待确认".equals(wsobj.getBackConfirm())) {
				wsobj.setStatus("已归还");
				wsobj.setBackConfirm("已确认通过申请");
				wss.comfirmBorrow(wsobj);
				session.setAttribute("waitrbConfirmObj",wsobj);
				resultMap.put("message", "通过成功");
				return resultMap;
			}else {
				resultMap.put("message", "操作失败，此记录已被处理");
				return resultMap;
			}
			
		}else if("cancel".equals(idvalue)) {
			if(wsobj!=null&&"待确认".equals(wsobj.getBackConfirm())){
				wsobj.setBackConfirm("");
				wss.comfirmBorrow(wsobj);
				session.setAttribute("waitrbConfirmObj",wsobj);
				resultMap.put("message", "取消成功");
				return resultMap;
			}else {
				resultMap.put("message", "操作失败，此记录已被处理");
				return resultMap;
			}
			
		}else if("sh".equals(idvalue)){
			
			log.info("进入损坏确认操作");
			if(wsobj!=null&&"待确认".equals(wsobj.getBackConfirm())) {
				wsobj.setStatus("损坏");
				wsobj.setBackConfirm("已确认通过申请");
				wss.comfirmBorrow(wsobj);
				session.setAttribute("waitrbConfirmObj",wsobj);
				resultMap.put("message", "损坏操作成功");
				QtfwThing qThing = new QtfwThing();
				qThing.setSerial(wsobj.getSerial());
				qThing.setStatus("1");
				wss.updateWindowServerThing(qThing);
				qtfwService.send(wsobj.getBorrowerId(), "", "损坏通知"
						 +"借用此物品因不慎损坏，将按照物品原价在您次月工资中扣除，请知悉。");
				return resultMap;
			}else {
				resultMap.put("message", "操作失败，此记录已被处理");
				return resultMap;
			}
		}else if("ds".equals(idvalue)){
			
			log.info("进入丢失确认操作");
			if(wsobj!=null&&"待确认".equals(wsobj.getBackConfirm())) {
				wsobj.setStatus("丢失");
				wsobj.setBackConfirm("已确认通过申请");
				wss.comfirmBorrow(wsobj);
				session.setAttribute("waitrbConfirmObj",wsobj);
				resultMap.put("message", "丢失操作成功");
				QtfwThing qThing = new QtfwThing();
				qThing.setSerial(wsobj.getSerial());
				qThing.setStatus("2");
				wss.updateWindowServerThing(qThing);
				qtfwService.send(wsobj.getBorrowerId(), "", "丢失通知"
						 +"借用此物品因不慎丢失、将按照物品原价在您次月工资中扣除，请知悉。");
				return resultMap;
			}else {
				resultMap.put("message", "操作失败，此记录已被处理");
				return resultMap;
			}
		}else if("bf".equals(idvalue)){
			
			log.info("进入损坏确认操作");
			if(wsobj!=null&&"待确认".equals(wsobj.getBackConfirm())) {
				wsobj.setStatus("报废");
				wsobj.setBackConfirm("已确认通过申请");
				wss.comfirmBorrow(wsobj);
				session.setAttribute("waitrbConfirmObj",wsobj);
				resultMap.put("message", "报废操作成功");
				QtfwThing qThing = new QtfwThing();
				qThing.setSerial(wsobj.getSerial());
				qThing.setStatus("3");
				wss.updateWindowServerThing(qThing);
				qtfwService.send(wsobj.getBorrowerId(), "", "报废通知"
						 +"借用此物品因不慎报废，将按照物品原价在您次月工资中扣除，请知悉。");
				return resultMap;
			}else {
				resultMap.put("message", "操作失败，此记录已被处理");
				return resultMap;
			}
		}else {
			resultMap.put("message", "操作失败，未知操作命令");
			return resultMap;
		}
    }
    
    @RequestMapping(value = "/errorpage",method = RequestMethod.GET)
    public String errorpage(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		return "WEB-INF/view/weixinjumpError.jsp";
    }
    
    
    @RequestMapping(value = "/toShowBorrowPage",method = RequestMethod.GET)
    public String toShowBorrowPage(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String code=request.getParameter("code");
		log.info("进入toShowBorrowPage");
		String userId = qtfwwx.getUserid(code);
		log.info("记录userId："+userId);
		if("999911".equals(userId)) {
			
			List<WindowServer> wsList = wss.queryListByCondition();
			log.info("查询记录："+wsList);
			if(wsList.size()<1){
				return "WEB-INF/view/weixinQueryError.jsp";
			}
			log.info("记录数："+wsList.size());
			session.setAttribute("listSize", wsList);
			session.setAttribute("maxSize", wsList.size());
			session.setAttribute("step",10);
			session.setAttribute("start",0);
			session.setAttribute("end",wsList.size()>10?10:wsList.size());
			return "WEB-INF/view/weixinInBorrowListLookByAdmin.jsp";
		}else {
			log.info("用户不为999911：");
			List<WindowServer> wsList = wss.queryListByUserId(userId);
			if(wsList.size()<1){
				return "WEB-INF/view/weixinQueryError.jsp";
			}
			session.setAttribute("inBorrowList", wsList);
			return "WEB-INF/view/weixinInBorrowList.jsp";
		}
		
    }
    
    
    @RequestMapping(value = "/toAddShowHistory",method = RequestMethod.GET)
    public String toAddShowHistory(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("hmaxSize");
		int step = (int) session.getAttribute("hstep");
		int start = (int) session.getAttribute("hstart");
		int end = (int) session.getAttribute("hend");
		
		if(start>0) {
			session.setAttribute("hstart",start-10);
			session.setAttribute("hend",start);
		}
		//设置序列号
		return "WEB-INF/view/weixinHistoryListLookByAdmin2.jsp";
    }
    
    @GetMapping("/toAddShowHistory/{page}/{limit}")
    public String getFrontCourseList(@PathVariable long page,
                                @PathVariable long limit,
                                Map<String ,Object> map){
        Page<WindowServer> pageCourse = new Page<>(page,limit);
        Map<String,Object> res = wss.getHistoryList(pageCourse);
        map = res;
        return "WEB-INF/view/weixinHistoryListLookByAdmin2.jsp";
    }
    
    
    @RequestMapping(value = "/toDetShowHistory",method = RequestMethod.GET)
    public String toDetShowHistory(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("hmaxSize");
		int step = (int) session.getAttribute("hstep");
		int start = (int) session.getAttribute("hstart");
		int end = (int) session.getAttribute("hend");
		
		if(ms-start>=10) {
			int temp = start+10;
			session.setAttribute("hstart",temp);
			if(ms-temp>=10) {
				session.setAttribute("hend",temp+10);
			}else {
				session.setAttribute("hend",ms);
			}
		}
		//设置序列号
		return "WEB-INF/view/weixinHistoryListLookByAdmin2.jsp";
    }
    
    
    @RequestMapping(value = "/toAddShow",method = RequestMethod.GET)
    public String toAddShow(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("maxSize");
		int step = (int) session.getAttribute("step");
		int start = (int) session.getAttribute("start");
		int end = (int) session.getAttribute("end");
		
		if(start>0) {
			session.setAttribute("start",start-10);
			session.setAttribute("end",start);
		}
		//设置序列号
		return "WEB-INF/view/weixinInBorrowListLookByAdmin.jsp";
    }
    
    @RequestMapping(value = "/toDetShow",method = RequestMethod.GET)
    public String toDetShow(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("maxSize");
		int step = (int) session.getAttribute("step");
		int start = (int) session.getAttribute("start");
		int end = (int) session.getAttribute("end");
		
		if(ms-start>=10) {
			int temp = start+10;
			session.setAttribute("start",temp);
			if(ms-temp>=10) {
				session.setAttribute("end",temp+10);
			}else {
				session.setAttribute("end",ms);
			}
		}
		//设置序列号
		return "WEB-INF/view/weixinInBorrowListLookByAdmin.jsp";
    }
    
    @RequestMapping(value = "/toControllBorrow",method = RequestMethod.GET)
    public String toControllBorrow(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String code=request.getParameter("code");
		log.info("进入toShowBorrowPage");
		String userId = qtfwwx.getUserid(code);
		log.info("记录userId："+userId);
		if("999911".equals(userId) || "101798".equals(userId)||"083380".equals(userId)) {
			
			List<WindowServer> wsList = wss.queryAllBorrowByCondition();
			log.info("查询记录："+wsList);
//			if(wsList.size()<1){
//				return "WEB-INF/view/weixinQueryError.jsp";
//			}
			log.info("记录数："+wsList.size());
			session.setAttribute("wsList", wsList);
			session.setAttribute("wsListmaxSize", wsList.size());
			session.setAttribute("wsListstep",10);
			session.setAttribute("wsListstart",0);
			session.setAttribute("wsListend",wsList.size()>10?10:wsList.size());
			return "WEB-INF/view/weixinInBorrowControllList.jsp";
		}else {
				return "WEB-INF/view/weixinQueryError2.jsp";
		}
		
    }
    
    
    
    @RequestMapping(value = "/toAddShowBorrow",method = RequestMethod.GET)
    public String toAddShowBorrow(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("wsListmaxSize");
		int step = (int) session.getAttribute("wsListstep");
		int start = (int) session.getAttribute("wsListstart");
		int end = (int) session.getAttribute("wsListend");
		
		if(start>0) {
			session.setAttribute("wsListstart",start-10);
			session.setAttribute("wsListend",start);
		}
		//设置序列号
		return "WEB-INF/view/weixinInBorrowControllList.jsp";
    }
    
    @RequestMapping(value = "/toDetShowBorrow",method = RequestMethod.GET)
    public String toDetShowBorrow(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("wsListmaxSize");
		int step = (int) session.getAttribute("wsListstep");
		int start = (int) session.getAttribute("wsListstart");
		int end = (int) session.getAttribute("wsListend");
		
		if(ms-start>=10) {
			int temp = start+10;
			session.setAttribute("wsListstart",temp);
			if(ms-temp>=10) {
				session.setAttribute("wsListend",temp+10);
			}else {
				session.setAttribute("wsListend",ms);
			}
		}
		//设置序列号
		return "WEB-INF/view/weixinInBorrowControllList.jsp";
    }
    
   
    
    @RequestMapping(value = "/toControllReturn",method = RequestMethod.GET)
    public String toControllReturn(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String code=request.getParameter("code");
		log.info("进入toShowBorrowPage");
		String userId = qtfwwx.getUserid(code);
		log.info("记录userId："+userId);
		if("999911".equals(userId) || "101798".equals(userId)||"083380".equals(userId)) {
			
			List<WindowServer> wsList = wss.queryAllReturnByCondition();
			log.info("查询记录："+wsList);
//			if(wsList.size()<1){
//				return "WEB-INF/view/weixinQueryError.jsp";
//			}
			log.info("记录数："+wsList.size());
			session.setAttribute("wsList", wsList);
			session.setAttribute("wsListmaxSize", wsList.size());
			session.setAttribute("wsListstep",10);
			session.setAttribute("wsListstart",0);
			session.setAttribute("wsListend",wsList.size()>10?10:wsList.size());
			return "WEB-INF/view/weixinInReturnControllList.jsp";
		}else {
				return "WEB-INF/view/weixinQueryError2.jsp";
		}
		
    }
    
    
    
    @RequestMapping(value = "/toAddShowReturn",method = RequestMethod.GET)
    public String toAddShowReturn(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("wsListmaxSize");
		int step = (int) session.getAttribute("wsListstep");
		int start = (int) session.getAttribute("wsListstart");
		int end = (int) session.getAttribute("wsListend");
		
		if(start>0) {
			session.setAttribute("wsListstart",start-10);
			session.setAttribute("wsListend",start);
		}
		//设置序列号
		return "WEB-INF/view/weixinInReturnControllList.jsp";
    }
    
    @RequestMapping(value = "/toDetShowReturn",method = RequestMethod.GET)
    public String toDetShowReturn(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		int ms = (int) session.getAttribute("wsListmaxSize");
		int step = (int) session.getAttribute("wsListstep");
		int start = (int) session.getAttribute("wsListstart");
		int end = (int) session.getAttribute("wsListend");
		
		if(ms-start>=10) {
			int temp = start+10;
			session.setAttribute("wsListstart",temp);
			if(ms-temp>=10) {
				session.setAttribute("wsListend",temp+10);
			}else {
				session.setAttribute("wsListend",ms);
			}
		}
		//设置序列号
		return "WEB-INF/view/weixinInReturnControllList.jsp";
    }
    
    @RequestMapping(value = "/toAskReturn",method = RequestMethod.GET)
    public String toAskReturn(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String code=request.getParameter("code");
		log.info("进入toShowBorrowPage");
		String userId = qtfwwx.getUserid(code);
		log.info("记录userId："+userId);
		if("999911".equals(userId)||"083380".equals(userId)||"101798".equals(userId)) {
			
			List<WindowServer> wsList = wss.queryListByCondition();
			log.info("查询记录："+wsList);
			if(wsList.size()<1){
				return "WEB-INF/view/weixinQueryError.jsp";
			}
			log.info("记录数："+wsList.size());
			session.setAttribute("listSize", wsList);
			session.setAttribute("maxSize", wsList.size());
			session.setAttribute("step",10);
			session.setAttribute("start",0);
			session.setAttribute("end",wsList.size()>10?10:wsList.size());
			return "WEB-INF/view/weixinInBorrowListLookByAdmin.jsp";
		}else {
			return "WEB-INF/view/weixinQueryError2.jsp";
		}
		
    }
    
    @RequestMapping(value = "/toShowHistory",method = RequestMethod.GET)
    public String toShowHistory(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String code=request.getParameter("code");
		log.info("进入toShowHistory");
		String userId = qtfwwx.getUserid(code);
		log.info("记录userId："+userId);
		if("999911".equals(userId)||"101798".equals(userId)||"083380".equals(userId)) {
			List<WindowServer> wsList = wss.queryAllByDescBorrowTime();
			log.info("查询记录："+wsList);
			if(wsList.size()<1){
				return "WEB-INF/view/weixinQueryError.jsp";
			}
			log.info("记录数："+wsList.size());
			session.setAttribute("hlistSize", wsList);
			session.setAttribute("hmaxSize", wsList.size());
			session.setAttribute("hstep",10);
			session.setAttribute("hstart",0);
			session.setAttribute("hend",wsList.size()>10?10:wsList.size());
			return "WEB-INF/view/weixinHistoryListLookByAdmin2.jsp";
		}else {
			return "WEB-INF/view/weixinQueryError2.jsp";
		}
		
    }
    
    @RequestMapping(value = "/toTransferPage",method = RequestMethod.GET)
    public String toTransferPage(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		log.info("{}:toTransferPage,开始...",LocalDateTime.now());
		HttpSession session = request.getSession();
		String code=request.getParameter("code");
		String userId = wx.getUserid(code);
		session.setAttribute("userid", userId);
		url = request.getRequestURL()+"?"+request.getQueryString();
		log.info(userId+"扫码配置开始！");
		 this.getTreeMapSHA1();
		
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("appId", appId);
		request.setAttribute("signature", signature);
		log.info(userId+",扫码配置结束！");
		 return "WEB-INF/view/transferQRCode.jsp";
    }
    
    @RequestMapping(value = "/transferReq",method = RequestMethod.GET)
    public String transferReq(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String assetSerNum = request.getParameter("assetNum");
		String userId = session.getAttribute("userid")==null?"":session.getAttribute("userid").toString();
		boolean flag = false;//使用人是否和扫码的人一致
		try {
			List<Asset> alist = assetSevice.getBySerialNumber(assetSerNum);
			if(!CollectionUtil.isEmpty(alist)) {
				asset = alist.get(0);
			}else{
				GlobalException ex = new GlobalException("序列号:"+assetSerNum+",资产不存在，请核实",500);
				request.setAttribute("errException", ex);
				 return "WEB-INF/view/weixinError.jsp";
			}
			if(asset.getJobnum()==null||asset.getJobnum()==""){
				GlobalException ex = new GlobalException("序列号:"+assetSerNum+",使用人工号异常，请联系管理员！",500);
				request.setAttribute("errException", ex);
				 return "WEB-INF/view/weixinError.jsp";
			}else{
				if(userId.equalsIgnoreCase(asset.getJobnum().toString())){
					request.setAttribute("asset", asset);
					
				}else {
					GlobalException ex = new GlobalException("序列号:"+assetSerNum+",您非使用人,请移交自己的资产",500);
					request.setAttribute("errException", ex);
					 return "WEB-INF/view/weixinError.jsp";
				}
			}
			
		} catch (Exception e) {
			log.error("盘点失败："+e.toString());
		}
		
		 return "WEB-INF/view/weixinPd.jsp";
	
    }
    
    
    @RequestMapping(value = "/transferApprove",method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object> transferApprove( HttpServletRequest request,HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String message="";
		WindowServer ws = new WindowServer();
		log.info("通过请求方式获取数据："+request.getParameter("userid"));
		log.info("通过请求方式获取数据："+request.getParameter("userName"));
		log.info("通过请求方式获取数据："+request.getParameter("thingName"));
		log.info("通过请求方式获取数据："+request.getParameter("count"));
		log.info("通过请求方式获取数据："+request.getParameter("currentTime"));
		log.info("通过请求方式获取数据："+request.getParameter("mobile"));
		log.info("通过请求方式获取数据："+request.getParameter("serial"));
//		ws.setTableID((UUID.randomUUID().toString()).substring(0,8));
//		ws.setBorrowerId(request.getParameter("userid"));
//		ws.setBorrowerName(request.getParameter("userName"));
//		ws.setTools(request.getParameter("thingName"));
//		ws.setBorrowTime(request.getParameter("currentTime"));
//		ws.setCount(Integer.parseInt(request.getParameter("count")));
//		ws.setSerial(String.valueOf(session.getAttribute("serial")));
//		ws.setMobile(request.getParameter("mobile"));
//		ws.setStatus("借用申请");
//		ws.setBorrowConfirm("待确认");
//		wss.saveWindowServer(ws);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//response.setContentType("text/html");
		resultMap.put("message", "借用申请成功，等待通过");
//		qtfwwx.getAccessToken();
//		qtfwService.send(INFORM_USER_LIST, "", "收到新的借用请求如下:\n"
//				 +"借用人工号:"+ws.getBorrowerId()+"\n"
//				 +"借用人姓名:"+ws.getBorrowerName()+"\n"
//				 +"物品:"+ws.getTools()+"\n"
//				 +"数量:"+ws.getCount()+"\n");
//				 		//+ "(请点击<a href=\"http://wxtest.fuyaogroup.com:8888/eam/bgcontroll?idvalue="+ws.getTableID()+"\">这里直接进入处理</a>)");
		return resultMap;
    }
    
    
    
    @RequestMapping(value = "/toExport", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> toExport(HttpServletResponse response) throws IOException{
    	
    	try {
    		ExcelWriter writer = null;
            OutputStream outputStream = response.getOutputStream();
          //添加响应头信息
            response.setHeader("Content-disposition", "attachment; filename=" + "前台借用记录.xls");
            response.setContentType("application/msexcel;charset=UTF-8");//设置类型
            response.setHeader("Pragma", "No-cache");//设置头
            response.setHeader("Cache-Control", "no-cache");//设置头
            response.setDateHeader("Expires", 0);//设置日期头
          //实例化 ExcelWriter
            writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLS, true);
          //实例化表单
            Sheet sheet = new Sheet(1, 0, QtfwObject.class);
            sheet.setSheetName("借用记录");
            
            
        	List<WindowServer> wsList = new ArrayList<WindowServer>();
        	List<QtfwObject> QOList = new ArrayList<QtfwObject>();
        	wsList = wss.queryAll();
        	if(wsList == null || wsList.size()<1) {
        		return null;
        	}
        	wsList.forEach(ws->{
        		QtfwObject qObject = new QtfwObject();
        		BeanUtils.copyProperties(ws, qObject);
        		QOList.add(qObject);
        	});
        	
        	//输出
            writer.write(QOList, sheet);
            writer.finish();
            outputStream.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
	            response.getOutputStream().close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
    	
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("res", "成功导出");
    	return map;
    }
    
    
    
    
}