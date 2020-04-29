package com.fuyaogroup.eam.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.collection.CollectionUtil;

import com.fuyaogroup.eam.common.Json.JsonResult;
import com.fuyaogroup.eam.common.enums.ComputerStatusEnum;
import com.fuyaogroup.eam.common.enums.OAOrgEnum;
import com.fuyaogroup.eam.modules.mes.model.AndonHis;
import com.fuyaogroup.eam.modules.mes.model.RepairBill;
import com.fuyaogroup.eam.modules.mes.service.AndonService;
import com.fuyaogroup.eam.util.EnumUtil;
import com.fuyaogroup.eam.util.FusionEAMAPIUtil;
import com.fuyaogroup.eam.util.ValidatorUtils;
import com.soa.eis.adapter.framework.message.IMsgObject;
import com.soa.eis.adapter.framework.message.impl.GroupRecord;


@RestController
@Slf4j
public class EamFusionController {
	@Autowired
    private AndonService andonService;
	
	@Autowired
	private FusionEAMAPIUtil frutil = new FusionEAMAPIUtil();
	@ResponseBody //用于返回json数据或者text格式文本
	@RequestMapping(value = "/api",method = RequestMethod.GET)
	 public String Get() {
		//操作
		List<AndonHis> list = null;
		Date date = new Date();
		try {
			list = andonService.getAndonHisByStatus("关闭", new Date());
			System.out.print(list.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getCause().toString());
		}
		
	  return null==list?"空的":list.toString();
	 }
	
//	@RequestMapping(value = "/api/MesPressLight", method = RequestMethod.POST,
//		  consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
//		  produces = { MediaType.APPLICATION_JSON_VALUE})
	@PostMapping(value="/api/MesPressLight", consumes={"application/json"})
	 public @ResponseBody JsonResult  Post(@RequestBody Map<String, String> map) {
		//1.验证MES传过来的字段（MD5）
		Map<String, String> reqMap = ValidatorUtils.validateMap(map);
		if(CollectionUtil.isEmpty(map)){
			return new JsonResult("500", "检修单生成失败！", "检修单生成失败，原因:传入参数为空");
		}
		//2.创建AndonHis类
		JSONObject jsonObj = new JSONObject(reqMap);
		AndonHis andon = com.alibaba.fastjson.JSONObject.parseObject(jsonObj.toString(),AndonHis.class);
		//3.生成检修单
		try {
			boolean flag= frutil.CreateNewWorkOrder(andon);
		} catch (Exception e) {
			log.error("生产检修单{}失败，原因:{}",andon.getEvent_id(),e.getMessage());
			return new JsonResult("500", "检修单生成失败！", "检修单生成失败，原因:"+e.getMessage());
			
		}
	  return new JsonResult("200", "检修单生成成功！","success");
	 }
	
	public  IMsgObject  addAssetWorkOrder(IMsgObject reqMo) {
		//1.创建AndonHis类
		RepairBill rb = this.createReqMap(reqMo);
		//2.生成检修单
		try {
			boolean flag= frutil.CreateAssetWorkOrder(rb);
			
		} catch (Exception e) {
			log.error("生产检修单{}失败，原因:{}",rb.getId(),e.getMessage());
			return null;
			
		}
	  return reqMo;
	 }

	private RepairBill createReqMap(IMsgObject reqMo) {
		RepairBill rb = new RepairBill();
		//解析reqMo
		List<GroupRecord> gr = reqMo.getReqGroupRecord("WorkOrder");
		GroupRecord reqRecord = gr.get(0);
		rb.setSpareParts(reqRecord.getAttribute("sparePartsName")+"("+reqRecord.getAttribute("sparePartsCode")+"):"+reqRecord.getAttribute("sparePartsQuantity")+";");
		if(reqMo.getReqValue("orderId")==null||reqMo.getReqValue("orderId")==""){
			rb.setId(new Long(reqMo.getReqValue("userId")+(int)Math.random()*50+51));//单据号
		}
//		rb.setId(Long.parseLong(reqMo.getReqValue("orderId")));//单据号
		rb.setCmpName(EnumUtil.getByCode(reqMo.getReqValue("cmpCode"), OAOrgEnum.class).getMessage());
		rb.setProposer(reqMo.getReqValue("proposer"));//申请人
		rb.setUserId(reqMo.getReqValue("userId"));//申请工号
		rb.setApplyDepartment(reqMo.getReqValue("applyDepartment"));//申请部门
		rb.setAssetName(reqMo.getReqValue("assetName"));//资产名称
		rb.setAssetCode(reqMo.getReqValue("AssetNumber"));//资产编码
		rb.setTelphone(reqMo.getReqValue("telphone"));//电话
		rb.setFaultCause(reqMo.getReqValue("faultCause"));//故障现象
		rb.setAssetModel(reqMo.getReqValue("assetModel"));//机型
		rb.setSerialNumber(reqMo.getReqValue("SerialNumber"));//序列号
		rb.setStatus(EnumUtil.getByCode(reqMo.getReqValue("status"), ComputerStatusEnum.class));//保修状态
		rb.setAllocation(reqMo.getReqValue("allocation"));//配置
		rb.setSuggestion(reqMo.getReqValue("suggestion"));//故障建议
		rb.setRemark(reqMo.getReqValue("remark"));//备注
		rb.setAcceptCheck(reqMo.getReqValue("acceptionCheck"));//验收意见
		
		return rb;
	}
}
