package pl.lapme.adoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.model.EventFactory;
import pl.lapme.adoption.model.dto.AddAnimalDTO;
import pl.lapme.adoption.model.dto.EditAnimalDTO;
import pl.lapme.adoption.repository.AnimalRepository;
import pl.lapme.adoption.repository.ApplicationEventRepository;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    @Autowired
    private ApplicationEventRepository applicationEventRepository;


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

    public boolean saveAnimal(AddAnimalDTO animalDTO, MultipartFile photo) {
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
        String name = photo.getName();
        try {
            byte[] bytes = photo.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
            stream.write(bytes);
            stream.close();
            animalDTO.setImage(bytes);
            animal.setImage(animalDTO.getImage());

        } catch (Exception e) {
            return false;
        }
        animalRepository.save(animal);
        return true;
    }

    public boolean removeAnimal(Long id, AppUser currentUser) {
        Optional<Animal> optionalAnimal = animalRepository.findById(id);
        if (optionalAnimal.isPresent()) {
            if (loginService.isAdmin() || optionalAnimal.get().getUser().getId() == currentUser.getId()) {
                applicationEventRepository.saveAndFlush(EventFactory.eventMessage("User removed animal :" + optionalAnimal.get(), currentUser.getUsername()));
                animalRepository.deleteById(id);
                return true;
            }
        } else {
            applicationEventRepository.saveAndFlush(EventFactory.eventMessage("User tried to remove non existing animal.", currentUser.getUsername()));
        }
        return false;
    }

    public boolean isEpty(EditAnimalDTO animalDTO) {
        if (animalDTO.getName().trim().isEmpty() ||
                animalDTO.getGender().trim().isEmpty() ||
                animalDTO.getRace().trim().isEmpty() ||
                animalDTO.getType().trim().isEmpty()) {
            return true;
        }
        return false;
    }

//TODO rozdzielić modyfikacje?

    public Optional<Animal> modifiedAnimal(Long id, EditAnimalDTO animalDTO, AppUser currentUser) {
        Optional<Animal> optionalAnimal = findById(id);
        if (optionalAnimal.isPresent()) {
            Animal animal = optionalAnimal.get();
            if (currentUser.getId().equals(animal.getUser().getId()) || loginService.isAdmin()) {
                animal.setName(animalDTO.getName());
                animal.setAge(animalDTO.getAge());
                animal.setGender(animalDTO.getGender());
                animal.setRace(animalDTO.getRace());
                animal.setDescription(animalDTO.getDescription());
                animal.setType(animalDTO.getType());
                animalRepository.save(animal);
                Optional.of(animal);
            }
        }
        return Optional.empty();
    }

}
