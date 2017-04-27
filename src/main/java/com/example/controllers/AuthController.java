package com.example.controllers;

import com.example.dictionaries.TokenStatus;
import com.example.models.ConfirmationToken;
import com.example.services.AuthService;
import com.example.services.UserService;
import com.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {
        authService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/confirm")
    public ResponseEntity<?> confirm(@PathParam("token") String token, @PathParam("email") String email) {
        authService.confirm(email, token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/auth/requestPasswordReset", method = RequestMethod.POST)
    public ResponseEntity<?> requestPasswordReset(@PathParam("email") String email) {
        authService.requestPasswordReset(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Secured("ROLE_USER")
    @RequestMapping(value = "/auth/hello", method = RequestMethod.GET)
    public ResponseEntity<String> hello() {
        return new ResponseEntity<String>("hello", HttpStatus.OK);
    }

}
