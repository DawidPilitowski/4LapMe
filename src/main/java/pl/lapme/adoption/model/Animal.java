package pl.lapme.adoption.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    private boolean awaitingApproval;
    private String sellingLogin;

    @Lob
    @Column
    private byte[] image;

    @ManyToMany
    private AppUser user;
//    private List<Category> category;

}
