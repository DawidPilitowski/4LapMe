package pl.lapme.adoption.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDTO {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private ChooseRoleDto roles;

}
