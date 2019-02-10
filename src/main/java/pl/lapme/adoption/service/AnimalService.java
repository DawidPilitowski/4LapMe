package pl.lapme.adoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.dto.AddAnimalDTO;
import pl.lapme.adoption.repository.AnimalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {
    @Value("${animal.default.approved}")
    private Boolean defaultApproved;

    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private LoginService loginService;


    public List<Animal> getAllAnimmals() {
        return animalRepository.findAll();
    }

    public List<Animal> getAllByType(String type) {
        return animalRepository.findAllByType(type);
    }

    public List<Animal> getAllByRace(String race) {
        return animalRepository.findAllByRace(race);
    }

    public List<Animal> getAllByAge(int age) {
        return animalRepository.findAllByAge(age);
    }

    public List<Animal> getAllByName(String name) {
        return animalRepository.findAllByName(name);
    }

    public List<Animal> getAllByGender(String gender) {
        return animalRepository.findAllByGender(gender);
    }

    public Optional<Animal> findById(Long id) {
        return animalRepository.findById(id);
    }

    public boolean saveAnimal(AddAnimalDTO animalDTO) {
        Animal animal = new Animal();
        if (loginService.isAdmin()) {
            animal.setAwaitingApproval(defaultApproved);
        } else {
            animal.setAwaitingApproval(!defaultApproved);
        }
        animal.setName(animalDTO.getName().trim());
        animal.setAge(animalDTO.getAge());
        animal.setDescription(animalDTO.getDescription());
        animal.setGender(animalDTO.getGender().trim());
        animal.setRace(animalDTO.getRace().trim());
        animal.setType(animalDTO.getType().trim());
        animal.setUser(loginService.getLoggedInUser().get());
        animalRepository.save(animal);
        return true;
    }

    public void removeAnimal(Long id) {
        animalRepository.deleteById(id);
    }
}
