package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChooseRoleDto {
    private Long role_shelter_id;
    private Long role_breeder_id;
    private Long role_admin_id;

    private String role_shelter;
    private String role_breeder;
    private String role_admin;

    private String role_shelter_name;
    private String role_breeder_name;
    private String role_admin_name;
}
