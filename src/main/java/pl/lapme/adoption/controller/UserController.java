package pl.lapme.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.lapme.adoption.model.User;
import pl.lapme.adoption.model.dto.EditEmailUserDTO;
import pl.lapme.adoption.model.dto.EditProfileUserDTO;
import pl.lapme.adoption.model.dto.RegisterUserDTO;
import pl.lapme.adoption.service.EmailSender;
import pl.lapme.adoption.service.UserService;

import java.util.Base64;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailSender emailSender;

    @GetMapping("/")
    public String homePage(){
        return "home";
    }
    @GetMapping("/home")
    public String homePage1(){
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
        RegisterUserDTO dto=new RegisterUserDTO();
        //TODO change ROLES
//        dto.setRoles(userRoleService.getRolesToSelect());
        model.addAttribute("user_dto", dto);
        return "register";
    }

    @PostMapping("/register")
    public String addUser(Model model, RegisterUserDTO newUserDto) {

        if (!newUserDto.getPassword().equals(newUserDto.getConfirmPassword())) {
            model.addAttribute("newUser", new RegisterUserDTO());
            model.addAttribute("failMsg", "Hasła są różne!");
            return "/register";
        }
        if (newUserDto.getEmail().isEmpty()){
            return "redirect:/register?error_message=Email is empty";
        }
        if (newUserDto.getLogin().isEmpty()){
            return "redirect:/register?error_message=Login is empty";
        }
        if (newUserDto.getPassword().isEmpty()||newUserDto.getConfirmPassword().isEmpty()){
            return "redirect:/register?error_message=Password or Confirm Password is empty";
        }

        boolean isNew = userService.registerUser(newUserDto);

        if (!isNew) {
            model.addAttribute("newUser", new RegisterUserDTO());
            model.addAttribute("failMsg", "Nazwa użytkownika zajęta!");
            return "/register";
        }

        Context context = new Context();
        context.setVariable("user", userService.findByLogin(newUserDto.getLogin()));

        String welcomeMail = templateEngine.process("welcomeMail", context);
        emailSender.sendEmail(newUserDto.getEmail(), "Witamy w 4LapMe!", welcomeMail);
        return "login";
    }

    @GetMapping("/activation")
    public String activateUser(@RequestParam(name = "code") String code) {
        Optional<User> userOptional = userService.findByPassword(code);
        if (userOptional.isPresent()) {
            System.out.println("found user");
            User appUser = userOptional.get();
            userService.makeUser(appUser.getId());
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {

        String userLogin = SecurityContextHolder.getContext().getAuthentication().getName();
        User userByLogin =userService.findUserByLogin(userLogin);
        if (userByLogin != null) {
            model.addAttribute("profile", userByLogin);

            if(userByLogin.getAvatar() != null) {
                model.addAttribute("image", new String(Base64.getEncoder().encode(userByLogin.getAvatar())));
            }else{
                model.addAttribute("image", "");
            }
            return "profile";
        }
        return "redirect:/error";
    }

    @GetMapping("/edit")
    public String editProfile(Model model) {
        User loggedInUser = userService.getLoggedInUser();

        EditProfileUserDTO editProfileUserDTO = new EditProfileUserDTO();
        editProfileUserDTO.setLogin(loggedInUser.getLogin());
        editProfileUserDTO.setEmail(loggedInUser.getEmail());
        editProfileUserDTO.setName(loggedInUser.getName());
        editProfileUserDTO.setSurname(loggedInUser.getSurname());
        editProfileUserDTO.setAddress(loggedInUser.getAddress());
        model.addAttribute("userDTO", editProfileUserDTO);
        return "editUser";
    }

    @PostMapping(value = "/edit")
    public String editPost(EditEmailUserDTO newUserDto, EditProfileUserDTO editProfileUserDTO) {
        String registerUserDTO = userService.getLoggedInUser().getEmail();

        if (!registerUserDTO.equals(editProfileUserDTO.getEmail())) {
            Context context = new Context();
            context.setVariable("user", userService.findByLogin(newUserDto.getLogin()));

            String welcomeMail = templateEngine.process("welcomeMail", context);
            emailSender.sendEmail(newUserDto.getEmail(), "Witamy ponownie w 4LapMe!", welcomeMail);
            userService.makeUserNone(newUserDto);
            userService.updateUserDTO(editProfileUserDTO);

            return "login";
        }
        userService.updateUserDTO(editProfileUserDTO);
//        String newMail = editProfileUserDTO.getEmail();
//        System.out.println(newMail);
        return "redirect:/profile";
    }
}