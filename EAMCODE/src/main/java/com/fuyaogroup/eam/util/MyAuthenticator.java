/**
 *
 * Copyright (c) 2017-2019 福耀玻璃工业集团股份有限公司
 * All rights reserved.
 *
 * 注意：本内容仅限于福耀玻璃工业集团股份有限公司内部传阅
 * 禁止外泄以及用于其他的商业目的,违者必究！
 * =======================================================
 * 公司地址：福建省福清市福耀工业区II
 * 邮       编：350301 
 * 网       址：http://www.fuyaogroup.com/
 */
package com.fuyaogroup.eam.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * 
 * @author CJ ♦ Wang
 * @version 1.0
 * 
 */
public class MyAuthenticator extends Authenticator{ 
	  String _userName=null; 
	  char[] _password=null; 
	    
	  public MyAuthenticator(){ 
	  } 
	  public MyAuthenticator(String username, String password) {  
	    this._userName = username;  
	    this._password = password.toCharArray();  
	  }  
	  protected PasswordAuthentication getPasswordAuthentication(){ 
	    return new PasswordAuthentication(_userName, _password); 
	  } 


}
