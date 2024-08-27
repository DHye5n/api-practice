package com.dgm.board.controller;


import com.dgm.board.model.user.User;
import com.dgm.board.model.user.UserAuthenticationResponse;
import com.dgm.board.model.user.UserLoginRequestBody;
import com.dgm.board.model.user.UserSignUpRequestBody;
import com.dgm.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> signUp(@Valid @RequestBody UserSignUpRequestBody userSignUpRequestBody) {
        User user = userService.signUp(userSignUpRequestBody.getUsername(), userSignUpRequestBody.getPassword());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(@Valid @RequestBody UserLoginRequestBody userLoginRequestBody) {
        UserAuthenticationResponse response = userService.authenticate(userLoginRequestBody.getUsername(), userLoginRequestBody.getPassword());
        return ResponseEntity.ok(response);
    }

}
