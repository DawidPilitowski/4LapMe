package pl.lapme.adoption.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String login;
    private String name;
    private String surname;
    private String address;
    private int phoneNumber;
    private String password;
    private int accountNumber;

    private int nip;

    private String email;
    private int privilege;

    @Lob
    @Column
    private byte[] avatar;

    @ManyToMany
    Set<Animal> animalList;

    public User(String login, String password, int privilege) {
        this.login = login;
        this.password = password;
        this.privilege = privilege;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        if (this.getPrivilege() > 2) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (this.getPrivilege() > 1) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_BREEDING"));
        }
        if (this.getPrivilege() > 1) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SHELTER"));
        }
        if (this.getPrivilege() > 0) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (this.getPrivilege() == 0) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_NONE"));
        }

        return grantedAuthorities;
    }
}
