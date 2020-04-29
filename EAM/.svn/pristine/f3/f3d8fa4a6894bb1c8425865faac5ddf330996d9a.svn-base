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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**   
 * 
 * @author CJ ♦ Wang
 * @version 1.0
 *  
 */
@Slf4j
@Component
@ConfigurationProperties
public class FusionRestUtil {
	
	@Value("${interface.fusionSoap.deaultUserName}")
	private  String deaultUserName;
	
	@Value("${interface.fusionSoap.deaultPassword}")
	private  String deaultPassword;
	
	 private static class TrustAnyTrustManager implements X509TrustManager {
		 
	        public void checkClientTrusted(X509Certificate[] chain, String authType)
	                throws CertificateException {
	        }
	 
	        public void checkServerTrusted(X509Certificate[] chain, String authType)
	                throws CertificateException {
	        }
	 
	        public X509Certificate[] getAcceptedIssuers() {
	            return new X509Certificate[] {};
	        }
	    }
	 
	  private static class TrustAnyHostnameVerifier implements HostnameVerifier {
	        public boolean verify(String hostname, SSLSession session) {
	            return true;
	        }
	    }
	/**
	 * get提交
	 * @param url url地址
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 * @throws java.lang.Exception 
	 */
	public String get(String url,String userName,String password) throws java.lang.Exception {
		//log.info("=====in FusionRestUtil get=====");
		//翻译服务的URL
//		String servicesUrl = "https://ekfa-test.fa.us2.oraclecloud.com/fscmRestApi/resources/11.13.18.05/items?limit=1&onlyData=true&orderBy=ItemId:asc&q=LastUpdatedBy!=IT-LY;LastUpdateDateTime!=2019-01-04 02:24:20";
		String servicesUrl = url;
		String result = "";
		  SSLContext sc = SSLContext.getInstance("SSL");
		 sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
	                new java.security.SecureRandom());
		URL realUrl = new URL(servicesUrl);
		HttpsURLConnection connection = (HttpsURLConnection)realUrl.openConnection();

