package com.spring.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.spring.edu.dao.UserDao;
import com.spring.edu.model.UserModel;

@Component
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String userName) 
			throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		UserModel userModel = userDao.selectUserByUserName(userName);
		/*UserModel user = new UserModel();
		
		final String testUserName = "test@test.com";
		final String testPassword = "test";
		final String testNickName = "Spring Test Nick Name...!!!!! Tester..";
		
		user.setUserName(userName);
		if(user.getUserName().equals(testUserName)){
			user.setUserName(testUserName);
			user.setPassword(encoder.encode(testPassword));
			user.setNickName(testNickName);
			user.setAuthority(AuthorityUtils.createAuthorityList("ROLE_USER"));
		*/
		
		if(userModel !=null){
			System.out.println("authority test!!!");
			System.out.println("authority"+userModel.getAuthority());
			userModel.setAuthorities(AuthorityUtils
					.createAuthorityList(userModel.getAuthority()));
		}else{
			System.out.println("not found authority");
			throw new UsernameNotFoundException(userName);
		}
		return userModel;
	}

}
