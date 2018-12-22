package com.anshao.service;

import com.anshao.domain.User;

import java.util.List;

public interface UserService {


    List<String> queryRoleByUserName(String username);

    User getUserByUserName(String userName);
}
