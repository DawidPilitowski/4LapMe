package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lapme.adoption.model.AppUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeAppUserSettingsDto {
    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String oldPassword;

    public static ChangeAppUserSettingsDto createForm(AppUser appUser) {
        return ChangeAppUserSettingsDto.builder()
                .id(appUser.getId())
                .name(appUser.getName())
                .surname(appUser.getSurname())
                .username(appUser.getUsername())
                .email(appUser.getEmail())
                .password(appUser.getPassword())
                .oldPassword(appUser.getPassword())
                .build();
    }
}
