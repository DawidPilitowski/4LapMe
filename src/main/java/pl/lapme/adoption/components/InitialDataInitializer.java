package pl.lapme.adoption.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.model.AppUserRole;
import pl.lapme.adoption.repository.UserRepository;
import pl.lapme.adoption.repository.UserRoleRopository;

import java.util.*;

@Component
public class InitialDataInitializer implements  ApplicationListener<ContextRefreshedEvent> {
   @Autowired
   private UserRoleRopository userRoleRopository;

   @Autowired
   private UserRepository userRepository;
   @Autowired
   private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        addRole("ROLE_ADMIN");
        addRole("ROLE_SHELTER_USER");
        addRole("ROLE_BREEDER_USER");
        addRole("ROLE_USER");

        addUser("admin", "admin", "ROLE_ADMIN", "ROLE_USER");
        addUser("user", "user", "ROLE_USER");
    }

    @Transactional
    public void addRole(String name) {
        Optional<AppUserRole> userRoleOptional = userRoleRopository.findByName(name);
        if (!userRoleOptional.isPresent()) {
            userRoleRopository.save(new AppUserRole(null, name));
        }
    }

    @Transactional
    public void addUser(String username, String password, String... roles) {
        Optional<AppUser> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            List<AppUserRole> rolesList = new ArrayList<>();
            for (String role : roles) {
                Optional<AppUserRole> userRoleOptional = userRoleRopository.findByName(role);
                userRoleOptional.ifPresent(rolesList::add);
            }
            userRepository.save(new AppUser(null,
                    username,
                    null,
                    null,
                    null,
                    1,
                    passwordEncoder.encode(password),
                    1,
                    1,
                    null,
                    new HashSet<>(rolesList),
                    null,
                    new HashSet<>(),
                    new ArrayList<>()
                    ));

        }
    }
}
