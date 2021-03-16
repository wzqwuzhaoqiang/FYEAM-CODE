package com.fuyaogroup.eam.controller;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuyaogroup.eam.common.Exception.GlobalException;
import com.fuyaogroup.eam.common.service.BookWeixinService;
import com.fuyaogroup.eam.common.service.WeixinService;
import com.fuyaogroup.eam.common.service.qtfwWeixinMessageService;
import com.fuyaogroup.eam.common.service.qtfwWeixinService;
import com.fuyaogroup.eam.modules.fusion.model.BookBorrowDetial;
import com.fuyaogroup.eam.modules.fusion.model.BookEntry;
import com.fuyaogroup.eam.modules.fusion.model.QtfwThing;
import com.fuyaogroup.eam.modules.fusion.model.WindowServer;
import com.fuyaogroup.eam.modules.fusion.model.vo.BookInfoVo;
import com.fuyaogroup.eam.modules.fusion.service.BookServerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class BookWeixinManageController {

	static String INFORM_USER_LIST = "101798|083380|138603|146099";
	
	public Long timestamp;
	public String nonceStr;
	public String signature;
	public String appId;
	public String url;
	
	@Autowired
	BookServerService bss;
	
	@Autowired
	BookWeixinService qtfwwx;
	
	@Autowired
	WeixinService wx;
	
	@Autowired
	qtfwWeixinMessageService qtfwService;
	
	
	
	
	/*
	 * 完成书籍从扫码->展示信息->申请完成借用
	 * 
	 * */
	/**
	 *  APP点击'借用扫码' 完成配置跳转到扫码页面
	 */
	@RequestMapping(value = "/bookBorrowPage",method = RequestMethod.GET)
    public String bookBorrowPage(HttpServletRequest request,HttpServletResponse response) throws ParseException {
		log.info("{}:toBorrowSecondPage,开始...",LocalDateTime.now());
		HttpSession session = request.getSession();
		String code=request.getParameter("code");
		String userId = qtfwwx.getUserid(code);
		session.setAttribute("userid", userId);
		url = request.getRequestURL()+"?"+request.getQueryString();
		 this.getTreeMapSHA2();
		request.setAttribute("timestamp", timestamp);
		request.setAttribute("nonceStr", nonceStr);
		request.setAttribute("appId", appId);
		request.setAttribute("signature", signature);
		log.info(userId+",扫码配置结束！");
		 return "WEB-INF/view/bookManage/borrowWeixinQRCode.jsp";
    }
	
	
	/**
	 * 扫码之后跳转到此方法，完成业务逻辑，再根据结果进行相应跳转
	 */
	@RequestMapping(value = "/insertBookBorrow",method = RequestMethod.GET)
    public String insertBookBorrow(HttpServletRequest request,HttpServletResponse response) throws ParseException{
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
			BookEntry qt = bss.getBookDetial(command);
			if(qt!=null) {
				int count = bss.selectCount(command,2);
				if(count>0) {
					return "WEB-INF/view/bookManage/bookServerError.jsp";
				}
				session.setAttribute("thingName", qt.getBookName());
				session.setAttribute("serial", qt.getBookCode());
				return "WEB-INF/view/bookManage/weixinBorrow.jsp";
			}else {
				GlobalException ex = new GlobalException("请扫描图书二维码！",500);
				request.setAttribute("errException", ex);
				return "WEB-INF/view/weixinError.jsp";
			}
			
		}else {
			GlobalException ex = new GlobalException("请扫描图书二维码！",500);
			request.setAttribute("errException", ex);
			 return "WEB-INF/view/weixinError.jsp";	
		}
		 
	
    }
	
	
	/**
	 * 完成借用提交
	 */
	
	@RequestMapping(value = "/bookApprove",method = RequestMethod.POST)
	@ResponseBody
    public Map<String, Object> bookApprove( HttpServletRequest request,HttpServletResponse response) throws Exception {
		log.info("++++++bookApprove++++++    ");
		HttpSession session = request.getSession();
		String message="";
		BookBorrowDetial ws = new BookBorrowDetial();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ws.setBid((UUID.randomUUID().toString()).substring(0,8));
		if(request.getParameter("userid")==null||"".equals(request.getParameter("userid"))) {
			resultMap.put("message", "借用失败，身份不合法");
			return resultMap;
		}
		ws.setManID(request.getParameter("userid"));
		ws.setName(request.getParameter("userName"));
		ws.setBookName(request.getParameter("thingName"));
		ws.setBtime(request.getParameter("currentTime"));
		ws.setBookID(String.valueOf(session.getAttribute("serial")));
		ws.setTelphone(request.getParameter("mobile"));
		ws.setState(0);
		log.info("++++++bookApprove2222222++++++    ");
		bss.saveBook(ws);
		log.info("++++++bookApprove333333++++++    ");
		//response.setContentType("text/html");
		resultMap.put("message", "借阅成功");
		qtfwwx.getAccessToken();
		qtfwService.sendBook(INFORM_USER_LIST, "", "收到新的借用消息:\n"
				 +"借阅人工号:"+ws.getManID()+"\n"
				 +"姓名:"+ws.getName()+"\n"
				 +"书名:"+ws.getBookName()+"\n"
				 +"时间:"+ws.getBtime()+"\n");
				 		//+ "(请点击<a href=\"http://wxtest.fuyaogroup.com:8888/eam/bgcontroll?idvalue="+ws.getTableID()+"\">这里直接进入处理</a>)");
		return resultMap;
    }
	
	
	
	/*
	 * 查看在借书籍情况，并可发送归还请求
	 * 
	 *  
	 * */
	
	@RequestMapping(value = "/toShowBookInfo",method = RequestMethod.GET)
    public String toShowBookInfo(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		
		String code=request.getParameter("code");
		log.info(code+"++++++bookApprovecode++++++    ");
		String userId = qtfwwx.getUserid(code);
		log.info(userId+"++++++bookApprove工号++++++    ");
		if("999911".equals(userId)) {
			
//			List<WindowServer> wsList = wss.queryListByCondition();
//			log.info("查询记录："+wsList);
//			if(wsList.size()<1){
//				return "WEB-INF/view/weixinQueryError.jsp";
//			}
//			log.info("记录数："+wsList.size());
//			session.setAttribute("listSize", wsList);
//			session.setAttribute("maxSize", wsList.size());
//			session.setAttribute("step",10);
//			session.setAttribute("start",0);
//			session.setAttribute("end",wsList.size()>10?10:wsList.size());
//			return "WEB-INF/view/weixinInBorrowListLookByAdmin.jsp";
			return null;
		}else {
			
			int count = bss.selectCountManId(userId,2);
			
			if(count<1){
				return "WEB-INF/view/bookManage/weixinQueryError.jsp";
			}
			log.info("jieguoq");
			List<BookBorrowDetial>wsList = bss.selectlistManId(userId,2);
			log.info("结果数："+wsList);
			session.setAttribute("inBorrowList", wsList);
			return "WEB-INF/view/bookManage/weixinInBorrowList.jsp";
		}
		
    }
	
	
	
	/*
	 * 请求归还，等待管理员确认
	 * */
	@RequestMapping(value = "/updateBookState",method = RequestMethod.GET)
	@ResponseBody
    public Map<String, Object> updateBookState(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		String command = request.getParameter("command");
		log.info("createReturnBackMessage二维码扫描的值+++    "+command);
		String userId = session.getAttribute("userid")==null?"":session.getAttribute("userid").toString();
		String userName = qtfwwx.getUsername(userId);
//		String mobile = qtfwwx.getUserMobile(userId);
//		session.setAttribute("mobile", mobile);
//		session.setAttribute("userName", userName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentTime = sdf.format(new Date());
		session.setAttribute("currentTime", currentTime);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(command!=null){
//			QueryWrapper<BookBorrowDetial> wrapper = new QueryWrapper<BookBorrowDetial>();
//			wrapper.eq("id", command);
			BookBorrowDetial ws = bss.selectGetById(command);
			log.info("ws的值为："+ws);
			if(ws!=null) {
				ws.setState(1);
				ws.setRtime(currentTime);
				bss.updateBookById(ws);
				session.setAttribute("bookServer", ws);
				resultMap.put("message", "成功发出归还申请");
				qtfwwx.getAccessToken();
				qtfwService.sendBook(INFORM_USER_LIST, "", "收到归还申请请求如下:\n"
						 +"借阅人工号:"+ws.getManID()+"\n"
						 +"姓名:"+ws.getName()+"\n"
						 +"书名:"+ws.getBookName()+"\n"
						 +"归还时间:"+ws.getRtime()+"\n");
				
				
				log.info("进行页面跳转");
				return resultMap;
			}else {
				GlobalException ex = new GlobalException("请扫描图书二维码！",500);
				request.setAttribute("errException", ex);
				log.info("进入异常跳转页面ws为null");
				resultMap.put("message", "归还申请失败");
				 return resultMap;
			}
			
			
		}else {
			GlobalException ex = new GlobalException("请扫描图书二维码！",500);
			request.setAttribute("errException", ex);
			resultMap.put("message", "归还申请失败");
			 return resultMap;
		}
		 
	
    }
	
	
	
	/*
	 * 后台管理人员处理归还信息
	 * 
	 * toBookReturnPage
	 * */
	
	@RequestMapping(value = "/toBookReturnPage",method = RequestMethod.GET)
    public String toBookReturnPage(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String code=request.getParameter("code");
		log.info("进入toShowBorrowPage");
		String userId = qtfwwx.getUserid(code);
		log.info("记录userId："+userId);
		if("999911".equals(userId) || "101798".equals(userId)||"083380".equals(userId)||"138603".equals(userId)||"146099".equals(userId)) {
			List<BookBorrowDetial> wsList = bss.selectList(1);
			log.info("查询记录："+wsList);
//			if(wsList.size()<1){
//				return "WEB-INF/view/weixinQueryError.jsp";
//			}
			log.info("记录数："+wsList.size());
			session.setAttribute("wsList", wsList);
//			session.setAttribute("wsListmaxSize", wsList.size());
//			session.setAttribute("wsListstep",10);
//			session.setAttribute("wsListstart",0);
//			session.setAttribute("wsListend",wsList.size()>10?10:wsList.size());
			return "WEB-INF/view/bookManage/weixinInReturnControllList.jsp";
		}else {
				return "WEB-INF/view/bookManage/weixinQueryError2.jsp";
		}
		
    }
	
	/*
	 * 接收归还书籍
	 * rbBookcontroll
	 * */
	@RequestMapping(value = "/rbBookcontroll",method = RequestMethod.GET)
	@ResponseBody
    public Map<String, Object> rbBookcontroll(HttpServletRequest request,HttpServletResponse response) throws ParseException{
		HttpSession session = request.getSession();
		//设置序列号
		qtfwwx.getAccessToken();
		String idvalue = request.getParameter("idvalue");
		log.info("idvalue数："+idvalue);
		BookBorrowDetial wss = bss.selectGetById(idvalue);
		log.info("结果数："+wss);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(wss!=null&&wss.getState()==1) {
			wss.setState(2);
			bss.updateBook(wss);
			resultMap.put("message", "接收成功");
			//session.setAttribute("waitrbConfirmObj", wsobj);
			return resultMap;
		}else {
			resultMap.put("message", "接收失败");
			return resultMap;
		}
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String currentTime = sdf.format(new Date());
		
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
	
}
