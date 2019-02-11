package pl.lapme.adoption.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lapme.adoption.model.Animal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditAnimalDTO {

    private Long id;
    private String name;
    private String type;
    private String race;
    private int age;
    private String gender;
    private String description;

    public static EditAnimalDTO createForm(Animal animal){
        return EditAnimalDTO.builder()
                .id(animal.getId())
                .name(animal.getName())
                .type(animal.getType())
                .race(animal.getRace())
                .age(animal.getAge())
                .gender(animal.getGender())
                .description(animal.getDescription())
                .build();
    }
}
