package com.example.library.repo;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.library.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

//	User getUserByUserName(String email);
//	@Query("select u from User u where u.email=email")
//	public String  getEmail(@Param("email")String email);
	

}
