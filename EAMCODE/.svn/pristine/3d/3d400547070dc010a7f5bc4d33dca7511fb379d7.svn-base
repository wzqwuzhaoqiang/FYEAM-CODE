package com.fuyaogroup.eam.common.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fuyaogroup.eam.common.Json.AccessToken;
import com.fuyaogroup.eam.common.Json.Audience;
import com.fuyaogroup.eam.common.Json.JsonResult;
import com.fuyaogroup.eam.common.Json.LogPara;
import com.fuyaogroup.eam.util.JwtHelperUtil;
import com.fuyaogroup.eam.util.SignUtil;
 
 
@RestController
public class JsonWebTokenController {
	@Autowired
	private Audience audienceEntity;
	
	@RequestMapping(value = "/token", method = RequestMethod.POST,
	        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
	        produces = { MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public  JsonResult getAccessToken(LogPara loginPara)
	{
		JsonResult resultMsg;
		try
		{
			if(loginPara.getGrant_type()== null 
					|| (loginPara.getGrant_type().compareTo(audienceEntity.getGrantType()) != 0))
			{
				resultMsg = new JsonResult("500", 
						"获取Token认证方式错误！", null);
				return resultMsg;
			}
			
				String md5Password = SignUtil.getMD5(audienceEntity.getPassword());
				if (md5Password.compareTo(loginPara.getPassword()) != 0)
				{
					resultMsg = new JsonResult("500",
							"Token认证失败，密码错误！", null);
					return resultMsg;
				}
			//拼装accessToken
			String accessToken = JwtHelperUtil.createJWT(loginPara.getUsername(),
					 audienceEntity.getGrantType(), audienceEntity.getUserName(),
					audienceEntity.getExpiresSecond() * 1000, audienceEntity.getPassword());
			
			//返回accessToken
			AccessToken accessTokenEntity = new AccessToken();
			accessTokenEntity.setAccess_token(accessToken);
			accessTokenEntity.setExpires_in(audienceEntity.getExpiresSecond());
			accessTokenEntity.setToken_type("bearer");
			resultMsg = new JsonResult("200", 
					"Token认证成功！", accessTokenEntity);
			return resultMsg;
			
		}
		catch(Exception ex)
		{
			resultMsg = new JsonResult("500", 
					"Token认证失败", ex.getMessage());
			return resultMsg;
		}
	}
}
