package com.seemee.userservice.controller;

import com.seemee.userservice.constants.SeeMeeConstants;
import com.seemee.userservice.dto.AuthenticateUser;
import com.seemee.userservice.dto.LoginResponse;
import com.seemee.userservice.model.User;
import com.seemee.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getProfile/{email}")
    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    public ResponseEntity<User> getUserProfile(@PathVariable String email) {
        User user = userService.getUserProfile(email);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody AuthenticateUser authenticateUser) {
        LoginResponse response = userService.authenticateUser(authenticateUser);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = SeeMeeConstants.FRONTEND_LOCAL_URL)
    @PostMapping("/createProfile")
    public ResponseEntity<String> createUserProfile(@RequestBody User user) {
        userService.createUserProfile(user);
        return ResponseEntity.ok("User created successfully. A welcome email has been sent to " + user.getEmail());
    }

    @CrossOrigin(origins = SeeMeeConstants.FORNTEND_URL)
    @PostMapping("/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody AuthenticateUser authenticateUser) {
        String response = userService.updatePassword(authenticateUser);
        if (response.equalsIgnoreCase("success")) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.ok("Password update failed, Please try again after sometime");
        }
    }

    @CrossOrigin(origins = SeeMeeConstants.FORNTEND_URL)
    @PostMapping("/address/{index}")
    public ResponseEntity<String> handleAddressUpdates(@RequestBody User user, @PathVariable String index) {
        userService.updateAddress(user, index);
        return ResponseEntity.ok("Address updated successfully");
    }

    @CrossOrigin
    @PostMapping("/updateProfile")
    public ResponseEntity<String> updateProfile(@RequestBody User user) {
        // userService.updateProfile(user);
        return ResponseEntity.ok("Profile updated successfully");
    }

}