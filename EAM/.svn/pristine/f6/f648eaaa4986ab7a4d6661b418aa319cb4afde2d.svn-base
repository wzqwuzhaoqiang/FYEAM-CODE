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
package com.fuyaogroup.eam.common.service;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuyaogroup.eam.common.model.BaseEntity;
import com.fuyaogroup.eam.util.ReflexObjectUtil;


/**   
 * 
 * @author CJ ♦ Wang
 * @version 1.0
 *  
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T>  extends ServiceImpl<M,T> {
	
		@Transactional(rollbackFor = Exception.class)
	    @Override
	    public boolean updateById(T entity) {
			if(entity instanceof BaseEntity){
				BaseEntity baseEntity = (BaseEntity)entity;
				if(baseEntity.getVersion() == null) {
					T tobj = this.getById(baseEntity.getId());
					ReflexObjectUtil.getKeyAndValue(entity);
					Object versionObj = ReflexObjectUtil.getFieldValue(tobj, "version");
					if(null != versionObj) {
						ReflexObjectUtil.setFieldValue(entity, "version", versionObj);
					}
				}
			}
	        return retBool(baseMapper.updateById(entity));
	    }

}
