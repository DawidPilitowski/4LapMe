package pl.lapme.adoption.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.lapme.adoption.model.dto.UserModifyDto;
import pl.lapme.adoption.service.EmailSenderService;
import pl.lapme.adoption.service.LoginService;
import pl.lapme.adoption.service.UserRoleService;
import pl.lapme.adoption.service.UserService;

@Controller
@RequestMapping("/admin/user/")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailSenderService emailSenderService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private LoginService loginService;

    @GetMapping("list")
    private String userList(Model model){
        model.addAttribute("userList",userService.getAllUsers());
        return "admin/user/list";
    }
    @GetMapping("/add/")
    public String getUser(Model model){
        UserModifyDto dto=new UserModifyDto();
        dto.setRoles(userRoleService.getRoleToSelect());
        model.addAttribute("user",dto);

        return "admin/user/userForm";
    }
}
