package com.fuyaogroup.eam.common.listen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.fuyaogroup.eam.util.MqHandler;
import com.soa.eis.adapter.framework.common.CacheManager;
import com.soa.eis.adapter.framework.config.ConfigConstants;
import com.soa.eis.adapter.framework.provider.IServiceProvider;
import com.soa.eis.adapter.framework.provider.impl.BaseServiceProvider;

@Service
public class BeanDefinedListener implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	private MqHandler mqHandler;
	
	public MqHandler getMqHandler() {
		return mqHandler;
	}

	public void setMqHandler(MqHandler mqHandler) {
		this.mqHandler = mqHandler;
	}

	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		try {
			IServiceProvider provider = new BaseServiceProvider();
			provider.setServiceHandler(mqHandler);
			int providerThreadNum = Integer.parseInt(CacheManager.getInstance()
					.getConfig().getProperty(ConfigConstants.PROVIDER_HANDLER_MAXNUM));
			for (int i = 0; i < providerThreadNum; i++) {
				new Thread(provider).start();
			}
			provider.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
