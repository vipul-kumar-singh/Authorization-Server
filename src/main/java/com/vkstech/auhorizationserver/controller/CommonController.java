package com.vkstech.auhorizationserver.controller;

import com.vkstech.auhorizationserver.dto.LoginForm;
import com.vkstech.auhorizationserver.dto.SignUpForm;
import com.vkstech.auhorizationserver.service.AuthenticationService;
import com.vkstech.auhorizationserver.utils.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private AuthenticationService authenticationService;

    private final Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

    //Hello World
    @GetMapping("/hello")
    public ResponseEntity hello() {
        LOGGER.info("CommonController hello...");

        return new ResponseEntity(new ResponseObject("Hello World"), HttpStatus.OK);
    }

    //Sign-up Controller
    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody SignUpForm signUpForm, BindingResult bindingResult, HttpServletRequest request) {
        LOGGER.info("CommonController signup...");

        if (bindingResult.hasErrors()) {
            List<ObjectError> fieldErrors = bindingResult.getAllErrors();
            for (ObjectError error : fieldErrors) {
                return new ResponseEntity(new ResponseObject(error.getDefaultMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        return authenticationService.signUp(signUpForm, request);
    }

    //Login Controller
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) {
        LOGGER.info("CommonController login...");

        if (bindingResult.hasErrors()) {
            List<ObjectError> fieldErrors = bindingResult.getAllErrors();
            for (ObjectError error : fieldErrors) {
                return new ResponseEntity(new ResponseObject(error.getDefaultMessage()), HttpStatus.BAD_REQUEST);
            }
        }

        return authenticationService.login(loginForm, request);
    }

    //Log-out controller
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        LOGGER.info("CommonController logout...");
        return authenticationService.logout(request);
    }
}
