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
package com.fuyaogroup.eam.common.model;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;

import lombok.Data;
import lombok.experimental.Accessors;

/**   
 * 
 * @author CJ ♦ Wang
 * @version 1.0
 *  
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable{

	private static final long serialVersionUID = -1069846309214393303L;

	
	  @TableId(value = "id", type = IdType.AUTO)
	  private Long id;
	
	  @Version  
	  @TableField("version")
	  private Integer version;

	    /**
	     * 创建人
	     */
	  @TableField(value="created_by",fill=FieldFill.INSERT)
	  private Long createdBy;

	    /**
	     * 创建时间
	     */
	  @TableField(value="creation_date",fill=FieldFill.INSERT)
	  @JSONField(format="yyyy-MM-dd HH:mm:ss")
	    private Date creationDate;

	    /**
	     * 最后更新人
	     */
	  @TableField(value="last_updated_by")
	  private Long lastUpdatedBy;

	    /**
	     * 最后更新时间
	     */
	    @TableField(value="last_update_date")
//	    @TableField(exist = false)
	    @JSONField(format="yyyy-MM-dd HH:mm:ss")
	    private Date lastUpdateDate;
	    
}
