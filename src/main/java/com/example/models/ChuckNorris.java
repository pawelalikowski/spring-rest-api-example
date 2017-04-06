package com.example.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class ChuckNorris implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Integer factNo;

    @NotNull
    private String fact;

    public ChuckNorris() {}

    public ChuckNorris(Integer factNo, String fact) {
        this.factNo = factNo;
        this.fact = fact;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFactNo() {
        return factNo;
    }

    public void setFactNo(Integer factNo) {
        this.factNo = factNo;
    }

    public String getFact() {
        return fact;
    }

    public void setFact(String fact) {
        this.fact = fact;
    }
}
