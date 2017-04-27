package com.example.conf;

import com.example.models.User;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;


@SpringBootConfiguration
@EnableAuthorizationServer
@EnableResourceServer
@RestController
public class AuthorizationServer {

    private final UserRepository repository;

    @Autowired
    public AuthorizationServer(UserRepository repository) {
        this.repository = repository;
    }


    @RequestMapping({ "/user", "/me" })
    public ResponseEntity<User> user(Principal principal) {
        String username = (String) ((OAuth2Authentication) principal).getUserAuthentication().getPrincipal();
        Optional<User> user = repository.findByEmail(username);
        return user.map(user1 -> ResponseEntity.ok().body(user1)).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Bean
    public JwtTokenStore jwtStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtConverter() {
        return new JwtAccessTokenConverter();
    }
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.allowFormAuthenticationForClients();
//    }
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//                .withClient("acme")
//                .secret("acmesecret")
//                .scopes("read,write")
//                .authorizedGrantTypes("password");
//    }
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        super.configure(endpoints);
//    }
}

