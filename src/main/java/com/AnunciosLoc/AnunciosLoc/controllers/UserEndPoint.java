package com.AnunciosLoc.AnunciosLoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.AnunciosLoc.AnunciosLoc.services.UserProfileService;
import com.AnunciosLoc.AnunciosLoc.services.UserService;

import xml.soap.user.*;

@Endpoint
@Component
public class UserEndPoint {

    private static final String NAMESPACE_URI = "http://user.soap.xml";

    @Autowired
    private final UserService userService;


    public UserEndPoint(UserService userService, UserProfileService userProfileService) {
        this.userService = userService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddUserRequest")
    @ResponsePayload
    public UserResponse addUser(@RequestPayload AddUserRequest request) {
        return this.userService.addUser(request);
    }
    

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LoginRequest")
    @ResponsePayload
    public LoginResponse login(@RequestPayload LoginRequest request) {
        return this.userService.login(request);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "LogoutRequest")
    @ResponsePayload
    public LogoutResponse logout(@RequestPayload LogoutRequest request) {
        return this.userService.logout(request);
    }

}
