package com.blog.dao;

import com.blog.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    //User实体类，主键为Long类型
    User findByUsernameAndPassword(String username,String password);

    User findByUsername(String username);
}
