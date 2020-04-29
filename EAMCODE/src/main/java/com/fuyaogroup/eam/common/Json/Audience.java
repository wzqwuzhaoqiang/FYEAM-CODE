package com.fuyaogroup.eam.common.Json;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * Token认证信息
 * @author fuyao
 *
 */

//@ConfigurationProperties(prefix = "audience")
//@PropertySource(value ="classpath:/jwt.properties")
@Component
public class Audience {
	
	@Value("${audience.grantType}")
	private  String grantType;
	
	@Value("${audience.userName}")
	private  String userName;
	
	@Value("${audience.password}")
	private  String password;
	
	@Value("${audience.expiresSecond}")
	private  int expiresSecond;
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getExpiresSecond() {
		return expiresSecond;
	}
	public void setExpiresSecond(int expiresSecond) {
		this.expiresSecond = expiresSecond;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
}

