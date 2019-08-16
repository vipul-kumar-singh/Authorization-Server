package com.vkstech.auhorizationserver.service;

import com.vkstech.auhorizationserver.constants.ResponseMessages;
import com.vkstech.auhorizationserver.dto.LoginForm;
import com.vkstech.auhorizationserver.dto.SignUpForm;
import com.vkstech.auhorizationserver.model.Role;
import com.vkstech.auhorizationserver.model.User;
import com.vkstech.auhorizationserver.repository.RoleRepository;
import com.vkstech.auhorizationserver.repository.UserRepository;
import com.vkstech.auhorizationserver.utils.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccessTokenService accessTokenService;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    public ResponseEntity signUp(SignUpForm signUpForm, HttpServletRequest request) {
        LOGGER.info("AuthenticationService signUp...");

        //check is username already exists
        User user = userRepository.findByUsername(signUpForm.getUsername());
        if (user != null) {
            LOGGER.error(ResponseMessages.USERNAME_EXISTS);
            return new ResponseEntity(new ResponseObject(ResponseMessages.USERNAME_EXISTS), HttpStatus.BAD_REQUEST);
        }

        //if username does not exists, create a new user
        user = new User();
        user.setUsername(signUpForm.getUsername());
        user.setEmail(signUpForm.getEmail());
        user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);

        //setting roles
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2L).get());
        user.setRoles(roles);

        //saving user
        try {
            userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error(ResponseMessages.SIGN_UP_FAILURE, e);
            return new ResponseEntity(new ResponseObject(ResponseMessages.SIGN_UP_FAILURE), HttpStatus.BAD_REQUEST);
        }

        //AutoLogin - fetch access token
        OAuth2AccessToken token = accessTokenService.getAccessToken(user, request);
        if (token != null && !token.isExpired()) {
            LOGGER.info(ResponseMessages.SIGN_UP_SUCCESS);
            return new ResponseEntity(new ResponseObject(ResponseMessages.SIGN_UP_SUCCESS, token), HttpStatus.OK);
        }

        LOGGER.warn(ResponseMessages.AUTO_LOGIN_FAILURE);
        return new ResponseEntity(new ResponseObject(ResponseMessages.SIGN_UP_SUCCESS + ". " + ResponseMessages.CONTINUE_LOGIN), HttpStatus.OK);


    }

    public ResponseEntity login(LoginForm loginForm, HttpServletRequest request) {
        LOGGER.info("AuthenticationService login...");

        //check is username exists
        User user = (User) userDetailService.loadUserByUsername(loginForm.getUsername());

        //verify password
        if (!passwordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
            LOGGER.error(ResponseMessages.INVALID_PASSWORD);
            return new ResponseEntity(new ResponseObject(ResponseMessages.INVALID_PASSWORD), HttpStatus.UNAUTHORIZED);
        }

        //fetch access token
        OAuth2AccessToken token = accessTokenService.getAccessToken(user, request);
        if (token != null && !token.isExpired()) {
            LOGGER.info(ResponseMessages.LOGIN_SUCCESS);
            return new ResponseEntity(new ResponseObject(ResponseMessages.LOGIN_SUCCESS, token), HttpStatus.OK);
        }

        LOGGER.error(ResponseMessages.LOGIN_FAILURE);
        return new ResponseEntity(new ResponseObject(ResponseMessages.LOGIN_FAILURE), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity logout(HttpServletRequest request) {
        LOGGER.info("AuthenticationService logout...");

        if (!accessTokenService.removeAccessToken(request))
            return new ResponseEntity(new ResponseObject("Invalid access token"), HttpStatus.UNAUTHORIZED);

        return new ResponseEntity(new ResponseObject("Logout successful"), HttpStatus.OK);
    }
}
