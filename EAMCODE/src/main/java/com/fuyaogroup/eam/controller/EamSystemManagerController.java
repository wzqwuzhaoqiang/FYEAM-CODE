package com.fuyaogroup.eam.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fuyaogroup.eam.modules.fusion.model.Config;
import com.fuyaogroup.eam.modules.fusion.service.AssetPdService;
import com.fuyaogroup.eam.modules.fusion.service.ConfigService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

@Controller
@CrossOrigin(origins = "*")
@Slf4j
public class EamSystemManagerController {

	@Autowired
	ConfigService configService;

	@RequestMapping(value = "/login",method = RequestMethod.POST)
	@ResponseBody
	@CrossOrigin(origins = "*")
	public String toLoginPage(@RequestBody String request,HttpServletRequest req) throws Exception {
//		log.info("登录开始！");
		Gson gson=new Gson();
		JSONObject json = JSONObject.fromObject(request);


		HashMap<String,String> map = new HashMap<String,String>();
		String username =json.get("userName")==null?null:json.get("userName").toString()  ;
		String password  = json.get("password")==null?null:json.get("password").toString();
		if (username==null||password==null){
//			map.put("success", "false");
//			map.put("errMeg", "账号/密码为空！");
//			String str = gson.toJson(map);
//			return str;
			throw new Exception("账号/密码为空！");
		}
		List<Config> list = configService.getUser(4,username);
		if(CollectionUtils.isEmpty(list)){
			map.put("success", "false");
			map.put("errMsg", "账号不存在!");
			String str = gson.toJson(map);
			return str;
		}else{
			if(!list.get(0).getConfigVal().equals(password)){
                map.put("success", "false");
                map.put("errMsg","密码有误！");
                String str = gson.toJson(map);
                return str;

			}
		}
        map.put("success", "true");
		map.put("token",list.get(0).getConfigName());
		map.put("user_id", list.get(0).getConfigId().toString());
		map.put("password",list.get(0).getConfigVal());
		map.put("name", list.get(0).getComments());
		map.put("username", list.get(0).getConfigName());
		map.put("setAccess", list.get(0).getConfigCode());
		String str = gson.toJson(map);
		HttpSession session = req.getSession();
		session.setAttribute("username", list.get(0).getConfigName());
		System.out.println(session.getAttribute("username")+"session:"+session);
		return str;
	}

	@RequestMapping(value = "/getInfo",method = RequestMethod.GET)
	@ResponseBody
	@CrossOrigin(origins = "*")
	public String getUserInfo(@Param("token") String token){
		Gson gson=new Gson();
		HashMap<String,String> map = new HashMap<String,String>();
		String username  = token;
		List<Config> list = configService.getUser(4,username);
		if(CollectionUtils.isEmpty(list)){
			map.put("success", "false");
			map.put("errMeg", "账号不存在");
			String str = gson.toJson(map);
			return str;
		}

		map.put("user_id", list.get(0).getConfigId().toString());
		map.put("password",list.get(0).getConfigVal());
		map.put("name", list.get(0).getComments());
		map.put("username", list.get(0).getConfigName());
		map.put("setAccess", list.get(0).getConfigCode());
		map.put("avatar", "avatar");
		String str = gson.toJson(map);
		return str;
	}

	@RequestMapping(value = "/loading",method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request,HttpServletResponse response){
		return "WEB-INF/view/home.jsp";
	}

	@RequestMapping(value = "/logout",method = RequestMethod.GET)
	public String logout(HttpServletRequest request,HttpServletResponse response){

		return "WEB-INF/view/login.jsp";
	}
}