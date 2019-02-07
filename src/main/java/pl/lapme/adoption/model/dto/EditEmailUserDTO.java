package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditEmailUserDTO {
    private String login;
    private String email;
    private int privilege;
}
