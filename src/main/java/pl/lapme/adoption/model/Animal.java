package pl.lapme.adoption.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String race;
    private int age;
    private String gender;
    private String description;

    private String sellingLogin;

    @Lob
    @Column
    private byte[] image;

    @ManyToMany
    Set<User> user;

    public Animal(String name) {
        this.name = name;
    }
}
