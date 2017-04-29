package com.example.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    private Boolean isActive = false;

    private Boolean isExpired = false;

    private Boolean isBlocked = false;

    private Integer failedAuthorizations = 0;

    private Date lastSuccessfulLogin;

    @OneToMany(mappedBy = "user")
    private Set<ConfirmationToken> tokens;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String password, String firstName, String lastName, String email) {
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public Boolean getBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public Integer getFailedAuthorizations() {
        return failedAuthorizations;
    }

    public void setFailedAuthorizations(Integer failedAuthorizations) {
        this.failedAuthorizations = failedAuthorizations;
    }

    public Date getLastSuccessfulLogin() {
        return lastSuccessfulLogin;
    }

    public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
        this.lastSuccessfulLogin = lastSuccessfulLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (isActive != null ? !isActive.equals(user.isActive) : user.isActive != null) return false;
        if (isExpired != null ? !isExpired.equals(user.isExpired) : user.isExpired != null) return false;
        if (isBlocked != null ? !isBlocked.equals(user.isBlocked) : user.isBlocked != null) return false;
        if (failedAuthorizations != null ? !failedAuthorizations.equals(user.failedAuthorizations) : user.failedAuthorizations != null)
            return false;
        if (lastSuccessfulLogin != null ? !lastSuccessfulLogin.equals(user.lastSuccessfulLogin) : user.lastSuccessfulLogin != null)
            return false;
        return tokens != null ? tokens.equals(user.tokens) : user.tokens == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (isActive != null ? isActive.hashCode() : 0);
        result = 31 * result + (isExpired != null ? isExpired.hashCode() : 0);
        result = 31 * result + (isBlocked != null ? isBlocked.hashCode() : 0);
        result = 31 * result + (failedAuthorizations != null ? failedAuthorizations.hashCode() : 0);
        result = 31 * result + (lastSuccessfulLogin != null ? lastSuccessfulLogin.hashCode() : 0);
        result = 31 * result + (tokens != null ? tokens.hashCode() : 0);
        return result;
    }
}
