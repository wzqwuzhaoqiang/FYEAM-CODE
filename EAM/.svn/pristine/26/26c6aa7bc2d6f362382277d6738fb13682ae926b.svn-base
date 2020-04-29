package com.fuyaogroup.eam;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import com.fuyaogroup.eam.common.datasources.DynamicDataSourceConfig;


@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@Import({DynamicDataSourceConfig.class})
@MapperScan("com.fuyaogroup.eam.modules.*.dao")
@ServletComponentScan(basePackages = "com.fuyaogroup.eam")
@ImportResource(value ="classpath:/scheduling-config.xml")
public class EamApplication extends SpringBootServletInitializer{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
	return application.sources(EamApplication.class);
	}
	public static void main(String[] args){
		SpringApplication.run(EamApplication.class,args);
		
	}
	
	
//	@Bean
//	  public FilterRegistrationBean indexFilterRegistration() {
//		    FilterRegistrationBean registration = new FilterRegistrationBean(new HttpBearerAuthFilter());
//		    registration.addUrlPatterns("/*");
//		    registration.addUrlPatterns("/WEB-INF/view/**");
//		    registration.addInitParameter("paramName", "paramValue");
////	        registration.setName("httpBearerAuthFilter");
////	        registration.setOrder(1);
//	        return registration;
//		  }


}
