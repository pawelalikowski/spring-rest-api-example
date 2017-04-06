package com.example.controllers;

import com.example.models.ChuckNorris;
import com.example.repositories.ChuckNorrisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("chuckNorris")
@RepositoryRestController
public class ChuckNorrisController {

    private ChuckNorrisRepository chuckNorrisRepository;

    @Autowired
    public ChuckNorrisController(ChuckNorrisRepository chuckNorrisRepository) {
        this.chuckNorrisRepository = chuckNorrisRepository;
    }

    @RequestMapping( value = "", method = RequestMethod.GET)
    public ResponseEntity<List<ChuckNorris>> index() {
        return new ResponseEntity<>((List<ChuckNorris>) chuckNorrisRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ChuckNorris> show(@PathVariable("id") Long id) {
        return new ResponseEntity<>(chuckNorrisRepository.findOne(id), HttpStatus.OK);
    }

    @RequestMapping( value = "", method = RequestMethod.POST)
    public ResponseEntity<ChuckNorris> create(@RequestBody ChuckNorris chuckNorris) {
        return new ResponseEntity<>(chuckNorrisRepository.save(chuckNorris), HttpStatus.CREATED);
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ChuckNorris> update(@PathVariable Long id, @RequestBody ChuckNorris chuckNorris) {
        return chuckNorrisRepository.findById(id)
                .map(persistedChuckNorris -> {
                    chuckNorris.setId(id);
                    chuckNorrisRepository.save(chuckNorris);
                    return new ResponseEntity<>(chuckNorris, HttpStatus.CREATED);
                }).orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        chuckNorrisRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
