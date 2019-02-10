package pl.lapme.adoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lapme.adoption.model.AppUserRole;
import pl.lapme.adoption.model.ApplicationEvent;
import pl.lapme.adoption.model.AppUser;

import pl.lapme.adoption.model.dto.*;
import pl.lapme.adoption.repository.UserRepository;
import pl.lapme.adoption.repository.UserRoleRopository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRoleRopository userRoleRopository;

    @Autowired
    private LoginService loginService;

    public Iterable<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

//    public void updateUserDTO(EditProfileUserDTO modifyUserDTO) {
//        String login = modifyUserDTO.getLogin();
//
//        Optional<AppUser> optionalAppUser = userRepository.findUserByLogin(login);
//
//        if (optionalAppUser.isPresent()) {
//            AppUser user = optionalAppUser.get();
//            user.setAddress(modifyUserDTO.getAddress());
//            user.setName(modifyUserDTO.getName());
//            user.setEmail(modifyUserDTO.getEmail());
//            user.setSurname(modifyUserDTO.getSurname());
//            user.setPhoneNumber(modifyUserDTO.getPhoneNumber());
//            userRepository.save(user);
//        }
//    }
//
//    public Optional<AppUser> findById(Long id) {
//        return userRepository.findById(id);
//    }
//
//    public Optional<AppUser> findByLogin(String login) {
//        return userRepository.findUserByLogin(login);
//    }
//
//    public Optional<AppUser> findByPassword(String code) {
//        return userRepository.findByPassword(code);
//    }

//    public boolean makeUser(Long id) {
//        Optional<AppUser> optionalUser = userRepository.findById(id);
//
//        if (optionalUser.isPresent()) {
//            AppUser appUser = optionalUser.get();
//            appUser.setRoles();
//            userRepository.save(appUser);
//            return true;
//        }
//        return false;
//    }

//    public AppUser getLoggedInUser() { //w tym miejscu pytanie czy nie powinno być zamiast name login?
//        String login = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findOneByLogin(login);
//    }
//
//    public AppUser findUserByLogin(String login) {
//        return userRepository.findByLogin(login);
//    }
//
//    public AppUser findOneId(Long id) {
//        return userRepository.findOneById(id);
//    }
//
////    public void makeUserNone(EditEmailUserDTO newUserDto) {
////        String login = newUserDto.getLogin();
////        Optional<AppUser> optionalAppUser = userRepository.findUserByLogin(login);
////        if (optionalAppUser.isPresent()) {
////            AppUser user = optionalAppUser.get();
////            user.setPrivilege(0);
////            userRepository.save(user);
////        }
////
////    }

    public void logAuthenticationAttempt(String name, ApplicationEvent applicationEvent) {

    }

    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
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

    public Optional<AppUser> register(RegisterUserDTO newUserDto) {
        Optional<AppUser> optionalAppUser = userRepository.findByUsername(newUserDto.getUsername());
        if (optionalAppUser.isPresent()) {
            return Optional.empty();
        }
        AppUser newUser = new AppUser();
        newUser.setEmail(newUserDto.getEmail().trim());
        newUser.setPassword(bCryptPasswordEncoder.encode(newUserDto.getPassword()));
        newUser.setUsername(newUserDto.getUsername().trim());

        newUser.setRoles(new HashSet<>(Arrays.asList(userRoleRopository.findByName("ROLE_USER").get())));

        userRepository.save(newUser);
        return Optional.of(newUser);
    }

    public void modifiedUser(ChangeAppUserSettingsDto dto, Long id) {
        Optional<AppUser> appUserOptional = userRepository.findById(id);
        if (appUserOptional.isPresent()) {
            AppUser appUser = appUserOptional.get();
            if (loginService.isAdmin()) {
                appUser.setUsername(dto.getUsername());
                appUser.setEmail(dto.getEmail());
                userRepository.save(appUser);
            }
        }
    }

    public void modifiedUserPassword(ChangeAppUserSettingsDto dto, Long id) {
        Optional<AppUser> optionalAppUser = userRepository.findById(id);
        if (optionalAppUser.isPresent()) {
            AppUser appUser = optionalAppUser.get();
            if (loginService.isAdmin()) {
                appUser.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
                userRepository.save(appUser);
            }
        }
    }

    public void modifiedUserRole(ModifiedRoleDto modifiedRoleDto, Long id) {
        Optional<AppUser> optionalAppUser = userRepository.findById(id);

        if (optionalAppUser.isPresent()) {
            AppUser newUser = optionalAppUser.get();
            if (loginService.isAdmin()) {
                newUser.setRoles(new HashSet<>(Arrays.asList(userRoleRopository.findByName("ROLE_USER").get())));

                if (modifiedRoleDto.getRoles().getRole_admin() != null) {
                    Optional<AppUserRole> appUserRole = userRoleRopository.findByName("ROLE_ADMIN");
                    appUserRole.ifPresent(newRole -> newUser.getRoles().add(newRole));
                }
                if (modifiedRoleDto.getRoles().getRole_shelter() != null) {
                    Optional<AppUserRole> appUserRole = userRoleRopository.findByName("ROLE_SHELTER");
                    appUserRole.ifPresent(newRole -> newUser.getRoles().add(newRole));
                }
                if (modifiedRoleDto.getRoles().getRole_breeder() != null) {
                    Optional<AppUserRole> appUserRole = userRoleRopository.findByName("ROLE_BREEDER");
                    appUserRole.ifPresent(newRole -> newUser.getRoles().add(newRole));
                }
                userRepository.save(newUser);
            }
        }


    }

    public Optional<AppUser> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<AppUser> addUser(UserModifyDto newUserDto) {
        Optional<AppUser> optionalAppUser = userRepository.findByUsername(newUserDto.getUsername());
        if (optionalAppUser.isPresent()) {
            return Optional.empty();
        }

        AppUser appUser = new AppUser();
        if (newUserDto.getName().isEmpty()) {
            appUser.setName(null);
        } else {
            appUser.setName(newUserDto.getName());
        }
        appUser.setEmail(newUserDto.getEmail().trim());
        appUser.setPassword(bCryptPasswordEncoder.encode(newUserDto.getPassword()));
        appUser.setUsername(newUserDto.getUsername().trim());
        if (newUserDto.getSurname().isEmpty()) {
            appUser.setSurname(null);
        } else {
            appUser.setSurname(newUserDto.getSurname());

        }
        appUser.setRoles(new HashSet<>(Arrays.asList(userRoleRopository.findByName("ROLE_USER").get())));

        if (newUserDto.getRoles().getRole_admin() != null) {
            Optional<AppUserRole> userRole = userRoleRopository.findByName("ROLE_ADMIN");
            if (userRole.isPresent()) {
                appUser.getRoles().add(userRole.get());
            }
        }
        if (newUserDto.getRoles().getRole_shelter() != null) {
            Optional<AppUserRole> userRole = userRoleRopository.findByName("ROLE_SHELTER");
            if (userRole.isPresent()) {
                appUser.getRoles().add(userRole.get());
            }
        }
        if (newUserDto.getRoles().getRole_breeder() != null) {
            Optional<AppUserRole> userRole = userRoleRopository.findByName("ROLE_BREEDER");
            if (userRole.isPresent()) {
                appUser.getRoles().add(userRole.get());
            }
        }
        appUser = userRepository.save(appUser);
        return Optional.of(appUser);
    }
}
