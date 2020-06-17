package com.fuyaogroup.eam.common.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Slf4j
@Service
public class qtfwWeixinMessageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public boolean send(String toUser,String toTag , String content) {

        String accessToken = qtfwWeixinService.accessToken;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost hp = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken);
        HashMap<String, Object> wxMessage = new HashMap<>();
        HashMap<String, Object> wxTextMessage = new HashMap<>();
        wxMessage.put("touser", toUser);
        wxMessage.put("toparty", "");
        wxMessage.put("totag", toTag);
        wxMessage.put("agentid", "1000043");
        wxMessage.put("msgtype", "text");//TODO 
        wxTextMessage.put("content", content);
        wxMessage.put("text", wxTextMessage);

        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(wxMessage);
            json = json.replace("\"[", "[");
            json = json.replace("]\"", "]");
            //logger.info(json);
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            se.setContentEncoding("utf-8");
            se.setContentType("application/json");
            hp.setEntity(se);
            ResponseHandler<String> rh = new BasicResponseHandler();
            String responseBody = httpClient.execute(hp, rh);
            om = new ObjectMapper();
            HashMap<String, String> returnMessage = new HashMap<>();
            returnMessage = om.readValue(responseBody, new TypeReference<HashMap<String, String>>() {
            });
//            logger.info(returnMessage.get("errcode"));
            if (returnMessage.get("errmsg").equals("ok")) {
                logger.info("发送消息给用户"+ toUser+"成功！" );
                return true;
            } else {
                logger.error(returnMessage.get("errmsg"));
                return false;
            }

        } catch (JsonProcessingException e) {
            logger.error(e.getOriginalMessage());
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return false;

    }

    public boolean sendImg(String toUser,String toTag , String path) {

        String accessToken = qtfwWeixinService.accessToken;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost hp = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken);
        HashMap<String, Object> wxMessage = new HashMap<>();
        HashMap<String, Object> wxTextMessage = new HashMap<>();
        String image;
		try {
			image = this.get_image_url(accessToken, path);
		} catch (Exception e1) {
		return false;
		}
        wxMessage.put("touser", toUser);
        wxMessage.put("toparty", "");
        wxMessage.put("totag", toTag);
        wxMessage.put("agentid", "1000043");
        wxTextMessage.put("media_id", image);
        wxMessage.put("msgtype", "image");//TODO 
        wxMessage.put("image", wxTextMessage);

        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(wxMessage);
            json = json.replace("\"[", "[");
            json = json.replace("]\"", "]");
            //logger.info(json);
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            se.setContentEncoding("utf-8");
            se.setContentType("application/json");
            hp.setEntity(se);
            ResponseHandler<String> rh = new BasicResponseHandler();
            String responseBody = httpClient.execute(hp, rh);
            om = new ObjectMapper();
            HashMap<String, String> returnMessage = new HashMap<>();
            returnMessage = om.readValue(responseBody, new TypeReference<HashMap<String, String>>() {
            });
