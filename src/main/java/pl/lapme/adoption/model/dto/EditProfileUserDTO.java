package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileUserDTO {
    private Long id;
    private String login;
    private String name;
    private String surname;
    private String address;
    private int phoneNumber;
    private String email;

}
