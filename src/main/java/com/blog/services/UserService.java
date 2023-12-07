package com.blog.services;

import com.blog.components.JwtTokenUtil;
import com.blog.exceptions.DataNotFoundException;
import com.blog.exceptions.ExistData;
import com.blog.models.Role;
import com.blog.models.User;
import com.blog.models.dtos.UserDTO;
import com.blog.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Override
    public void createUser(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())){
            throw new ExistData("Username is exist");
        }
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .username(userDTO.getUsername())
                .password(encodedPassword)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("Invalid phone number / password"));

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, password,user.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(user);
    }
}
