package app.com.bloom.app.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "users")
@Data
public class User {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    private String id;
    private String name;
    private String email;
    private Long number;


    User(String name, String email, Long number) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
    }

    User() {}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }



}
