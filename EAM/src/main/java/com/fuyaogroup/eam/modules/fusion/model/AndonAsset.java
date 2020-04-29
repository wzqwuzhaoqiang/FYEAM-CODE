package com.fuyaogroup.eam.modules.fusion.model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;


/**
 * 创建按灯设备编码对应表
 * @author liangqing.chi
 *
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("ANDON_ASSET")
public class AndonAsset{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	  /**
	   * 按灯编码
	   */
	  private String andon_code;
	  
	  /**
	   * 设备名称
	   */
	  private String asset_num;
	  
	  /**
	   * 1:在用；2：废弃
	   */
	  private String is_use;
	 
}
