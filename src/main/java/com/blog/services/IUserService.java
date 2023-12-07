package com.blog.services;

import com.blog.models.User;
import com.blog.models.dtos.UserDTO;

public interface IUserService {

    void createUser(UserDTO userDTO);

    String login(String phone, String password);
}
