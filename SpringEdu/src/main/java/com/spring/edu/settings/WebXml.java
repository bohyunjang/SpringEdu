package com.spring.edu.settings;

import java.nio.file.DirectoryStream.Filter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.util.ReflectionUtils.FieldFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class WebXml implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
	
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(ServletContextXml.class);
		
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		FilterRegistration.Dynamic charEncodingFilter = servletContext.addFilter("characterEncodingFilter", getCharacterEncodingFilter()); // charEncodingFilter UTF-8
		
		
		rootContext.setServletContext(servletContext);
		
		ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(rootContext));
		
		charEncodingFilter.addMappingForServletNames(null, true, servlet.getName()); // charEncodingFilter UTF-8
		
		servlet.addMapping("/");
		servlet.setLoadOnStartup(1);
		
	}
	
	// UTF-8 filter
	private CharacterEncodingFilter getCharacterEncodingFilter(){
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		
		filter.setEncoding("UTF-8");
		filter.setForceEncoding(true);
		return filter;
	}
	
}
