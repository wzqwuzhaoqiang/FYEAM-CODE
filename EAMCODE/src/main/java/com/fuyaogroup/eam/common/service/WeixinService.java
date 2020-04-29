package com.fuyaogroup.eam.common.service;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.TreeMap;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
@Slf4j
@Service
public class WeixinService {

    public static String corpid = "wxc625758e50b5ced1";
    public static String sercret = "BzvUuAbthlXuEXqwMJTWrzWCzGfFDbR2aDDl8czPHIA";
    public static String accessToken;
    public static String jsapi_Ticket;
    public static Date expire_time;
    public static Date token_expire_time;
    public static String noncestr="tdwci0qeZd5NaKlWPxBYDUgjvsdRTvK7fMTJ_PyIQ_i4xAHEJAbNNy";
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getAccessToken() {
    	if(token_expire_time==null||token_expire_time.before(new Date())){//如果access_Token已经过期或者还没去获取到
    		accessToken =  this.getAccessTokenFromWX() ;
    	}else{
    		accessToken = accessToken == null ? this.getAccessTokenFromWX() : accessToken;
    	}
      
        return accessToken;
    }

    public String getAccessTokenFromWX() {
        String access_token = "";
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet hg = new HttpGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpid + "&corpsecret=" + sercret + "");
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(hg, responseHandler);
            logger.info("远程获取AccessToken：" + responseBody + "");
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createJsonParser(responseBody);
            access_token = "";
            //System.out.print(parser);

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                //out.println((parser.getCurrentToken() == JsonToken.FIELD_NAME) + "    " + parser.getValueAsString());
                if (parser.getCurrentToken() == JsonToken.VALUE_STRING && parser.getCurrentName().equals("access_token")) {
                    //out.println(parser.getCurrentName().equals("access_token"));
                    access_token = parser.getValueAsString().toString();
                    accessToken = access_token;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        token_expire_time=DateUtils.addHours(new Date(), 1);
        return access_token;
    }

    public String getUserid(String code) {
        String accesstoken =  this.getAccessToken();
        String UserId = "";
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet hg = new HttpGet("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + accesstoken + "&code=" + code);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(hg, responseHandler);
//            logger.info("远程获取Userid：" + responseBody + "");
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createJsonParser(responseBody);

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                //out.println((parser.getCurrentToken() == JsonToken.FIELD_NAME) + "    " + parser.getValueAsString());
                if (parser.getCurrentToken() == JsonToken.VALUE_STRING && parser.getCurrentName().equals("UserId")) {
                    //out.println(parser.getCurrentName().equals("access_token"));
                    UserId = parser.getValueAsString().toString();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return UserId;
    }

//    public Map<String, String> getWxsign(String url) {
//        Map<String, String> ret = new HashMap<String, String>();
//        ret = new WeixinSign().sign(this.getJsapi_ticket(), url);
//        ret.put("appId",this.corpid);
//        ret.put("corpid",this.corpid);
//        return ret;
//    }

    public String getJsapi_ticket() {
    	String jsapi_ticket = null;
    	if(expire_time==null||expire_time.before(new Date())){//如果jsapi的ticket已经过期或者还没去获取到
    		   jsapi_ticket =  this.getJsapi_ticketFromWX() ;
    	}else{
    		 jsapi_ticket = jsapi_Ticket == null ? this.getJsapi_ticketFromWX() : jsapi_Ticket;
    	}
      
        return jsapi_ticket;
    }
    
    public String getJsapi_ticketFromWX() {
        String accesstoken = this.getAccessToken();
        String jsapi_ticket = "";
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            accesstoken = URLEncoder.encode(accesstoken);
            String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token=" + accesstoken;
            //System.out.println(url);
            HttpGet hg = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpclient.execute(hg, responseHandler);
            logger.info("远程获取Jsapi_ticket：" + responseBody + "");
            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createJsonParser(responseBody);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                //out.println((parser.getCurrentToken() == JsonToken.FIELD_NAME) + "    " + parser.getValueAsString());
                if (parser.getCurrentToken() == JsonToken.VALUE_STRING && parser.getCurrentName().equals("ticket")) {
                    //out.println(parser.getCurrentName().equals("access_token"));
                    jsapi_ticket = parser.getValueAsString().toString();
                    jsapi_Ticket = jsapi_ticket;
                }
            }
            expire_time=DateUtils.addHours(new Date(), 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("jsapi_ticket="+jsapi_ticket);
        return jsapi_ticket;
    }

    /**获取微信签名
     * jsapi_ticket=JSAPITICKET&noncestr=NONCESTR&timestamp=TIMESTAMP&url=URL
     * @param map
     * @return
     * @throws NoSuchAlgorithmException
     */
    public String sha1(TreeMap<String,String> map) throws NoSuchAlgorithmException{
    	StringBuffer str=new StringBuffer("");
    	for(String key:map.keySet()){
    		str.append(key+"="+map.get(key)+"&");
    	}
    	MessageDigest sha1; 
    	 sha1 = MessageDigest.getInstance("SHA1"); 
    	 String sha1Str = str.substring(0, str.length()-1).toString();
    	 log.info(sha1Str);
    	 sha1.update(sha1Str.getBytes()); //先更新摘要 
    	 byte[] digest = sha1.digest(); //再通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，摘要被重置。 
    	   
    	 String hex = toHex(digest); 
    	 System.out.println("SHA1摘要:" + hex); 
    	 return hex; 

    }

    /** 
     * sha1 摘要转16进制 
     * @param digest 
     * @return 
     */
     private static String toHex(byte[] digest) { 
     StringBuilder sb = new StringBuilder(); 
     int len = digest.length; 
       
     String out = null; 
     for (int i = 0; i < len; i++) { 
    //  out = Integer.toHexString(0xFF & digest[i] + 0xABCDEF); //加任意 salt 
      out = Integer.toHexString(0xFF & digest[i]);//原始方法 
      if (out.length() == 1) { 
      sb.append("0");//如果为1位 前面补个0 
      } 
      sb.append(out); 
     } 
     return sb.toString(); 
     } 
      

     public String getWeixin_userId(String code) {
    	 String token = this.getAccessToken();
    	 String userId=this.getUserIdFromWx(code,token);
		return userId;
    	 
     }

	private String getUserIdFromWx(String code, String token) {
   	 String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+token+"&code="+code;
   	 String user_id = "";
     try {
         CloseableHttpClient httpclient = HttpClients.createDefault();
         HttpGet hg = new HttpGet(url);
         ResponseHandler<String> responseHandler = new BasicResponseHandler();
         String responseBody = httpclient.execute(hg, responseHandler);
         logger.info("远程获取user_id：" + responseBody + "");
         JsonFactory factory = new JsonFactory();
         JsonParser parser = factory.createJsonParser(responseBody);
         user_id = "";
         //System.out.print(parser);

         while (parser.nextToken() != JsonToken.END_OBJECT) {
             //out.println((parser.getCurrentToken() == JsonToken.FIELD_NAME) + "    " + parser.getValueAsString());
             if (parser.getCurrentToken() == JsonToken.VALUE_STRING && parser.getCurrentName().equals("userid")) {
                 //out.println(parser.getCurrentName().equals("access_token"));
            	 user_id = parser.getValueAsString().toString();
             }
         }
     } catch (Exception e) {
         e.printStackTrace();
     }

     return user_id;
		
	}
	

}
