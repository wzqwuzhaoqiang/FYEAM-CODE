/**
 *
 * Copyright (c) 2017-2018 福耀玻璃工业集团股份有限公司
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 反射处理Bean，得到里面的属性值
 * 
 * @author CJ ♦ Wang
 * @version 1.0
 * 
 */
@Slf4j
public class ReflexObjectUtil {

	 /**
     * 单个对象的所有键值
     * 
     * @param object
     *            单个对象
     * 
     * @return Map<String, Object> map 所有 String键 Object值 ex：{pjzyfy=0.00,
     *         xh=01, zzyl=0.00, mc=住院患者压疮发生率, pjypfy=0.00, rs=0, pjzyts=0.00,
     *         czydm=0037, lx=921, zssl=0.00}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> getKeyAndValue(Object obj) {
    	if(obj instanceof Map) {
    		return (Map<String, Object>)obj;
    	}
        Map<String, Object> map = new HashMap<String, Object>();
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            Object val = new Object();
            try {
                val = f.get(obj);
                // 得到此属性的值
                map.put(f.getName(), val);// 设置键值
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            /*
             * String type = f.getType().toString();//得到此属性的类型 if
             * (type.endsWith("String")) {
             * log.info(f.getType()+"\t是String"); f.set(obj,"12") ;
             * //给属性设值 }else if(type.endsWith("int") ||
             * type.endsWith("Integer")){
             * log.info(f.getType()+"\t是int"); f.set(obj,12) ; //给属性设值
             * }else{ log.info(f.getType()+"\t"); }
             */

        }
        log.info("单个对象的所有键值==反射==" + map.toString());
        return map;
    }

    /**
     * 单个对象的某个键的值
     * 
     * @param object
     *            对象
     * 
     * @param key
     *            键
     * 
     * @return Object 键在对象中所对应得值 没有查到时返回空字符串
     */
    @SuppressWarnings("rawtypes")
	public static Object getValueByKey(Object obj, String key) {
        // 得到类对象
        Class userCla = (Class) obj.getClass();
        /* 得到类中的所有属性集合 */
        Field[] fs = userCla.getDeclaredFields();
        for (int i = 0; i < fs.length; i++) {
            Field f = fs[i];
            f.setAccessible(true); // 设置些属性是可以访问的
            try {
            
                if (f.getName().endsWith(key)) {
                    log.info("单个对象的某个键的值==反射==" + f.get(obj));
                    return f.get(obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        // 没有查到时返回空字符串
        return "";
    }

    /**
     * 多个（列表）对象的所有键值
     * 
     * @param object
     * @return List<Map<String,Object>> 列表中所有对象的所有键值 ex:[{pjzyfy=0.00, xh=01,
     *         zzyl=0.00, mc=住院患者压疮发生率, pjypfy=0.00, rs=0, pjzyts=0.00,
     *         czydm=0037, lx=921, zssl=0.00}, {pjzyfy=0.00, xh=02, zzyl=0.00,
     *         mc=新生儿产伤发生率, pjypfy=0.00, rs=0, pjzyts=0.00, czydm=0037, lx=13,
     *         zssl=0.00}, {pjzyfy=0.00, xh=03, zzyl=0.00, mc=阴道分娩产妇产伤发生率,
     *         pjypfy=0.00, rs=0, pjzyts=0.00, czydm=0037, lx=0, zssl=0.00},
     *         {pjzyfy=0.00, xh=04, zzyl=0.75, mc=输血反应发生率, pjypfy=0.00, rs=0,
     *         pjzyts=0.00, czydm=0037, lx=0, zssl=0.00}, {pjzyfy=5186.12,
     *         xh=05, zzyl=0.00, mc=剖宫产率, pjypfy=1611.05, rs=13, pjzyts=7.15,
     *         czydm=0037, lx=13, zssl=0.00}]
     */
    @SuppressWarnings("rawtypes")
	public static List<Map<String, Object>> getKeysAndValues(List<Object> object) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Object obj : object) {
            Class userCla;
            // 得到类对象
            userCla = (Class) obj.getClass();
            /* 得到类中的所有属性集合 */
            Field[] fs = userCla.getDeclaredFields();
            Map<String, Object> listChild = new HashMap<String, Object>();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                Object val = new Object();
                try {
                    val = f.get(obj);
                    // 得到此属性的值
                    listChild.put(f.getName(), val);// 设置键值
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            list.add(listChild);// 将map加入到list集合中
        }
        log.info("多个（列表）对象的所有键值====" + list.toString());
        return list;
    }

    /**
     * 多个（列表）对象的某个键的值
     * 
     * @param object
     * @param key
     * @return List<Object> 键在列表中对应的所有值 ex:key为上面方法中的mc字段 那么返回的数据就是： [住院患者压疮发生率,
     *         新生儿产伤发生率, 阴道分娩产妇产伤发生率, 输血反应发生率, 剖宫产率]
     */
    @SuppressWarnings("rawtypes")
	public static List<Object> getValuesByKey(List<Object> object, String key) {
        List<Object> list = new ArrayList<Object>();
        for (Object obj : object) {
            // 得到类对象
            Class userCla = (Class) obj.getClass();
            /* 得到类中的所有属性集合 */
            Field[] fs = userCla.getDeclaredFields();
            for (int i = 0; i < fs.length; i++) {
                Field f = fs[i];
                f.setAccessible(true); // 设置些属性是可以访问的
                try {
                    if (f.getName().endsWith(key)) {
                        list.add(f.get(obj));
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("多个（列表）对象的某个键的值列表====" + list.toString());
        return list;
    }
    
    
    /**
	 * 获取obj对象fieldName的Field
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field getFieldByFieldName(Object obj, String fieldName) {
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
			}
		}
		return null;
	}

	/**
	 * 获取obj对象fieldName的属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static Object getValueByFieldName(Object obj, String fieldName)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = getFieldByFieldName(obj, fieldName);
		Object value = null;
		if (field != null) {
			if (field.isAccessible()) {
				value = field.get(obj);
			} else {
				field.setAccessible(true);
				value = field.get(obj);
				field.setAccessible(false);
			}
		}
		return value;
	}

	/**
	 * 设置obj对象fieldName的属性值
	 * 
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static void setValueByFieldName(Object obj, String fieldName, Object value)
			throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		Field field = obj.getClass().getDeclaredField(fieldName);
		if (field.isAccessible()) {
			field.set(obj, value);
		} else {
			field.setAccessible(true);
			field.set(obj, value);
			field.setAccessible(false);
		}
	}
	
	
	/** 
     * 循环向上转型, 获取对象的 DeclaredMethod 
     * @param object : 子类对象 
     * @param methodName : 父类中的方法名 
     * @param parameterTypes : 父类中的方法参数类型 
     * @return 父类中的方法对象 
     */  
      
    public static Method getDeclaredMethod(Object object, String methodName, Class<?> ... parameterTypes){  
        Method method = null ;  
          
        for(Class<?> clazz = object.getClass() ; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                method = clazz.getDeclaredMethod(methodName, parameterTypes) ;  
                return method ;  
            } catch (Exception e) {  
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  
              
            }  
        }  
          
        return null;  
    }  
      
    /** 
     * 直接调用对象方法, 而忽略修饰符(private, protected, default) 
     * @param object : 子类对象 
     * @param methodName : 父类中的方法名 
     * @param parameterTypes : 父类中的方法参数类型 
     * @param parameters : 父类中的方法参数 
     * @return 父类中方法的执行结果 
     */  
      
    public static Object invokeMethod(Object object, String methodName, Class<?> [] parameterTypes,  
            Object [] parameters) {  
        //根据 对象、方法名和对应的方法参数 通过反射 调用上面的方法获取 Method 对象  
        Method method = getDeclaredMethod(object, methodName, parameterTypes) ;  
          
        //抑制Java对方法进行检查,主要是针对私有方法而言  
        method.setAccessible(true) ;  
          
            try {  
                if(null != method) {  
                      
                    //调用object 的 method 所代表的方法，其方法的参数是 parameters  
                    return method.invoke(object, parameters) ;  
                }  
            } catch (IllegalArgumentException e) {  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                e.printStackTrace();  
            } catch (InvocationTargetException e) {  
                e.printStackTrace();  
            }  
          
        return null;  
    }  
  
    /** 
     * 循环向上转型, 获取对象的 DeclaredField 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * @return 父类中的属性对象 
     */  
      
    public static Field getDeclaredField(Object object, String fieldName){  
        Field field = null ;  
          
        Class<?> clazz = object.getClass() ;  
          
        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                field = clazz.getDeclaredField(fieldName) ;  
                return field ;  
            } catch (Exception e) {  
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。  
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了  
                  
            }   
        }  
      
        return null;  
    }     
      
    /** 
     * 直接设置对象属性值, 忽略 private/protected 修饰符, 也不经过 setter 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * @param value : 将要设置的值 
     */  
      
    public static void setFieldValue(Object object, String fieldName, Object value){  
      
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象  
        Field field = getDeclaredField(object, fieldName) ;  
          
        //抑制Java对其的检查  
        field.setAccessible(true) ;  
          
        try {  
            //将 object 中 field 所代表的值 设置为 value  
             field.set(object, value) ;  
        } catch (IllegalArgumentException e) {  
            e.printStackTrace();  
        } catch (IllegalAccessException e) {  
            e.printStackTrace();  
        }  
          
    }  
      
    /** 
     * 直接读取对象的属性值, 忽略 private/protected 修饰符, 也不经过 getter 
     * @param object : 子类对象 
     * @param fieldName : 父类中的属性名 
     * @return : 父类中的属性值 
     */  
      
    public static Object getFieldValue(Object object, String fieldName){  
          
        //根据 对象和属性名通过反射 调用上面的方法获取 Field对象  
        Field field = getDeclaredField(object, fieldName) ;  
          
        //抑制Java对其的检查  
        field.setAccessible(true) ;  
          
        try {  
            //获取 object 中 field 所代表的属性值  
            return field.get(object) ;  
              
        } catch(Exception e) {  
            e.printStackTrace() ;  
        }  
          
        return null;  
    }  

}
