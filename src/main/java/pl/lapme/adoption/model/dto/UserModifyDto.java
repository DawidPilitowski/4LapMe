package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyDto {
    private String name;
    private String surname;

    private String username;

    @Size(min = 6, max = 255)
    private String password;

    @Size(min = 6, max = 255)
    private String passwordConfirm;

    @Email
    private String email;

    private ChooseRoleDto roles;
}
