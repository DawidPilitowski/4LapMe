package pl.lapme.adoption.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.model.dto.ChangeAppUserSettingsDto;
import pl.lapme.adoption.model.dto.ModifiedRoleDto;
import pl.lapme.adoption.model.dto.UserModifyDto;
import pl.lapme.adoption.service.EmailSenderService;
import pl.lapme.adoption.service.LoginService;
import pl.lapme.adoption.service.UserRoleService;
import pl.lapme.adoption.service.UserService;

import java.util.Optional;

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
    @GetMapping("/edit/{id}")
    public String editUser(Model model, ModifiedRoleDto modifiedRoleDto, @PathVariable(name = "id") Long id,
                               @RequestParam(name = "error_message", required = false) String error_message) {
        Optional<AppUser> userOptional = userService.findById(id);
        model.addAttribute("error_message", error_message);
        if (userOptional.isPresent()) {
            if (loginService.isAdmin()) {
                modifiedRoleDto.setRoles(userRoleService.getRoleToSelect());
                model.addAttribute("userRole", modifiedRoleDto);
                model.addAttribute("appUser", ChangeAppUserSettingsDto.createForm(userOptional.get()));
            } else {
                return "redirect:/admin/user/edit" + id + "?error_message=You can not edit user.";
            }
        }
        return "admin/user/editForm";
    }
    @PostMapping("/edit/{id}")
    public String editUserForm(ChangeAppUserSettingsDto dto, @PathVariable(name = "id") Long id) {
        if (loginService.isAdmin()) {
            if (dto.getUsername().isEmpty() ||
                    dto.getEmail().isEmpty()) {
                return "redirect:/admin/user/edit/" + id + "?error_message=" + "Empty Field";
            } else {
                userService.modifiedUser(dto, id);
            }
        } else {
            return "redirect:/?error_message=" + "Access denied";
        }
        return "redirect:/admin/user/list";
    }
    @PostMapping("/edit/{id}/changePassword")
    public String changePassword(ChangeAppUserSettingsDto dto, @PathVariable(name = "id") Long id) {
        if (loginService.isAdmin()) {
            if (dto.getPassword().trim().isEmpty() ||
                    dto.getConfirmPassword().trim().isEmpty()) {
                return "redirect:/admin/user/edit/" + id + "?error_message=" + "Password or confirm password is empty";
            } else if (!dto.getConfirmPassword().equals(dto.getPassword())) {
                return "redirect:/admin/user/edit/" + id + "?error_message=" + "Password and Confirm password is not the same";
            } else {
                userService.modifiedUserPassword(dto, id);
            }
        } else {
            return "redirect:/?error_message=" + "Access denied";
        }
        return "redirect:/admin/user/list";
    }
    @PostMapping("/edit/{id}/changeRoles")
    public String changeRoles(ModifiedRoleDto modifiedRoleDto,  @PathVariable(name = "id") Long id) {
        userService.modifiedUserRole(modifiedRoleDto,id);
        return "redirect:/admin/user/list";
    }
}
