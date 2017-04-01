package com.example.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@Configuration
@EnableAuthorizationServer
@EnableResourceServer
@RestController
public class AuthorizationServer {

    @RequestMapping({ "/user", "/me" })
    public Map<String, String> user(Principal principal) {
        Map<String, String> map = new LinkedHashMap<>();
        HashMap<String, String> userDetails = (HashMap) ((OAuth2Authentication) principal).getUserAuthentication().getDetails();
        map.put("name", principal.getName());
        map.put("email", userDetails.get("email"));
        return map;
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

