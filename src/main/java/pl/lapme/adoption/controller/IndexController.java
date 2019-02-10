package pl.lapme.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.model.dto.ChooseRoleDto;
import pl.lapme.adoption.model.dto.RegisterUserDTO;
import pl.lapme.adoption.service.EmailSender;
import pl.lapme.adoption.service.LoginService;
import pl.lapme.adoption.service.UserRoleService;
import pl.lapme.adoption.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@ControllerAdvice
@Scope("session")
public class IndexController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private EmailSender emailSender;

    @ModelAttribute
    public void addAttributes(Model model) {
        Optional<AppUser> optionalLoggedInUserDTO = loginService.getLoggedInUser();
        if (optionalLoggedInUserDTO.isPresent()) {
            AppUser loggedInUserDTO = optionalLoggedInUserDTO.get();
            model.addAttribute("loggedInUserDTO", loggedInUserDTO);
        }
    }

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/locale")
    public String getIndex(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(name = "error", required = false) String error, Model model) {

        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        RegisterUserDTO dto = new RegisterUserDTO();
        model.addAttribute("user_dto", dto);
        return "register";
    }

    @PostMapping("/register")
    public String addUser(Model model, RegisterUserDTO newUserDto) {

        if (!newUserDto.getPassword().equals(newUserDto.getConfirmPassword())) {
            return "redirect:/register?error_message=Password and Confirm password is not the same";
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

//        if (appUserOptional.isPresent()) {
//            Context context = new Context();
//            context.setVariable("user", userService.findByUsername(newUserDto.getUsername()));
//
//            String welcomeMail = templateEngine.process("welcomeMail", context);
//            emailSender.sendEmail(newUserDto.getEmail(), "Witamy w 4LapMe!", welcomeMail);
//            return "login";
//        }
        return "login";
    }
}
