package com.youku.java.copyright.runtime;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.youku.java.copyright.util.Config;
import com.youku.java.raptor.boot.RuntimeSpringBeans;
/*
 * @author chenlisong
 */
@Configuration
@EnableAutoConfiguration
@EnableAspectJAutoProxy
@EnableTransactionManagement
@EnableScheduling
@PropertySources({
    @PropertySource(value = "classpath:stock-api.properties"),
    @PropertySource(value = "file:etc/stock-api.properties", ignoreResourceNotFound=true)
})
@ComponentScan(basePackages = {
        "com.youku.java.raptor.aspect",
        "com.youku.java.raptor.auth",
        "com.youku.java.raptor.util",
        "com.youku.java.raptor.validation",
        "com.youku.java.raptor.client",
        "com.youku.java.raptor.exception",
		"com.youku.java.copyright.mapper",
		"com.youku.java.copyright.controller",
		"com.youku.java.copyright.service",
		"com.youku.java.copyright.util",
		"com.youku.java.copyright.exception",
		"com.youku.java.copyright.bean",
		"com.youku.java.copyright.aspect"})
@RuntimeSpringBeans(mode = "http-server", withWeb = true)
public class HttpBeans {
	
	@Bean(name="dataSourceCore")
	@DependsOn("config")
	@Primary
	public DataSource dataSourceCore(@Qualifier("config") Config config) throws Exception {
		ComboPooledDataSource instance = new ComboPooledDataSource();
		instance.setDriverClass("com.mysql.jdbc.Driver");
		instance.setJdbcUrl("jdbc:mysql://" + config.coreDbIp + ":" + config.coreDbPort + "/" + config.coreDbName
				+"?useUnicode=true&characterEncoding=utf8");
		instance.setUser(config.coreDbUserName);                      
		instance.setPassword(config.coreDbPassword);

		instance.setMinPoolSize(5);
		instance.setInitialPoolSize(5);
		instance.setAcquireIncrement(1);
		instance.setMaxPoolSize(30);
		instance.setMaxIdleTime(60);

		instance.getConnection();
		return instance;
	}
	
	@Bean(name="sqlSessionFactoryCore")
	@DependsOn("dataSourceCore")
	public SqlSessionFactoryBean sqlSessionFactoryCore(@Qualifier("dataSourceCore") DataSource dataSource) 
			throws PropertyVetoException, SQLException {
		SqlSessionFactoryBean instance = new SqlSessionFactoryBean();
		instance.setDataSource(dataSource);
		instance.setConfigLocation(new ClassPathResource("mybatis.xml"));
		return instance;
	}

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactoryCore");
		configurer.setBasePackage("com.youku.java.copyright.mapper");
		return configurer;
	}
	
	@Bean(name="dataSourceLog")
	@DependsOn("config")
	public DataSource dataSourceLog(Config config) throws Exception {
		ComboPooledDataSource instance = new ComboPooledDataSource();
		instance.setDriverClass("com.mysql.jdbc.Driver");
		instance.setJdbcUrl("jdbc:mysql://" + config.logDbIp + ":" + config.logDbPort + "/" + config.coreDbName
				+"?useUnicode=true&characterEncoding=utf8");
		instance.setUser(config.logDbUserName);
		instance.setPassword(config.logDbPassword);

		instance.setMinPoolSize(5);
		instance.setInitialPoolSize(5);
		instance.setAcquireIncrement(1);
		instance.setMaxPoolSize(30);
		instance.setMaxIdleTime(60);

		instance.getConnection();
		return instance;
	}
	
	@Bean(name="sqlSessionFactoryLog")
	public SqlSessionFactoryBean sqlSessionFactoryLog(@Qualifier("dataSourceLog")DataSource dataSourceLog) throws PropertyVetoException, SQLException {
		SqlSessionFactoryBean instance = new SqlSessionFactoryBean();
		instance.setDataSource(dataSourceLog);
		instance.setConfigLocation(new ClassPathResource("mybatislog.xml"));
		return instance;
	}

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurerLog() {
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactoryLog");
		configurer.setBasePackage("com.youku.java.copyright.mapperlog");
		return configurer;
	}
	
	@Bean
	public DataSourceTransactionManager transactionManager(@Qualifier("dataSourceCore") DataSource dataSourceCore)
			throws PropertyVetoException, SQLException {
		return new DataSourceTransactionManager(dataSourceCore);
	}
	
	@Bean()
    public  ThreadPoolTaskScheduler  taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(20);
        return  taskScheduler;
    }
	
}