//            logger.info(returnMessage.get("errcode"));
            if (returnMessage.get("errmsg").equals("ok")) {
                logger.info("发送消息给用户"+ toUser+"成功！" );
                return true;
            } else {
                logger.error(returnMessage.get("errmsg"));
                return false;
            }

        } catch (JsonProcessingException e) {
            logger.error(e.getOriginalMessage());
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return false;

    }
    private String get_image_url(String accessToken, String path) throws Exception {
    	CloseableHttpClient httpClient = HttpClients.createDefault();
   	 HttpPost hp = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token="
   	 		+accessToken+"&type=image"   );
   	HashMap<String,Object> hps =new HashMap<String,Object>() ;
    try {
    	MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    	 
    	builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    	//@2017-06-28 在文件上传中，有些系统不支持指定字符集(企业微信)
    	//builder.setCharset(Charset.forName(FsSpec.Charset_Default) );
    	 
    	//先添加文件部分(无需指定编码)
    	File f = new File(path);
    	if (f != null && f.exists()) {
    		builder.addBinaryBody(f.getName(), f, ContentType.DEFAULT_BINARY, f.getName() );
    	}
    	 
//    	//再添加表单部分(需指定编码，@2017-06-28 key和value都需要指定编码)
//    	if (params != null && !params.isEmpty()) {
//    		
//    		//@2017-06-28 在文件上传中，有些系统不支持指定字符集(企业微信)
//    		builder.setCharset(Charset.forName(FsSpec.Charset_Default) );
//    		ContentType contentType = ContentType.create("text/plain", FsSpec.Charset_Default);
//    		
//    		for (Map.Entry<String, String> entry : params.entrySet()) {
//    			
//    			builder.addTextBody(entry.getKey(), entry.getValue(), contentType);
//    		}
//    	}
    	 
    		hp.setEntity(builder.build() );
//    		   hps.put("media", ImageUtil.getImageBinary(path));
    		   ObjectMapper om = new ObjectMapper();
//              String json = om.writeValueAsString(hps);
//              json = json.replace("\"[", "[");
//              json = json.replace("]\"", "]");
//              //logger.info(json);
//              StringEntity se = new StringEntity(json,"utf-8");
//              se.setContentEncoding("utf-8");
//              se.setContentType("application/json");
//              hp.setEntity(se);
              ResponseHandler<String> rh = new BasicResponseHandler();
              String responseBody = httpClient.execute(hp, rh);
              om = new ObjectMapper();
              HashMap<String, String> returnMessage = new HashMap<>();
              returnMessage = om.readValue(responseBody, new TypeReference<HashMap<String, String>>() {
              });
//              logger.info(returnMessage.get("errcode"));
              if (returnMessage.get("errmsg").equals("ok")) {
                  logger.info("上传图片成功！" );
                  return returnMessage.get("media_id");
              } else {
                  logger.error(returnMessage.get("errmsg"));
                 throw new Exception("上传图片失败:"+returnMessage.get("errmsg"));
              }

          } catch (JsonProcessingException e) {
              logger.error(e.getOriginalMessage());
              throw new Exception("上传图片失败："+e.getOriginalMessage());
          } catch (ClientProtocolException e) {
              logger.error(e.getMessage());
              throw new Exception("上传图片失败："+e.getMessage());
          } catch (IOException e) {
              logger.error(e.getMessage());
              throw new Exception("上传图片失败："+e.getMessage());

          }

    		
	}

	public boolean sendToAdmin(String toUser,String toTag , String content) {

        String accessToken = qtfwWeixinService.accessToken;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost hp = new HttpPost("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken);
        HashMap<String, Object> wxMessage = new HashMap<>();
        HashMap<String, Object> wxTextMessage = new HashMap<>();
        wxMessage.put("touser", toUser);
        wxMessage.put("toparty", "");
        wxMessage.put("totag", toTag);
        wxMessage.put("agentid", "1000017");
        wxMessage.put("msgtype", "text");
        wxTextMessage.put("content", content);
        wxMessage.put("text", wxTextMessage);

        ObjectMapper om = new ObjectMapper();
        try {
            String json = om.writeValueAsString(wxMessage);
            json = json.replace("\"[", "[");
            json = json.replace("]\"", "]");
            //logger.info(json);
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            se.setContentEncoding("utf-8");
            se.setContentType("application/json");
            hp.setEntity(se);
            ResponseHandler<String> rh = new BasicResponseHandler();
            String responseBody = httpClient.execute(hp, rh);
            om = new ObjectMapper();
            HashMap<String, String> returnMessage = new HashMap<>();
            returnMessage = om.readValue(responseBody, new TypeReference<HashMap<String, String>>() {
            });
//            logger.info(returnMessage.get("errcode"));
            if (returnMessage.get("errmsg").equals("ok")) {
                logger.info("发送消息给用户"+ toUser+"成功！" );
                return true;
            } else {
                logger.error(returnMessage.get("errmsg"));
                return false;
            }

        } catch (JsonProcessingException e) {
            logger.error(e.getOriginalMessage());
        } catch (ClientProtocolException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return false;

    }
	
	public static void main(String[] args) {
		  try {
		   qtfwWeixinMessageService asd=new qtfwWeixinMessageService();
		   WeixinService wx=new WeixinService();
		   wx.getAccessToken();
		   asd.send("112520","","asdfasdfasdfasdf");
		  } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		 }

}
