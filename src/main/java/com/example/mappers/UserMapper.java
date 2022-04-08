package com.example.mappers;

import com.example.entity.User;
import org.apache.ibatis.annotations.*;

/**
 * @author dongyudeng
 */
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM users WHERE id=#{id}")
    User getUserById(@Param("id") long id);

    @Select("SELECT * FROM users WHERE username= #{username}")
    User getUserByName(@Param("username") String username);

    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    @Insert("INSERT INTO users (username,password,gender) VALUES (#{user.username},#{user.password},#{user.gender})")
    void InsertUser(@Param("user") User user);

    @Update("UPDATE users SET password=#{password} WHERE id=#{id}")
    void updatePassword(@Param("password")String password,@Param("id")long id);
}
