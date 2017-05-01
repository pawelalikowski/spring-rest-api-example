package com.example;

import com.example.dictionaries.TokenStatus;
import com.example.models.ChuckNorris;
import com.example.models.User;
import com.example.repositories.ChuckNorrisRepository;
import com.example.repositories.TokenStatusRepository;
import com.example.repositories.UserRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class OnBoot implements ApplicationRunner {

    private final Faker faker = new Faker(new Locale("en"));
    private final ChuckNorrisRepository chuckNorrisRepository;
    private final UserRepository userRepository;
    private final TokenStatusRepository tokenStatusRepository;

    @Autowired
    public OnBoot(
            UserRepository userRepository,
            ChuckNorrisRepository chuckNorrisRepository,
            TokenStatusRepository tokenStatusRepository
    ) {
        this.userRepository = userRepository;
        this.chuckNorrisRepository = chuckNorrisRepository;
        this.tokenStatusRepository = tokenStatusRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.seedDatabase();
        this.seedDictionaries();
    }

    private void seedDatabase() {
        userRepository.save(new User("password", "Chuck", "Norris", "chuck@norris.com"));
        userRepository.save(new User("admin", "Admin", "User", "password"));
        for(int i = 1; i <= 101; i++) {
            Name name = faker.name();
            userRepository.save(new User("password", name.firstName(), name.lastName(), name.username()+"@gmail.com"));
        }
        for(int i = 1; i <= 101; i++) chuckNorrisRepository.save(new ChuckNorris(i, faker.chuckNorris().fact()));

    }

    private void seedDictionaries() throws IllegalAccessException {
        tokenStatusRepository.save(getList(new TokenStatus()));
    }

    private <E> List<E> getList(E o) throws IllegalAccessException {
        List<E> list = new ArrayList<>();
        for(Field element : o.getClass().getFields()) {
            list.add((E) element.get(o));
        }
        return list;
    }


}
