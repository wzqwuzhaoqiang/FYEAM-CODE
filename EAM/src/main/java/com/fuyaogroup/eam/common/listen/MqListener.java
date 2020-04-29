package com.fuyaogroup.eam.common.listen;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.soa.eis.adapter.framework.common.CacheManager;
import com.soa.eis.adapter.framework.config.ConfigConstants;
import com.soa.eis.adapter.framework.provider.IServiceProvider;
import com.soa.eis.adapter.framework.provider.impl.BaseServiceProvider;

@Deprecated
public class MqListener implements ServletContextListener{
//	private WebApplicationContext springContext;
//	private DefaultSqlSessionFactory sqlSessionFactory;

	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
	public void contextInitialized(ServletContextEvent sce) {
		try {
			/*
			springContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
			if(springContext!=null){
				sqlSessionFactory = (DefaultSqlSessionFactory) springContext.getBean("sqlSessionFactory");
				SqlSession session = sqlSessionFactory.openSession();
				EmpMapper empMapper = session.getMapper(EmpMapper.class);
				if(empMapper!=null){
					Emp emp = empMapper.findByEmpNo(7369);
					LogUtil.getInstance().info("*****************************************>emp = " + emp);
				}
			}
			LogUtil.getInstance().info("*****************************************>sqlSessionFactory = " + sqlSessionFactory);
			*/
			IServiceProvider provider = BaseServiceProvider.getInstance();
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
