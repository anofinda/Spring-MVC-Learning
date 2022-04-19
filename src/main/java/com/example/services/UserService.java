package com.example.services;

import com.example.entities.User;
import com.example.mybatisMappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author dongyudeng
 */
@Component
@Transactional(rollbackFor = {RuntimeException.class})
public class UserService {
    @Autowired
    UserMapper userMapper;

    public User getUserById(long id) {
        User user = userMapper.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        return user;
    }

    public User getUserByName(String username) {
        User user = userMapper.getUserByName(username);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        return user;
    }

    private void register(User user) {
        userMapper.insertUser(user);
    }

    public void register(String username, String password,String email, String gender) {
        if (userMapper.getUserByName(username) != null) {
            throw new RuntimeException("User exits.");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setGender(gender);
        register(user);
    }

    public User login(String username, String password) {
        User user = userMapper.getUserByName(username);
        if (user == null) {
            throw new RuntimeException("User not exit.");
        }
        if (user.getPassword().equals(password)) {
            return user;
        } else {
            throw new RuntimeException("Wrong Password.");
        }
    }

    public void updatePassword(String username, String password) {
        User user = userMapper.getUserByName(username);
        if (user == null) {
            throw new RuntimeException("User not found.");
        }
        userMapper.updatePassword(password, user.getId());
    }

    public long getUserNumber() {
        return userMapper.getUserNumber();
    }

    public List<User> getAllUsers() {
        return userMapper.getUsers();
    }

}
