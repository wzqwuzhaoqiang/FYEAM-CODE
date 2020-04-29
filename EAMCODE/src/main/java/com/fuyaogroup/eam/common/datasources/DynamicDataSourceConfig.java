package com.fuyaogroup.eam.common.datasources;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * 配置多数据源
 *
 * @author CJ ♦ Wang
 * @version 1.0
 */
@Configuration
public class DynamicDataSourceConfig {

    @Bean(name = "masterDS")
    @ConfigurationProperties("spring.datasource.mes")
    public DataSource firstDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "slaveDS")
    @ConfigurationProperties("spring.datasource.eam")
    public DataSource secondDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

//    @Bean
//    @Primary
//    public DynamicDataSource dataSource(DataSource firstDataSource, DataSource secondDataSource) {
//        Map<Object, Object> targetDataSources = new HashMap<>();
//        targetDataSources.put(DataSourceNames.FIRST, firstDataSource);
//        targetDataSources.put(DataSourceNames.SECOND, secondDataSource);
//        return new DynamicDataSource(firstDataSource, targetDataSources);
//    }
}
