package com.anshao.service.impl;

import com.anshao.dao.UserDao;
import com.anshao.domain.User;
import com.anshao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public List<String> queryRoleByUserName(String username) {
        return userDao.queryRoleByUserName(username);
    }

    @Override
    public User getUserByUserName(String userName) {
        User user = userDao.getUserByUserName(userName);
        System.out.println(user.getPassword());
        return user;
    }
}
