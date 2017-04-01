package com.example.controllers;

import com.example.Services.UserService;
import com.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping("users")
@RepositoryRestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/index")
    public @ResponseBody ResponseEntity<?> getProducers() {
        List<User> users = (List<User>) userService.findAll();

        //
        // do some intermediate processing, logging, etc. with the users
        //

        Resources<User> resources = new Resources<>(users);

        resources.add(linkTo(methodOn(UserController.class).getProducers()).withSelfRel());

        // add other links as needed

        return ResponseEntity.ok(resources);
    }
}
