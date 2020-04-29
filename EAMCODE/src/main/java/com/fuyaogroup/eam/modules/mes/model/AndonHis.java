package com.fuyaogroup.eam.modules.mes.model;
import java.util.Date;

import javax.persistence.Column;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 创建按灯模块视图表
 * @author liangqing.chi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("HMCS.HCM_ANDON_HIS_FOR_EAM_V")
public class AndonHis extends BaseEntity{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	  
	  private Long SERVER_ID;
	  /**
	   * 服务名称
	   */
	  @Column(name="SERVER_NAME")
	  private String server_name;
	  /**
	   * 服务名称
	   */
	  @Column(name="EVENT_ID")
	  private Double event_id;
	  /**
	   * 工厂编号
	   */
	  @Column(name="PLANT_CODE")
	  private String plant_code;
	  /**
	   * 工厂描述
	   */
	  @Column(name="PLANT_DESC")
	  private String plant_desc;
	  /**
	   * 按灯编码
	   */
	  @Column(name="ANDON_CODE")
	  private String andon_code;
	  /**
	   * 按灯描述
	   */
	  @Column(name="ANDON_DESC")
	  private String andon_desc;
	  /**
	   * 生产线编码
	   */
	  @Column(name="PROD_LINE_CODE")
	  private String prod_line_code;
	  /**
	   * 产线描述
	   */
	  @Column(name="LINE_DESC")
	  private String line_desc;
	  /**
	   * 班组
	   */
	  @Column(name="WORKCELL_CODE")
	  private String workcell_code;
	  /**
	   * 班组名称
	   */
	  @Column(name="WKC_DESC")
	  private String wkc_desc;
	  /**
	   * 事件类型编码
	   */
	  @Column(name="BTYPE_CODE")
	  private String btype_code;
	  /**
	   * 事件类型编码
	   */
	  @Column(name="BTYPE_DESC")
	  private String btype_desc;
	  /**
	   * 班次
	   */
	  @Column(name="PROD_CALENDAR_DAY")
	  private String prod_calendar_day;
	  /**
	   * 班次
	   */
	  @Column(name="PROD_SHIFT_CODE")
	  private String prod_shift_code;
	  /**
	   * 按灯状态
	   */
	  @Column(name="ANDON_STATUS")
	  private String andon_status;
	  /**
	   * 问题录入状态  
	   */
	  @Column(name="SOLUTION_STATUS")
	  private String solution_status;
	  /**
	   * 响应时间
	   */
	  @Column(name="RESPOND_VALUE")
	  private String respond_value;
	  /**
	   * 处理时间
	   */
	  @Column(name="MANAGE_VALUE")
		private String manage_value;
	  /**
	   * 触发时间
	   */
	  @Column(name="T_TIME")
	  @JSONField(format="yyyy-MM-dd HH:mm:ss")
	  private Date t_time;
	  /**
	   * 响应时间
	   */
	  @Column(name="R_TIME")
	  @JSONField(format="yyyy-MM-dd HH:mm:ss")
	  private Date r_time;
	  /**
	   * 录入时间
	   */
	  @Column(name="C_TIME")
	  @JSONField(format="yyyy-MM-dd HH:mm:ss")
	  private Date c_time;
	  
	  
	  /**
	   * 备注
	   */
	  @Column(name="REMARK")
	  private String remark;
	  
	  
	  
	  
	
	
	
	

	
}
