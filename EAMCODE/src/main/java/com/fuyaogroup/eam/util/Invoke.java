package com.fuyaogroup.eam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fuyaogroup.eam.controller.AddAssetController;
import com.fuyaogroup.eam.controller.EamFusionController;
import com.fuyaogroup.eam.controller.TransferAssetController;
import com.soa.eis.adapter.framework.message.IMsgObject;

@Component
@Slf4j
//@PropertySource(value = { "classpath:config/classMethods.xml"})
public class Invoke {
	private static HashMap<String, Object> hm_methods = new HashMap<String, Object>();
	private static HashMap<String, Object> hm_controllers = new HashMap<String, Object>();
	private final static Lock METHODSLOCK = new ReentrantLock();
	private final static Class<?> IMSGOBJECT_CLASS = IMsgObject.class;
	
	@Autowired
	private AddAssetController addAssetController;

	@Autowired
	private TransferAssetController transferAssetController;
	
	@Autowired
	private EamFusionController workorderController;
 
	/**
	 * 根据配置文件反射处理方法
	 * @param serviceID 系统服务号
	 * @param inData 传入数据
	 * @return
	 */
	public Object invokeMethod(String serviceID, IMsgObject reqMsg) {
		System.out.print("ESB开始，serviceID:"+serviceID);
		Object obj = null;
		getClassMethodsMap();
		getControllersMap();
		if (serviceID != null && !"".equals(serviceID) && !hm_methods.isEmpty()) {
			if (hm_methods.containsKey(serviceID)) {
				String[] sstr = hm_methods.get(serviceID).toString().split(",");
				if (sstr.length == 2) {
					String clsName = sstr[0];
					String methodName = sstr[1];
					if (hm_controllers.containsKey(clsName)) {
						try {
							Object object = hm_controllers.get(clsName);
							Class<?>[] types = new Class[] { IMSGOBJECT_CLASS };
							Object[] objs = new Object[] { reqMsg };
							Class<?> cls = Class.forName(clsName);
							Method method = cls.getMethod(methodName, types);
							if (method != null) {
								obj = method.invoke(object, objs);
							} else {
								throw new Exception("not found " + clsName
										+ "." + methodName);
							}
						} catch (Exception e) {
							
							e.printStackTrace();
						}
					}
				}
			}
		}
		return obj;
	}

	private void getControllersMap() {
		if (!hm_methods.isEmpty() && hm_controllers.isEmpty()) {
			Field[] fields = this.getClass().getDeclaredFields();
			for (Field field : fields) {
				if (field.isAnnotationPresent(Autowired.class)) {
					Class fidldClass = field.getType();// 获得此属性的类
					try {
						hm_controllers.put(
								fidldClass.toString().replace("class ", ""),
								field.get(this));
					} catch (Exception e) {
						log.error(e.toString());
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 获取classMethods.xml配置
	 */
	private void getClassMethodsMap() {
		if (hm_methods.isEmpty() == true) {
			METHODSLOCK.lock();
			try {
				// 读取classMethods.config
//				File rootPath = new File( this.getClass().getResource( "/" ).toURI().getPath());
//				log.info("config.path:"+rootPath);
				// Windows,Linux,Unix采用/方式,\\方式只是Windows使用
//				if (rootPath!=null&&!rootPath.exists()) {
//				System.out.print( "classMethods.xml配置文件地址： " + rootPath+"/config/classMethods.xml");
//					getConfigByXML( rootPath+"/config/classMethods.xml",
//							hm_methods);
//				} else {
					ClassPathResource resource = new ClassPathResource("/config/classMethods.xml");
					InputStream inputStream = resource.getInputStream();
					inputStream=Invoke.class.getClassLoader().getResourceAsStream("config/classMethods.xml");
					log.info( "classMethods.xml配置文件地址： " +inputStream.toString());
					getConfigByInput(inputStream,
							hm_methods);
//				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  finally {
				METHODSLOCK.unlock();
			}
		}
	}

	/**
	 * 获取解析classMethods.config xml数据
	 * 
	 * @param filepath
	 * @param hm
	 */
	private void getConfigByXML(String filepath, HashMap<String, Object> hm) {
		SAXBuilder sb = null;
		Document doc = null; // XML文件的整个文档
		Element root = null; // XML文件中的根节点 items
		FileInputStream filestream = null;
		try {
			sb = new SAXBuilder();
			java.io.File file = new File(filepath);
			if (file.exists()) {
				filestream = new FileInputStream(filepath);
				doc = sb.build(filestream);
				root = doc.getRootElement();
				getConfigToHashMap(root, hm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (filestream != null) {
					filestream.close();
				}
			} catch (Exception exp) {
				log.error(exp.toString());
			}
		}
	}
	
	/**
	 * 获取解析classMethods.config xml数据
	 * 
	 * @param filepath
	 * @param hm
	 */
	private void getConfigByInput(InputStream inputStream, HashMap<String, Object> hm) {
		SAXBuilder sb = null;
		Document doc = null; // XML文件的整个文档
		Element root = null; // XML文件中的根节点 items
		FileInputStream filestream = null;
		try {
			sb = new SAXBuilder();
				doc = sb.build(inputStream);
				root = doc.getRootElement();
				getConfigToHashMap(root, hm);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (filestream != null) {
					filestream.close();
				}
			} catch (Exception exp) {
				log.error(exp.toString());
			}
		}
	}


	/**
	 * 根据根节点取得classMethods.config中配置数据存储到hashmap中
	 * 
	 * @param root
	 * @param hm
	 */
	private void getConfigToHashMap(Element root, HashMap<String, Object> hm) {
		List<?> list = root.getChildren();
		int len = list.size();
		for (int i = 0; i < len; i++) {
			Element elm = (Element) list.get(i);
			String sid = elm.getAttributeValue("id");
			String sclass = elm.getAttributeValue("class");
			String smethod = elm.getAttributeValue("method");
			if (!sid.isEmpty() && !hm.containsKey(sid)) {
				String tvalue = sclass + "," + smethod;
				hm.put(sid, tvalue);
			}
		}
	}

}