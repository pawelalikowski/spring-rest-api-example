package com.example.models;

import com.example.dictionaries.TokenStatus;
import com.example.dictionaries.TokenType;

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
    private TokenStatus tokenStatus = TokenStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    public ConfirmationToken() {
    }

    public ConfirmationToken(String token, User user, TokenType type, Date created) {
        this.token = token;
        this.user = user;
        this.created = created;
        this.tokenType = type;
    }

    public ConfirmationToken(String token, User user, TokenType type, Date created, TokenStatus tokenStatus) {
        this.token = token;
        this.user = user;
        this.created = created;
        this.tokenStatus = tokenStatus;
        this.tokenType = type;
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

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfirmationToken that = (ConfirmationToken) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (created != null ? !created.equals(that.created) : that.created != null) return false;
        return tokenStatus != null ? tokenStatus.equals(that.tokenStatus) : that.tokenStatus == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (tokenStatus != null ? tokenStatus.hashCode() : 0);
        return result;
    }

}
