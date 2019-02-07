package pl.lapme.adoption.service;

import org.springframework.stereotype.Service;
import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.dto.AddAnimalDTO;
import pl.lapme.adoption.repository.AnimalRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    private AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

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
        Optional<Animal> newAnimal = animalRepository.findById(animalDTO.getId());
        if (newAnimal.isPresent()) {
            return false;
        }
        Animal animal = new Animal(animalDTO.getName());
        animalRepository.save(animal);
        return true;
    }

    public void removeAnimal(Long id) {
        animalRepository.deleteById(id);
    }
}