		BufferedReader in = null;
		try {
			//认证信息对象，用于包含访问翻译服务的用户名和密码
			Authenticator auth = new MyAuthenticator(userName, password);
			Authenticator.setDefault(auth);
			
			// 打开和URL之间的连接
			connection.setDoInput(true);  
	        connection.setDoOutput(true);//允许连接提交信息      
	        connection.setConnectTimeout(300000); 
			connection.setReadTimeout(300000);//TODO 测试
	        connection.setRequestMethod("GET");
	        connection.setSSLSocketFactory(sc.getSocketFactory());
	        connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
	        Boolean flag=true;
			// 建立实际的连接
			connection.connect();
			if (connection.getResponseCode() >= 400 ) {//连接失败报错
				if(null==connection.getErrorStream()){
					flag = false;
					log.error("=====The Result of FusionRestUtil get is=====>"+connection.getResponseMessage());
					throw Exception(connection.getResponseMessage());
				}else{
					in = new BufferedReader(new InputStreamReader(
							 connection.getErrorStream()));
					flag = false;
				}
				
			}
			
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
//			log.info("============================");
//			log.info(result);
//			log.info("=====The Result of FusionRestUtil get is=====>"+result);
			connection.disconnect();
			if(flag)
				return result;
			else{
				System.out.print(result);
//				log.info("=====The Result of FusionRestUtil get is=====>"+result);
				return null;
			}
				
		} catch (Exception e) {
			log.error("=====The Result of FusionRestUtil get is=====>"+e.getMessage());
			connection.disconnect();
				throw Exception("Get失败，错误信息:"+e.toString());
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * get提交方法
	 * @param url url地址
	 * @return
	 * @throws java.lang.Exception 
	 */
	public String get(String url) throws java.lang.Exception {
		return get(url, deaultUserName, deaultPassword);
	}
	
	/**
	 * post提交
	 * @param url url地址
	 * @param param 提交数据json串
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 * @throws java.lang.Exception 
	 */
	public String post(String url,String param,String userName,String password) throws java.lang.Exception {
//		log.info("=====in FusionRestUtil post=====");
//		log.info("=====The Request param of FusionRestUtil post is====="+param);
		//翻译服务的URL
//		String servicesUrl = "https://ekfa-test.fa.us2.oraclecloud.com/crmRestApi/resources/11.13.18.05/activities";
		String servicesUrl = url;
		String result = "";
		BufferedReader in = null;
		HttpsURLConnection connection=null;
		try {
			URL realUrl = new URL(servicesUrl);
			
			//认证信息对象，用于包含访问翻译服务的用户名和密码
			Authenticator auth = new MyAuthenticator(userName, password);
			Authenticator.setDefault(auth);
			
			// 打开和URL之间的连接
			connection = (HttpsURLConnection)realUrl.openConnection();
			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setConnectTimeout(30000); 
			connection.setReadTimeout(30000);
			connection.setRequestProperty("Content-Type","application/vnd.oracle.adf.resourceitem+json");
			connection.connect();
			
			try (OutputStream os = connection.getOutputStream()) {
				os.write(param.getBytes("UTF-8"));
			}
			try{
				InputStream is=null;
				BufferedReader reader=null ;
				if (connection.getResponseCode() >= 400 ) {//连接失败报错
					 is = connection.getErrorStream();
					 if(null==is&&null!=connection){
						 is=connection.getInputStream();
					 }
					 reader = new BufferedReader(
								new InputStreamReader(is));
		            	 String lines;
		 				StringBuffer sbf = new StringBuffer();
		 				while ((lines = reader.readLine()) != null) {
		 					lines = new String(lines.getBytes(), "utf-8");
		 					sbf.append(lines);
	            }
		 				result = sbf.toString();
		 				log.error(result);
		 				throw new Exception("Post提交失败，错误信息:"+result);
				}else{
	            	 reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
	            	 String lines;
	 				StringBuffer sbf = new StringBuffer();
	 				while ((lines = reader.readLine()) != null) {
	 					lines = new String(lines.getBytes(), "utf-8");
	 					sbf.append(lines);
	 				}
	 				result = sbf.toString();
	            }
				connection.disconnect();
				return result;
//				log.info("=====The Result of FusionRestUtil post is=====>"+result);
			}catch (Exception e) {
				connection.disconnect();
				throw Exception("Post提交失败，错误信息:"+e.toString());
			}
		 }catch (Exception e) {
			 throw Exception("Post提交失败，错误信息:"+e.toString());
		}finally {
			try {
				if (in != null) {
					in.close();
				}
				if(connection!=null){
					connection.disconnect();
				}
			} catch (Exception e2) {
				 throw Exception("Post提交失败，错误信息:"+e2.toString());
			}
			
		}
	}
	
	/**
	 * patch实现
	 * @param url
	 * @param param
	 * @param userName
	 * @param password
	 * @return
	 * @throws java.lang.Exception 
	 */
	public String patch(String url,String param,String userName,String password) throws java.lang.Exception {
//		log.info("=====in FusionRestUtil post=====");
//		log.info("=====The Request param of FusionRestUtil post is====="+param);
		//翻译服务的URL
//		String servicesUrl = "https://ekfa-test.fa.us2.oraclecloud.com/crmRestApi/resources/11.13.18.05/activities";
		String servicesUrl = url;
		String result = "";
		BufferedReader in = null;
		HttpsURLConnection connection=null;
		try {
			URL realUrl = new URL(servicesUrl);
			
			//认证信息对象，用于包含访问翻译服务的用户名和密码
			Authenticator auth = new MyAuthenticator(userName, password);
			Authenticator.setDefault(auth);
			
			// 打开和URL之间的连接
			 connection = (HttpsURLConnection)realUrl.openConnection();
			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
			connection.setConnectTimeout(30000); 
			connection.setReadTimeout(30000);
			//connection.setRequestMethod("PATCH");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type","application/vnd.oracle.adf.resourceitem+json");
			connection.connect();
			
			try (OutputStream os = connection.getOutputStream()) {
				os.write(param.getBytes("UTF-8"));
			}
			try{
				InputStream is=null;
				BufferedReader reader=null ;
				if (connection.getResponseCode() >= 400 ) {//连接失败报错
					 is = connection.getErrorStream();
					 result = is.toString();
					 reader = new BufferedReader(
								new InputStreamReader(is));
		            	 String lines;
		 				StringBuffer sbf = new StringBuffer();
		 				while ((lines = reader.readLine()) != null) {
		 					lines = new String(lines.getBytes(), "utf-8");
		 					sbf.append(lines);
	            }
		 				result = sbf.toString();
		 				throw Exception("更新工单失败，错误信息:"+result);
				}else{
	            	 reader = new BufferedReader(
							new InputStreamReader(connection.getInputStream()));
	            	 String lines;
	 				StringBuffer sbf = new StringBuffer();
	 				while ((lines = reader.readLine()) != null) {
	 					lines = new String(lines.getBytes(), "utf-8");
	 					sbf.append(lines);
	 				}
	 				result = sbf.toString();
	            }
				return result;
//				log.info("=====The Result of FusionRestUtil post is=====>"+result);
			}catch (Exception e) {
				throw Exception("更新工单状态失败，错误信息:"+e.toString());
			}
		 }catch (Exception e) {
			 throw Exception("更新工单状态失败，错误信息:"+e.toString());
		}finally {
			try {
				if (in != null) {
					in.close();
				}
				if(connection!=null){
					connection.disconnect();
				}
			} catch (Exception e2) {
				 throw Exception("更新工单状态失败，错误信息:"+e2.toString());
			}
			
		}
	}
	
	private Exception Exception(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * post提交
	 * @param url url地址
	 * @param param 提交数据json串
	 * @return
	 * @throws java.lang.Exception 
	 */
	public String post(String url,String param) throws java.lang.Exception {
		return post(url, param, deaultUserName, deaultPassword);
	}
	
	public String connect(String url,LinkedHashMap param,String method){
		String user = null;
        try {
            HttpClient client = HttpClients.createDefault();
          //需要验证 
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials("112520", "fy123456");
          
            clientsetCredentials(AuthScope.ANY, creds); 
            if("get".equals(method)){
                HttpGet request = new HttpGet(url);
                request.setHeader("Accept", "application/json");
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                ObjectMapper mapper = new ObjectMapper();
//                user = mapper.readValue(entity.getContent(), User.class);
            }else if("post".equals(method)){
                HttpPost request2 = new HttpPost(url);
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
                Iterator iter = param.entrySet().iterator();
                while(iter.hasNext()) {
                    Map.Entry entry = (Map.Entry)iter.next();
                	nvps.add(new BasicNameValuePair((String)entry.getKey(), (String)entry.getValue()));  
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, "GBK");
                request2.setEntity(formEntity);
                HttpResponse response2 = client.execute(request2);
                HttpEntity entity = response2.getEntity();
                ObjectMapper mapper = new ObjectMapper();
//                user = mapper.readValue(entity.getContent(), User.class);
            }else if("delete".equals(method)){

            }else if("put".equals(method)){

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    } 
	
	private void clientsetCredentials(AuthScope any,
			UsernamePasswordCredentials creds) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		String query = "{                                                                                                          \r\n" + 
				"   \"ActivityFunctionCode\" : \"APPOINTMENT\",\r\n" + 
				"    \"ActivityStartDate\" : \"2014-05-26T13:00:00+07:00\",\r\n" + 
				"    \"ActivityEndDate\" : \"2014-05-26T14:00:00+07:00\",\r\n" + 
				"    \"Subject\" : \"ATAP_REST_TASK_TEST004_Appt1\"\r\n" + 
				"}";
//		FusionRestUtil.post("https://ekfa-test.fa.us2.oraclecloud.com/crmRestApi/resources/11.13.18.05/activities",query);
	}

}
