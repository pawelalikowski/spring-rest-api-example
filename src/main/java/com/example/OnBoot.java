package com.example;

import com.example.models.ChuckNorris;
import com.example.models.User;
import com.example.repositories.ChuckNorrisRepository;
import com.example.repositories.UserRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class OnBoot implements ApplicationRunner {

    private final ChuckNorrisRepository chuckNorrisRepository;
    private final UserRepository userRepository;
    private final Faker faker = new Faker(new Locale("en"));

    @Autowired
    public OnBoot(UserRepository userRepository, ChuckNorrisRepository chuckNorrisRepository) {
        this.userRepository = userRepository;
        this.chuckNorrisRepository = chuckNorrisRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.seedDatabase();
    }

    private void seedDatabase() {
        userRepository.save(new User("user", "password", "Chuck", "Norris", "chuck@norris.com"));
        userRepository.save(new User("admin", "password"));
        for(int i = 1; i <= 101; i++) {
            Name name = faker.name();
            userRepository.save(new User(name.username(), "password", name.firstName(), name.lastName(), name.username()+"@gmail.com"));
        }
        for(int i = 1; i <= 101; i++) chuckNorrisRepository.save(new ChuckNorris(i, faker.chuckNorris().fact()));

    }

}
