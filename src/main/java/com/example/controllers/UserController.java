package com.example.controllers;

import com.example.models.User;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RequestMapping("users")
@RepositoryRestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/index")
    public @ResponseBody ResponseEntity<?> getProducers() {
        List<User> users = (List<User>) userRepository.findAll();

        //
        // do some intermediate processing, logging, etc. with the users
        //

        Resources<User> resources = new Resources<>(users);

        resources.add(linkTo(methodOn(UserController.class).getProducers()).withSelfRel());

        // add other links as needed

        return ResponseEntity.ok(resources);
    }

}
