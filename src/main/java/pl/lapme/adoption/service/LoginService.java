package pl.lapme.adoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.repository.ApplicationEventRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoginService implements UserDetailsService {
    @Autowired
    private ApplicationEventRepository applicationEventRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;
    private boolean admin;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();

            //applicationEventRepository.save(EventFactory.loginSuccess(username, request.getRemoteAddr()));

            return User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(extractRoles(user))
                    .build();
        }

//        applicationEventRepository.saveAndFlush(EventFactory.loginFailed(username, request.getRemoteAddr()));
        throw new UsernameNotFoundException("User not found by name: " + username);
    }

    private String[] extractRoles(AppUser user) {
        List<String> roles = user.getRoles().stream().map(role -> role.getName().replace("ROLE_", "")).collect(Collectors.toList());
        String[] rolesArray = new String[roles.size()];
        rolesArray = roles.toArray(rolesArray);

        return rolesArray;
    }

    public Optional<AppUser> getLoggedInUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null ||
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            // nie jesteśmy zalogowani
            return Optional.empty();
        }

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userService.findByUsername(user.getUsername());
            // jesteśmy zalogowani, zwracamy user'a
        }

        return Optional.empty();
    }

    public boolean isAdmin() {
        return getLoggedInUser().get().getRoles().stream().anyMatch(
                employeeRole -> employeeRole.getName().equals("ROLE_ADMIN"));
    }
}
