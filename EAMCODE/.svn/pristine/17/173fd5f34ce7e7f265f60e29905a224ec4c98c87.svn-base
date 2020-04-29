package com.fuyaogroup.eam.modules.fusion.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("config")
public class Config extends BaseEntity{
	private Long configId   ;//参数ID
	private String configCode ;//参数编码
	private Integer config_type ;//参数类型 
	private String configName ;//参数名称
	private String configVal ;//参数值
	private String comments ;//字段解释
}
