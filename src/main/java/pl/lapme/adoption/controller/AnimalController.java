package pl.lapme.adoption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.Category;
import pl.lapme.adoption.model.dto.AddAnimalDTO;
import pl.lapme.adoption.model.dto.EditAnimalDTO;
import pl.lapme.adoption.service.AnimalService;
import pl.lapme.adoption.service.CategoryService;
import pl.lapme.adoption.service.UserService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/animal")
public class AnimalController {

    private AnimalService animalService;
    private UserService userService;
    private CategoryService categoryService;

    public AnimalController(AnimalService animalService, UserService userService, CategoryService categoryService) {
        this.animalService = animalService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping(path = "/show/all")
    public List<Animal> getAllAnimals() {
        return animalService.getAllAnimmals();
    }

    @GetMapping(path = "/get/all/by/type")
    public List<Animal> getAllByType(String type) {
        return animalService.getAllByType(type);
    }

    @GetMapping(path = "/get/all/by/race")
    public List<Animal> getAllByRace(String race) {
        return animalService.getAllByRace(race);
    }

    @GetMapping(path = "/get/all/by/age")
    public List<Animal> getAllByAge(int age) {
        return animalService.getAllByAge(age);
    }

    @GetMapping(path = "/get/all/by/name")
    public List<Animal> getAllByName(String name) {
        return animalService.getAllByName(name);
    }

    @GetMapping(path = "/get/all/by/gender")
    public List<Animal> getAllByGender(String gender) {
        return animalService.getAllByGender(gender);
    }

    @GetMapping(path = "/add")
    public String addAnimal(Model model, AddAnimalDTO animalDTO) {
        AddAnimalDTO animal = new AddAnimalDTO();
        System.out.println(userService.getLoggedInUser().getLogin());
        animal.setSellingLogin(userService.getLoggedInUser().getLogin());
        System.out.println(animal.getSellingLogin());
        List<Category> categories = categoryService.getAllList();
        model.addAttribute("animal", animalDTO);
        model.addAttribute("categories2", categories);
        return "add/form";
    }

    @PostMapping(path = "/add")
    public String addAnimal(AddAnimalDTO animalDTO, @RequestParam("photo") MultipartFile photo) {
        String name = photo.getName();
        try {
            byte[] bytes = photo.getBytes();
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
            stream.write(bytes);
            stream.close();
            animalDTO.setImage(bytes);
        } catch (Exception e) {
            System.out.println("File has not been added.");
        }
        System.out.println(animalDTO.getSellingLogin());
        animalService.saveAnimal(animalDTO);
        return "list";
    }

    @DeleteMapping(path = "/remove/{id}")
    public String removeAnimal(@PathVariable(value = "id") Long id) {
        animalService.removeAnimal(id);
        return "succes";
    }

    @GetMapping(path = "/edit/{id}")
    public String editAnimal(Model model, @PathVariable(name = "id") Long id) {
        Optional<Animal> existingAnimal = animalService.findById(id);
        if (existingAnimal.isPresent()) {
            Animal animal = existingAnimal.get();
            EditAnimalDTO animalDTO = new EditAnimalDTO(
                    animal.getId(),
                    animal.getName(),
                    animal.getType(),
                    animal.getRace(),
                    animal.getAge(),
                    animal.getGender(),
                    animal.getDescription());
            model.addAttribute("animalDTO", animalDTO);
        }
        return "edit/form";
    }

    @PutMapping(path = "/edit")
    public String setAnimalEdit(EditAnimalDTO animalDTO) {
        Optional<Animal> editedAnimal = animalService.findById(animalDTO.getId());
        if (editedAnimal.isPresent()) {
            Animal edited = editedAnimal.get();
            edited.setName(animalDTO.getName());
            edited.setType(animalDTO.getType());
            edited.setRace(animalDTO.getRace());
            edited.setAge(animalDTO.getAge());
            edited.setGender(animalDTO.getGender());
            edited.setDescription(animalDTO.getDescription());
        }
        return "list";
    }
}