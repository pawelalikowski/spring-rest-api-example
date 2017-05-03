package com.example.conf;

import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.RestController;


@SpringBootConfiguration
@EnableAuthorizationServer
@EnableResourceServer
@RestController
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthorizationServer(UserRepository repository, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.authenticationManager = authenticationManager;
    }

    @Bean
    public JwtTokenStore jwtStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtConverter() {
        return new JwtAccessTokenConverter();
    }

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {

            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                OAuth2Exception exception = (OAuth2Exception) e;
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                OAuth2Exception body = responseEntity.getBody();
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());

                HttpStatus status = HttpStatus.FORBIDDEN;
                body.addAdditionalInformation("status", String.valueOf(status.value()));
                body.addAdditionalInformation("statusText", status.getReasonPhrase());
                body.addAdditionalInformation("code", exception.getOAuth2ErrorCode());
                body.addAdditionalInformation("message", exception.getMessage());
                headers.setContentType(MediaType.APPLICATION_JSON);

                return new ResponseEntity<>(body, headers, status);
            }
        };
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .pathMapping("/oauth/authorize", "/authorize")
                .pathMapping("/oauth/error", "/error")
                .pathMapping("/oauth/token", "/token")
                .exceptionTranslator(webResponseExceptionTranslator())
                .tokenStore(jwtStore(jwtConverter()))
                .accessTokenConverter(jwtConverter())
                .authenticationManager(this.authenticationManager)
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("acme")
                .secret("acmesecret")
                .authorizedGrantTypes("authorization_code",
                        "password", "client_credentials", "implicit", "refresh_token")
                .scopes("read", "write")
        ;
     }
}


