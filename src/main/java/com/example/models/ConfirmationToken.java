package com.example.models;

import com.example.dictionaries.TokenStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private Date created;

    @ManyToOne
    @JoinColumn(name = "token_status_id")
    private TokenStatus tokenStatus;

    public ConfirmationToken() {
    }

    public ConfirmationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public TokenStatus getTokenStatus() {
        return tokenStatus;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.tokenStatus = tokenStatus;
    }
}
