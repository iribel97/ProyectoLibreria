package com.egg.springLibrary.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.egg.springLibrary.entidades.UserEn;


/**
 *
 * @author irina
 */
@Repository
public interface UserRepository extends JpaRepository<UserEn, String>{
    
    @Query("SELECT u FROM UserEn u WHERE u.email LIKE :email")
    public UserEn findByEmail(@Param("email")String email);
}
