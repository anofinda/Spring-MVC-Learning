package com.example.mybatisMappers;

import com.example.entities.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
    @Insert("INSERT INTO users (username,password,email,gender) VALUES (#{user.username},#{user.password},#{user.email},#{user.gender})")
    void insertUser(@Param("user") User user);

    @Update("UPDATE users SET password=#{password} WHERE id=#{id}")
    void updatePassword(@Param("password")String password,@Param("id")long id);

    @Select("SELECT * FROM users")
    List<User> getUsers();

    @Select("SELECT COUNT(*) FROM users")
    long getUserNumber();
}
