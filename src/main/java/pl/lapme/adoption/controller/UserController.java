package pl.lapme.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.model.dto.ChangeAppUserSettingsDto;
import pl.lapme.adoption.model.dto.EditEmailUserDTO;
import pl.lapme.adoption.model.dto.EditProfileUserDTO;
import pl.lapme.adoption.model.dto.RegisterUserDTO;
import pl.lapme.adoption.service.EmailSender;
import pl.lapme.adoption.service.LoginService;
import pl.lapme.adoption.service.UserRoleService;
import pl.lapme.adoption.service.UserService;

import java.util.Base64;
import java.util.Optional;

@Controller
@RequestMapping("/user/")
@PreAuthorize("hasRole('ROLE_USER')")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserRoleService userRoleService;

    @ModelAttribute("currentUser")
    public AppUser getCurrentUser() {
        return loginService.getLoggedInUser().get();
    }

    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/home")
    public String homePage1() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }

    @GetMapping("/register")
    public String register(Model model) {
        RegisterUserDTO dto = new RegisterUserDTO();
        dto.setRoles(userRoleService.getRoleToSelect());
        model.addAttribute("user_dto", dto);
        return "user/register";
    }

    @PostMapping("/register")
    public String addUser(Model model, RegisterUserDTO newUserDto) {

        if (!newUserDto.getPassword().equals(newUserDto.getConfirmPassword())) {
            model.addAttribute("newUser", new RegisterUserDTO());
            model.addAttribute("failMsg", "Hasła są różne!");
            return "/register";
        }
        if (newUserDto.getEmail().isEmpty()) {
            return "redirect:/register?error_message=Email is empty";
        }
        if (newUserDto.getUsername().isEmpty()) {
            return "redirect:/register?error_message=Login is empty";
        }
        if (newUserDto.getPassword().isEmpty() || newUserDto.getConfirmPassword().isEmpty()) {
            return "redirect:/register?error_message=Password or Confirm Password is empty";
        }

        Optional<AppUser> appUserOptional = userService.register(newUserDto);

        if (appUserOptional.isPresent()) {
            Context context = new Context();
            context.setVariable("user", userService.findByLogin(newUserDto.getUsername()));

            String welcomeMail = templateEngine.process("welcomeMail", context);
            emailSender.sendEmail(newUserDto.getEmail(), "Witamy w 4LapMe!", welcomeMail);
            return "login";
        }
        return "login";
    }

//    @GetMapping("/activation")
//    public String activateUser(@RequestParam(name = "code") String code) {
//        Optional<AppUser> userOptional = userService.findByPassword(code);
//        if (userOptional.isPresent()) {
//            System.out.println("found user");
//            AppUser appUser = userOptional.get();
//            userService.makeUser(appUser.getId());
//        }
//        return "redirect:/login";
//    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser userByLogin = userService.findUserByLogin(userLogin);
        if (userByLogin != null) {
            model.addAttribute("profile", userByLogin);

            if (userByLogin.getAvatar() != null) {
                model.addAttribute("image", new String(Base64.getEncoder().encode(userByLogin.getAvatar())));
            } else {
                model.addAttribute("image", "");
            }
            return "profile";
        }
        return "redirect:/error";
    }

//    @GetMapping("/edit")
//    public String editProfile(Model model) {
//        AppUser loggedInUser = userService.getLoggedInUser();
//
//        EditProfileUserDTO editProfileUserDTO = new EditProfileUserDTO();
//        editProfileUserDTO.setLogin(loggedInUser.getLogin());
//        editProfileUserDTO.setEmail(loggedInUser.getEmail());
//        editProfileUserDTO.setName(loggedInUser.getName());
//        editProfileUserDTO.setSurname(loggedInUser.getSurname());
//        editProfileUserDTO.setAddress(loggedInUser.getAddress());
//        model.addAttribute("userDTO", editProfileUserDTO);
//        return "editUser";
//    }

    @PostMapping(value = "/edit")
    public String editPost(EditEmailUserDTO newUserDto, EditProfileUserDTO editProfileUserDTO) {
        String registerUserDTO = userService.getLoggedInUser().getEmail();

        if (!registerUserDTO.equals(editProfileUserDTO.getEmail())) {
            Context context = new Context();
            context.setVariable("user", userService.findByLogin(newUserDto.getLogin()));

            String welcomeMail = templateEngine.process("welcomeMail", context);
            emailSender.sendEmail(newUserDto.getEmail(), "Witamy ponownie w 4LapMe!", welcomeMail);
//            userService.makeUserNone(newUserDto);
            userService.updateUserDTO(editProfileUserDTO);

            return "login";
        }
        userService.updateUserDTO(editProfileUserDTO);
//        String newMail = editProfileUserDTO.getEmail();
//        System.out.println(newMail);
        return "redirect:/profile";
    }

    @GetMapping("/settings")
    public String employeeSettings(Model model, ChangeAppUserSettingsDto user,
                                   @RequestParam(name = "message", required = false) String message,
                                   @RequestParam(name = "error_message", required = false) String error_message) {
        Long id = loginService.getLoggedInUser().get().getId();
        Optional<AppUser> optionalAppUser = userService.getUserById(id);

        model.addAttribute("error_message", error_message);
        model.addAttribute("message", message);
        model.addAttribute("employee", ChangeAppUserSettingsDto.createForm(optionalAppUser.get()));
        return "user/settings";
    }

    @PostMapping("/settings/change/")
    public String changeSettings(Model model, ChangeAppUserSettingsDto changeAppUserSettingsDto) {
        if ((changeAppUserSettingsDto.getName().trim()).isEmpty()) {
            return "redirect:/employee/settings?error_message=" + "User name is empty";
        }
        if ((changeAppUserSettingsDto.getSurname().trim()).isEmpty()) {
            return "redirect:/employee/settings?error_message=" + "User surname is empty";
        }
        userService.changeSettings(changeAppUserSettingsDto, getCurrentUser());
        return "redirect:/employee/settings?message=" + "User edited";
    }

    @GetMapping("/settings/changePassword")
    public String employeePasswordSettings(Model model, ChangeAppUserSettingsDto employee,
                                           @RequestParam(name = "message", required = false) String message,
                                           @RequestParam(name = "error_message", required = false) String error_message) {
        Long id = loginService.getLoggedInUser().get().getId();
        Optional<AppUser> optionalAppUser = userService.getUserById(id);

        model.addAttribute("message", message);
        model.addAttribute("error_message", error_message);
        model.addAttribute("employee", ChangeAppUserSettingsDto.createForm(optionalAppUser.get()));
        return "employee/settings";
    }

    @PostMapping("/settings/changePassword/")
    public String changePasswordSettings(Model model, ChangeAppUserSettingsDto changeAppUserSettingsDto) {
        Long id = loginService.getLoggedInUser().get().getId();

        if (!userService.ifOldAndNewPasswordAreTheSame(id, changeAppUserSettingsDto)) {
            return "redirect:/employee/settings?error_message=" + "Incorrect password. Try again.";
        }
        if (!changeAppUserSettingsDto.getConfirmPassword().equals(changeAppUserSettingsDto.getPassword())) {
            return "redirect:/employee/settings?error_message=" + "Password and confirm password are not the same";
        }
        if (changeAppUserSettingsDto.getPassword().trim().isEmpty() ||
                changeAppUserSettingsDto.getConfirmPassword().trim().isEmpty()) {
            return "redirect:/employee/settings?error_message=" + "Empty password or confirm password";
        }
        userService.changePasswordSettings(changeAppUserSettingsDto, getCurrentUser());
        return "redirect:/employee/settings?message=" + "User password edited";
    }
}