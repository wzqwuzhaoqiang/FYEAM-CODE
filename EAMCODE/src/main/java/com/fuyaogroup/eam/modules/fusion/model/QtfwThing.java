package com.fuyaogroup.eam.modules.fusion.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("WINDOW_SERVER_THING")

/**
 * 前台物品
 * @author gnb781
 *
 */
public class QtfwThing extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	//物品序列号
	private String serial;
	//物品名称
	private String name;
	//管理部门
	private String departName;
	//状态
	private String status;
}
