package com.spring.edu.settings;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class SecurityWebXml extends AbstractSecurityWebApplicationInitializer{
	
	protected void beforeSpringSecurityFilterChain(ServletContext servletContext){
		
		super.insertFilters(servletContext, getCharacterEncodingFilter());
		
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(ServletContextXml.class);
		ContextLoaderListener listener = new ContextLoaderListener(rootContext);
		servletContext.addListener(listener);
		rootContext.setServletContext(servletContext);
		
		DispatcherServlet dispatcher = new DispatcherServlet(rootContext);
		
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcher);
		
		servlet.addMapping("/");
		servlet.setLoadOnStartup(1);
		
	}
	
	private CharacterEncodingFilter getCharacterEncodingFilter(){
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		
		return filter;
	}

}
