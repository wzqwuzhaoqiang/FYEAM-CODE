package com.fuyaogroup.eam.common.datasources;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
//fantasy.dao.master包下的mapper类使用该数据源
@MapperScan(basePackages = SlaveDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "slaveSqlSessionFactory")
public class SlaveDataSourceConfig {
	 static final String PACKAGE = "com.fuyaogroup.eam.modules.fusion.dao";
	 
	 @Autowired 
	 @Qualifier("slaveDS") 
	 private DataSource slaveDS; 
	 
	 @Bean 
	 public SqlSessionFactory slaveSqlSessionFactory() throws Exception 
	 { 
		 SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		 factoryBean.setDataSource(slaveDS);
		 return factoryBean.getObject();
		 } 
	 
	 @Bean 
	 public SqlSessionTemplate sqlSessionTemplateSlave() throws Exception {
		 SqlSessionTemplate template = new SqlSessionTemplate(slaveSqlSessionFactory()); 
		 return template;
		 }
}
