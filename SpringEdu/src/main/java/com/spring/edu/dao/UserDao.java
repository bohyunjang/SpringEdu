package com.spring.edu.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.edu.model.UserModel;

@Repository
public class UserDao {

	@Autowired
	SqlSession sqlSession;
	
	public UserModel selectUserByUserName(String userName){
		
		System.out.println("UserDAO.. selectUserByUserName");
		System.out.println("userName:: "+ userName);

		return sqlSession.selectOne("selectUserByUserName",userName); 
	}
	
	public int insertUser(UserModel userModel){
		
		System.out.println("UserDAO... insertUser");
		
		return sqlSession.insert("insertUser", userModel);
		
	}
	
	
}
