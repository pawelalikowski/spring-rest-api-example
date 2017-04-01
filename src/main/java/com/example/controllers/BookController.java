package com.example.controllers;

import com.example.models.Book;
import com.example.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("books")
@RepositoryRestController
public class BookController {

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RequestMapping( value = "", method = RequestMethod.GET)
    public ResponseEntity<List<Book>> index() {
        return new ResponseEntity<>((List<Book>) bookRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Book> show(@PathVariable("id") Long id) {
        return new ResponseEntity<>(bookRepository.findOne(id), HttpStatus.OK);
    }

    @RequestMapping( value = "", method = RequestMethod.POST)
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book book) {
        return bookRepository.findById(id)
                .map(persistedBook -> {
                    book.setId(id);
                    bookRepository.save(book);
                    return new ResponseEntity<>(book, HttpStatus.CREATED);
                }).orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @RequestMapping( value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        bookRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
