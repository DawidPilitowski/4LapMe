package pl.lapme.adoption.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lapme.adoption.model.ApplicationEvent;
import pl.lapme.adoption.model.AppUser;

import pl.lapme.adoption.model.dto.ChangeAppUserSettingsDto;
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

    public Iterable<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

//    public boolean registerUser(RegisterUserDTO dto) {
//
//        Optional<AppUser> userByUsername = userRepository.findUserByLogin(dto.getLogin());
//        if (userByUsername.isPresent()) {
//            return false;
//        }
//        AppUser newUser = new AppUser(dto.getLogin(), bCryptPasswordEncoder.encode(dto.getPassword()), 0);
//        newUser.setEmail(dto.getEmail());
//        userRepository.save(newUser);
//        return true;
//    }

    public void updateUserDTO(EditProfileUserDTO modifyUserDTO) {
        String login = modifyUserDTO.getLogin();

        Optional<AppUser> optionalAppUser = userRepository.findUserByLogin(login);

        if (optionalAppUser.isPresent()) {
            AppUser user = optionalAppUser.get();
            user.setAddress(modifyUserDTO.getAddress());
            user.setName(modifyUserDTO.getName());
            user.setEmail(modifyUserDTO.getEmail());
            user.setSurname(modifyUserDTO.getSurname());
            user.setPhoneNumber(modifyUserDTO.getPhoneNumber());
            userRepository.save(user);
        }
    }

    public Optional<AppUser> findByid(Long id) {
        return userRepository.findById(id);
    }

    public Optional<AppUser> findByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    public Optional<AppUser> findByPassword(String code) {
        return userRepository.findByPassword(code);
    }

//    public boolean makeUser(Long id) {
//        Optional<AppUser> optionalUser = userRepository.findById(id);
//
//        if (optionalUser.isPresent()) {
//            AppUser appUser = optionalUser.get();
//            appUser.setPrivilege(1);
//            userRepository.save(appUser);
//            return true;
//        }
//        return false;
//    }

    public AppUser getLoggedInUser() { //w tym miejscu pytanie czy nie powinno być zamiast name login?
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findOneByLogin(login);
    }

    public AppUser findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public AppUser findOneId(Long id) {
        return userRepository.findOneById(id);
    }

//    public void makeUserNone(EditEmailUserDTO newUserDto) {
//        String login = newUserDto.getLogin();
//        Optional<AppUser> optionalAppUser = userRepository.findUserByLogin(login);
//        if (optionalAppUser.isPresent()) {
//            AppUser user = optionalAppUser.get();
//            user.setPrivilege(0);
//            userRepository.save(user);
//        }
//
//    }

    public void logAuthenticationAttempt(String name, ApplicationEvent applicationEvent) {

    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername();
    }

    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public AppUser changeSettings(ChangeAppUserSettingsDto changeAppUserSettingsDto, AppUser user) {
        if (changeAppUserSettingsDto.getName() != null) {
            user.setName(changeAppUserSettingsDto.getName());
        }
        if (changeAppUserSettingsDto.getSurname() != null) {
            user.setSurname(changeAppUserSettingsDto.getSurname());
        }
        if (changeAppUserSettingsDto.getEmail() != null) {
            user.setEmail(changeAppUserSettingsDto.getEmail());
        }
        return userRepository.save(user);
    }

    public boolean ifOldAndNewPasswordAreTheSame(Long id, ChangeAppUserSettingsDto changeAppUserSettingsDto) {
        Optional<AppUser> optionalAppUser = getUserById(id);
        if (bCryptPasswordEncoder.matches(changeAppUserSettingsDto.getOldPassword(), optionalAppUser.get().getPassword())) {
            return true;
        }
        return false;
    }

    public AppUser changePasswordSettings(ChangeAppUserSettingsDto changeAppUserSettingsDto, AppUser currentUser) {
        if (changeAppUserSettingsDto.getPassword() != null) {
            currentUser.setPassword(bCryptPasswordEncoder.encode(changeAppUserSettingsDto.getPassword()));
        }
        return userRepository.save(currentUser);
    }
}
