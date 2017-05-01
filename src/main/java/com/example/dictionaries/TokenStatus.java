package com.example.dictionaries;

import com.example.models.ConfirmationToken;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class TokenStatus {
    public static final TokenStatus PENDING = new TokenStatus(1, "PENDING", "Pending", 1, null);
    public static final TokenStatus COMPLETED = new TokenStatus(2, "COMPLETED", "Completed", 2, null);
    public static final TokenStatus EXPIRED = new TokenStatus(3, "EXPIRED", "Expired", 3, null);

    @Id
    private Integer id;

    private Integer sequence;

    private String key;

    private String value;

    private String description;

    @OneToMany(mappedBy = "tokenStatus")
    private Set<ConfirmationToken> tokens;

    public TokenStatus() {}

    private TokenStatus(Integer id, String key, String value, Integer sequence, String description) {
        this.sequence = sequence;
        this.key = key;
        this.value = value;
        this.description = description;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenStatus that = (TokenStatus) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (sequence != null ? !sequence.equals(that.sequence) : that.sequence != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return description != null ? description.equals(that.description) : that.description == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (sequence != null ? sequence.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
