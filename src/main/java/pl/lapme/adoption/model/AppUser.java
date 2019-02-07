package pl.lapme.adoption.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    private String username;
    private String name;
    private String surname;
    private String address;
    private int phoneNumber;
    private String password;
    private int accountNumber;

    private int nip;
    @Email
    private String email;
    @ManyToMany
    private Set<AppUserRole> roles;
    @Lob
    @Column
    private byte[] avatar;

    @ManyToMany
    Set<Animal> animalList;

    @OneToMany
    private List<ApplicationEvent> applicationEvents;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", accountNumber=" + accountNumber +
                ", nip=" + nip +
                ", email='" + email + '\'' +
                '}';
    }
}