package com.spring.edu.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityContextXml extends WebSecurityConfigurerAdapter {

	/*
	 * @Autowired PasswordEncoder encoder;
	 * 
	 * @Autowired public void configureGlobal(AuthenticationManagerBuilder
	 * auth)throws Exception{ final String testUserName = "test@test.com"; final
	 * String testPassword = "test";
	 * 
	 * auth.inMemoryAuthentication().passwordEncoder(encoder).withUser(
	 * testUserName).password(testPassword).authorities("ROLE_USER"); }
	 * 
	 * @Override public void configure(WebSecurity web) throws Exception{
	 * web.ignoring().antMatchers("/resources/**"); }
	 */
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").authenticated().and().formLogin().loginPage("/login").permitAll(false)
				.usernameParameter("login_email").passwordParameter("login_password")
				.loginProcessingUrl("/login-request").and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
				.invalidateHttpSession(true);
	}

}
