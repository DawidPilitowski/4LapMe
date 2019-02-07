package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileBreedingDTO {

    private String name;
    private String surname;
    private String address;
    private int phoneNumber;
    private int accountNumber;
    private int nip;
    private String breedingLicence;
    private String email;

}
