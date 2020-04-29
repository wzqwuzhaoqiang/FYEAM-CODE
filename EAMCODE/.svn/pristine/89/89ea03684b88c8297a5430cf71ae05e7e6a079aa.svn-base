package com.fuyaogroup.eam.common.datasources.aspect;


import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.fuyaogroup.eam.common.datasources.DataSourceNames;
import com.fuyaogroup.eam.common.datasources.DynamicDataSource;
import com.fuyaogroup.eam.common.datasources.annotation.DataSource;

/**
 * 多数据源，切面处理类
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017/9/16 22:20
 */
@Aspect
@Component
public class DataSourceAspect implements Ordered {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("@within(com.fuyaogroup.eam.common.datasources.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DataSource ds = (DataSource)method.getDeclaringClass().getAnnotation(DataSource.class);
//        DataSource ds = method.getAnnotation(DataSource.class);
        if(ds == null || ds.name().equals(DataSourceNames.FIRST)){
            DynamicDataSource.setDataSource(DataSourceNames.FIRST);
            logger.debug("set datasource is " + DataSourceNames.FIRST);
        }else if(ds.name().equals(DataSourceNames.SECOND)){
            DynamicDataSource.setDataSource(DataSourceNames.SECOND);
            logger.debug("set datasource is " + DataSourceNames.SECOND);
        }else {
            DynamicDataSource.setDataSource(ds.name());
            logger.debug("set datasource is " + ds.name());
        }

        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            logger.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
