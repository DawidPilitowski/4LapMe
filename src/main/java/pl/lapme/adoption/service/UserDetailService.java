package pl.lapme.adoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import pl.lapme.adoption.repository.UserRepository;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<pl.lapme.adoption.model.User> userFounder = userRepository.findUserByLogin(login);
        if (userFounder.isPresent()) {
            pl.lapme.adoption.model.User appUser = userFounder.get();
            return new User(
                    appUser.getLogin(),
                    appUser.getPassword(),
                    appUser.getAuthorities());
        }
        throw new UsernameNotFoundException("User not found");
    }
}
