package com.fuyaogroup.eam.util;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;

/**
 * 目前支持8大基本类型、Date、Set、List、自定义对象、BigDecimal
 * 由于GroupRecord不支持重复设置字段值(key-value)
 * 所以目前集合类型仅支持自定义对象的集合(setSubGroups)
 * @author chenjf
 * @date 2016年11月07日
 */
public class GroupRecordTool {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * 设置顶层请求参数
	 * @param reqMO 报文对象
	 * @param paramObj 请求参数对象
	 * @throws Exception
	 */
	public static void setParamToMsgObject(IMsgObject reqMO, Object paramObj)
			throws Exception {
		Field[] fields = paramObj.getClass().getDeclaredFields();
		List<GroupRecord> groups = new ArrayList<GroupRecord>();
		for (Field field : fields) {
			field.setAccessible(true); // 设置属性是可以访问的
			Object fieldValue = field.get(paramObj);// 得到此属性的值
			String fieldName = field.getName();// 获得此属性的名称
			// Type type = field.getGenericType();//获得此属性的类型
			// Class fidldClass = field.getType();//获得此属性的类
//			update by linlipeng date 2017/3/20
			if (fieldValue instanceof Character || fieldValue instanceof Byte
					|| fieldValue instanceof Boolean
					|| fieldValue instanceof Short
					|| fieldValue instanceof Integer
					|| fieldValue instanceof Float
					|| fieldValue instanceof Long
					|| fieldValue instanceof Double
					|| fieldValue instanceof String
					|| fieldValue instanceof BigDecimal) {
				reqMO.setReqValue(fieldName, fieldValue + "");
			} else if (fieldValue instanceof Date && fieldValue != null) {// String
				reqMO.setReqValue(fieldName,
						dateFormat.format((Date) fieldValue));
			} else if (fieldValue instanceof List && fieldValue != null) {// List集合
				//暂不处理
			} else if (fieldValue instanceof Set && fieldValue != null) {// Set集合
				//暂不处理
			}else if (fieldValue != null) {// 自定义对象
				groups.add(convertToGroupRecord(fieldValue));
			}
		}
		if(groups.size()>0){
			reqMO.setReqGroupRecord(groups);
		}
	}

