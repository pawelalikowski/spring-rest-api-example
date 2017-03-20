package com.example;

import com.example.models.Book;
import com.example.models.User;
import com.example.repositories.BookRepository;
import com.example.repositories.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class OnBoot implements ApplicationRunner {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public OnBoot(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userRepository.save(new User("user", "password"));
        userRepository.save(new User("admin", "password"));
        bookRepository.save(new Book("Design Patterns", "GOF"));
        bookRepository.save(new Book("Patterns of Enterprise Application Architecture", "Martin Fowler"));
    }
}
