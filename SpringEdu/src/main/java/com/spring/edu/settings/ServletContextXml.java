package com.spring.edu.settings;

import java.io.IOException;

import javax.annotation.processing.SupportedSourceVersion;

//import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@ComponentScan("com.spring.edu")
@EnableWebMvc // <mvc:annotation - driven />
@Import({SecurityContextXml.class}) // 추가된 부분
@EnableTransactionManagement
public class ServletContextXml extends WebMvcConfigurerAdapter 
						implements TransactionManagementConfigurer{
	
	//Database 트랜스액션 생성
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager;
	
	public void addResouceHandler(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resource/");
	}
	
	// Mybatis를 사용하기 위해 설정하는 메소드
	@Bean
	public SqlSessionFactoryBean setSqlSessionFactoryBean(
			DataSource dataSource, ApplicationContext applicationContext) throws IOException{
		
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		
		final String configLocation = "/WEB-INF/spring/mybatis/mybatis-conf.xml";
		final String resourceLocation = "/WEB-INF/spring/mybatis/mapper/sql.xml";
		
		sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setConfigLocation(applicationContext.getResource(configLocation));
		sqlSessionFactory.setMapperLocations(applicationContext.getResources(resourceLocation));
		
		return sqlSessionFactory;
	}
	

	// Mybatis를 이요하기 위함인데 Spring Framework 자체의 SQL 질의 시스템을 이용해서 Mybatis가 동작하게끔 해주고
	// 실제로 질의문을 던질수 있는 메소드들을 사용할 수 있게 해주는 클래스 변수를 @Autowired로 사용할 수 있게 제공함
	@Bean(name="sqlSession")
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
		
		SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
		
		return sqlSessionTemplate;
	}
	
	// 데이터베이스 서버와 웹서버를 연결시켜주는 클래스.
	// 내부에선 Tomcat Connection Pool을 이용하기 때문에 데이터랑 빠른 통신이 가능
	@Bean(name="dataSource")
	public DataSource dataSource(){
		
		DataSource dataSource = new DataSource();
		
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/edu"
				+ "?useSSl=false&characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("test");
		
		dataSource.setMaxActive(10);
		dataSource.setInitialSize(2);
		
		dataSource.setMinIdle(2);
		dataSource.setMaxIdle(5);
		
		dataSource.setTestWhileIdle(true);
		dataSource.setValidationQuery("SELECT 1");
		dataSource.setTimeBetweenEvictionRunsMillis(7200000);
		dataSource.setMinEvictableIdleTimeMillis(28000000);
		
		return dataSource; 
	}
	
	
	@Bean
	public DataSourceTransactionManager DataSourceTransactionManager(DataSource dataSource){
		
		DataSourceTransactionManager dataSourceTransactionManager = 
				new DataSourceTransactionManager(dataSource);
		
		return dataSourceTransactionManager;
	}
	
	@Bean
	public InternalResourceViewResolver setInternalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();

		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setViewClass(JstlView.class);

		return resolver;
	}

	@Bean
	public PasswordEncoder setPasswordEncoder() {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}

	@Override // <mvc:interceptors>
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(setWebContentInterceptor());
	}

	private WebContentInterceptor setWebContentInterceptor() {
		WebContentInterceptor interceptor = new WebContentInterceptor();
		interceptor.setCacheSeconds(0);
		return interceptor;
	}

	
	// @Transaction 어노테이션으로 트랜잭션을 관리할 수 있게 해주는 클래스
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		// TODO Auto-generated method stub
		
		return dataSourceTransactionManager;
	}
	
}








































