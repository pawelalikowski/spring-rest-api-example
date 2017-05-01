package com.example.controllers;

import com.example.exceptions.InvalidRequestException;
import com.example.models.PasswordResetRequest;
import com.example.models.User;
import com.example.services.AuthService;
import com.example.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
public class AuthController {
    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new InvalidRequestException("Invalid user", bindingResult);
        authService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/confirm", method = RequestMethod.GET)
    public ResponseEntity<?> confirm(@PathParam("token") String token, @PathParam("email") String email) {
        authService.confirm(email, token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/auth/requestPasswordReset", method = RequestMethod.GET)
    public ResponseEntity<?> requestPasswordReset(@PathParam("email") String email) {
        authService.requestPasswordReset(email);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/auth/passwordReset", method = RequestMethod.POST)
    public ResponseEntity<?> passwordReset(@RequestBody @Valid PasswordResetRequest passwordResetRequest,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new InvalidRequestException("Invalid request", bindingResult);
        authService.passwordReset(passwordResetRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
