package com.fuyaogroup.eam.util;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soa.eis.adapter.framework.exception.EisException;
import com.soa.eis.adapter.framework.handler.IServiceHandler;
import com.soa.eis.adapter.framework.message.IMsgObject;

@Slf4j
@Component
public class MqHandler implements IServiceHandler {
	
	@Autowired
	 Invoke invoke;
	
	public IMsgObject execute(IMsgObject reqMo) throws EisException {
		// 获取请求数据
		String sourceSysID = reqMo.getSourceSysID();
		String serviceID = reqMo.getServiceID();
		String serialNO = reqMo.getSerialNO();
		log.info(
				"请求方系统号：" + sourceSysID + "  接口号：" + serviceID + "  消息流水号："
						+ serialNO);
		log.info(reqMo.toString());
		IMsgObject resMo = (IMsgObject) invoke.invokeMethod(serviceID, reqMo);
		if(resMo==null){
			reqMo.setServResStatus("FAIL");
			return reqMo;
		}
		return resMo;
	}

	public void handleException(EisException arg0) {

	}

	public Invoke getInvoke() {
		return invoke;
	}

	public void setInvoke(Invoke invoke) {
		this.invoke = invoke;
	}
}
