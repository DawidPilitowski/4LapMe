package pl.lapme.adoption.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lapme.adoption.model.User;

import pl.lapme.adoption.model.dto.EditEmailUserDTO;
import pl.lapme.adoption.model.dto.EditProfileUserDTO;
import pl.lapme.adoption.model.dto.RegisterUserDTO;
import pl.lapme.adoption.repository.UserRepository;

import java.util.Optional;

@Service

public class UserService {


    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean registerUser(RegisterUserDTO dto) {

        Optional<User> userByUsername = userRepository.findUserByLogin(dto.getLogin());
        if (userByUsername.isPresent()) {
            return false;
        }
        User newUser = new User(dto.getLogin(), bCryptPasswordEncoder.encode(dto.getPassword()), 0);
        newUser.setEmail(dto.getEmail());
        userRepository.save(newUser);
        return true;
    }

    public void updateUserDTO(EditProfileUserDTO modifyUserDTO) {
        String login = modifyUserDTO.getLogin();

        Optional<User> optionalAppUser = userRepository.findUserByLogin(login);

        if (optionalAppUser.isPresent()) {
            User user = optionalAppUser.get();
            user.setAddress(modifyUserDTO.getAddress());
            user.setName(modifyUserDTO.getName());
            user.setEmail(modifyUserDTO.getEmail());
            user.setSurname(modifyUserDTO.getSurname());
            user.setPhoneNumber(modifyUserDTO.getPhoneNumber());
            userRepository.save(user);
        }
    }

    public Optional<User> findByid(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public Optional<User> findByPassword(String code) {
        return userRepository.findByPassword(code);
    }

    public boolean makeUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User appUser = optionalUser.get();
            appUser.setPrivilege(1);
            userRepository.save(appUser);
            return true;
        }
        return false;
    }

    public User getLoggedInUser() { //w tym miejscu pytanie czy nie powinno być zamiast name login?
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findOneByLogin(login);
    }

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public User findOneId(Long id) {
        return userRepository.findOneById(id);
    }

    public void makeUserNone(EditEmailUserDTO newUserDto) {
        String login = newUserDto.getLogin();
        Optional<User> optionalAppUser = userRepository.findUserByLogin(login);
        if (optionalAppUser.isPresent()) {
            User user = optionalAppUser.get();
            user.setPrivilege(0);
            userRepository.save(user);
        }

    }
}
