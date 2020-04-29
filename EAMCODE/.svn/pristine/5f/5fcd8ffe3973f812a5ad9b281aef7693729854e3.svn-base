package com.fuyaogroup.eam.modules.mes.model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fuyaogroup.eam.common.model.BaseEntity;


/**
 * 创建按灯模块视图表
 * @author liangqing.chi
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("HMCS.FY_ANDON_SOLUTION_ITF_EAM")
public class AndonHisTemp extends BaseEntity{
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableField(value="EVENT_ID",fill=FieldFill.INSERT)
	Double	  event_id;	  // 安灯事件ID（以触发事件ID进行汇总，取触发事件ID）
	
	@TableField(value="fault_type",fill=FieldFill.INSERT)
	String	  fault_type         ;	  // 故障类型
	String	  fault_code         ;	  // 故障編碼
	String	  fault_info         ;	  // 故障
	String	  reason             ;	  // 问题原因
	String	  description        ;	  // 问题描述
	String	  solution           ;	  // 处理措施
	String	  update_file        ;	  // 更新文件
	String	  falut_scrapt       ;	  // 故障废品
	String	  response_unit      ;	  // 责任单位
	String	  repair_man         ;	  // 维修人员
	String	  spare_part         ;	  // 备品备件
	String	  timteout_analysis  ;	  // 超时分析
	String	  is_stop            ;	  // 是否停机
	String	  operate_man        ;	  // 录入人
	String	  status             ;	  // 问题录入状态
	String	  remark             ;	  // 备注
	String	  mes_process_status ;	  // 数据到MES业务表状态 P:待处理 S:处理完成 E:处理结果错误
//	Date	  sync_update_time;	  // MES同步最后更新时间
//	String	  mes_process_msg    ;	  // MES处理信息
	String	  attribute1         ;	  // 备用字段
	String	  attribute2         ;	  // 备用字段
	String	  attribute3         ;	  // 备用字段
	String	  attribute4         ;	  // 备用字段
	String	  attribute5         ;	  // 备用字段
	String	  attribute6         ;	  // 备用字段
	String	  attribute7         ;	  // 备用字段
	String	  attribute8         ;	  // 备用字段
	String	  attribute9         ;	  // 备用字段
	String	  attribute10        ;	  // 备用字段
	
}
