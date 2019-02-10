package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.model.Category;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAnimalDTO {

    private Long id;
    private String name;
    private String type;
    private String race;
    private int age;
    private String gender;
    private String description;

    private Category category;
    private AppUser userName;
    private byte[] image;
}
