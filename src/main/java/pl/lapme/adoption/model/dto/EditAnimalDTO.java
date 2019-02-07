package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditAnimalDTO {

    private Long id;
    private String name;
    private String type;
    private String race;
    private int age;
    private String gender;
    private String description;
}