	/**
	 * 传入
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static GroupRecord convertToGroupRecord(Object object)
			throws Exception {
		GroupRecord groupRecord = new GroupRecord();// 获得类的简称
		String objectName = object.getClass().getSimpleName();
		groupRecord.setName(objectName);// 自定义名称或者类型
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true); // 设置属性是可以访问的
			Object fieldValue = field.get(object);// 得到此属性的值
			String fieldName = field.getName();// 获得此属性的名称
			// Type type = field.getGenericType();//获得此属性的类型
			// Class fidldClass = field.getType();//获得此属性的类
//			update by linlipeng ;date 2017/3/20
			if (fieldValue instanceof Character || fieldValue instanceof Byte
					|| fieldValue instanceof Boolean
					|| fieldValue instanceof Short
					|| fieldValue instanceof Integer
					|| fieldValue instanceof Float
					|| fieldValue instanceof Long
					|| fieldValue instanceof Double
					|| fieldValue instanceof String
					|| fieldValue instanceof BigDecimal) {
				groupRecord.setFieldValue(fieldName, fieldValue + "");
			} else if (fieldValue instanceof Date && fieldValue != null) {// String
				groupRecord.setFieldValue(fieldName,
						dateFormat.format((Date) fieldValue));
			} else if (fieldValue instanceof List && fieldValue != null) {// List集合
			// String subFieldName = null;
			// if (field.isAnnotationPresent(GroupRecordAnnotation.class)) {
			// GroupRecordAnnotation annotation = field
			// .getAnnotation(GroupRecordAnnotation.class);
			// subFieldName = annotation.subFieldName();
			// }
				List<GroupRecord> groups = new ArrayList<GroupRecord>();
				groups.add(convertToGroupRecords(fieldName, (List) fieldValue,
						null));
				groupRecord.setSubGroups(fieldName, groups);
			} else if (fieldValue instanceof Set && fieldValue != null) {// Set集合
			// GroupRecordAnnotation annotation = field
			// .getAnnotation(GroupRecordAnnotation.class);
			// String subFieldName = null;
			// if (annotation != null) {
			// subFieldName = annotation.subFieldName();
			// }
				List<GroupRecord> groups = new ArrayList<GroupRecord>();
				groups.add(convertToGroupRecords(fieldName, (Set) fieldValue,
						null));
				groupRecord.setSubGroups(fieldName, groups);
			} else if (fieldValue != null) {// 自定义对象
				List<GroupRecord> groups = new ArrayList<GroupRecord>();
				groups.add(convertToGroupRecord(fieldValue));
				groupRecord.setSubGroups(fieldName, groups);// 这里fieldName似乎不起作用
			}
		}
		return groupRecord;
	}

	public static GroupRecord convertToGroupRecords(String groupName,
			List<Object> objects, String subFieldName) throws Exception {
		GroupRecord groupRecord = new GroupRecord();
		groupRecord.setName(isEmpty(subFieldName) ? groupName : subFieldName);

		if (objects.size() > 0) {
			Object firstData = objects.get(0);
			Class entityClass = firstData.getClass();
//			update by linlipeng ;date 2017/3/20
			if (entityClass == Character.class || entityClass == Byte.class
					|| entityClass == Boolean.class
					|| entityClass == Short.class
					|| entityClass == Integer.class
					|| entityClass == Float.class || entityClass == Long.class
					|| entityClass == Double.class
					|| entityClass == String.class
					|| entityClass == BigDecimal.class) {
				/*
				 * if (!isEmpty(subFieldName)) { List<GroupRecord> groups = new
				 * ArrayList<GroupRecord>(); for (Object object : objects) {
				 * GroupRecord subRecord = new GroupRecord();
				 * subRecord.setFieldValue(subFieldName, object + "");
				 * groups.add(subRecord); } groupRecord.setSubGroups("sub_" +
				 * groupName, groups); }
				 */
			} else if (entityClass == Date.class) {
				/*
				 * if (!isEmpty(subFieldName)) { for (Object object : objects) {
				 * Date date = (Date) object;
				 * groupRecord.setFieldValue(subFieldName,
				 * dateFormat.format(date)); } }
				 */
			} else {
				List<GroupRecord> groups = new ArrayList<GroupRecord>();
				for (Object object : objects) {
					groups.add(convertToGroupRecord(object));
				}
				groupRecord.setSubGroups(groupName, groups);
			}
		}
		return groupRecord;
	}

	public static GroupRecord convertToGroupRecords(String groupName,
			Set<Object> objects, String subFieldName) throws Exception {
		GroupRecord groupRecord = new GroupRecord();
		groupRecord.setName(groupName);
		Object[] objects2 = objects.toArray();
		if (objects2.length > 0) {
			Object firstData = objects2[0];
			Class entityClass = firstData.getClass();
//			update by linlipeng ;date 2017/3/20
			if (entityClass == Character.class || entityClass == Byte.class
					|| entityClass == Boolean.class
					|| entityClass == Short.class
					|| entityClass == Integer.class
					|| entityClass == Float.class || entityClass == Long.class
					|| entityClass == Double.class
					|| entityClass == String.class
					|| entityClass == BigDecimal.class) {
				/*
				 * if (!isEmpty(subFieldName)) { List<GroupRecord> groups = new
				 * ArrayList<GroupRecord>(); for (Object object : objects) {
				 * GroupRecord subRecord = new GroupRecord();
				 * subRecord.setFieldValue(subFieldName, object + "");
				 * groups.add(subRecord); } groupRecord.setSubGroups("sub_" +
				 * groupName, groups); }
				 */
			} else if (entityClass == Date.class) {
				/*
				 * if (!isEmpty(subFieldName)) { for (Object object : objects) {
				 * Date date = (Date) object;
				 * groupRecord.setFieldValue(subFieldName,
				 * dateFormat.format(date)); } }
				 */
			} else {
				List<GroupRecord> groups = new ArrayList<GroupRecord>();
				for (Object object : objects) {
					groups.add(convertToGroupRecord(object));
				}
				groupRecord.setSubGroups(groupName, groups);
			}
		}
		return groupRecord;
	}

	private static boolean isEmpty(String s) {
		if (s == null || "".equals(s.trim()))
			return true;
		return false;
	}

}