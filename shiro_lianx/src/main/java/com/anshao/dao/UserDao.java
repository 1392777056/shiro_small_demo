package com.anshao.dao;

import com.anshao.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao {

    //根据名称查数据
    @Select("select username,password from users where username = #{username}")
    User getUserByUserName(@Param("username") String username);

    //查询角色
    @Select("select role_name from user_roles where username = #{username}")
    List<String> queryRoleByUserName(@Param("username") String username);
}
