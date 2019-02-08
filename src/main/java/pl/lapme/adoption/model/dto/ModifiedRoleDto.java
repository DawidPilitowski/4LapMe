package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifiedRoleDto {
    private Long id;
    private ChooseRoleDto roles;
}
