package pl.lapme.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.AppUser;
import pl.lapme.adoption.model.Category;
import pl.lapme.adoption.model.dto.AddAnimalDTO;
import pl.lapme.adoption.model.dto.EditAnimalDTO;
import pl.lapme.adoption.service.AnimalService;
import pl.lapme.adoption.service.CategoryService;
import pl.lapme.adoption.service.LoginService;
import pl.lapme.adoption.service.UserService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/animal/")
public class AnimalController {
    @Autowired
    private AnimalService animalService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private LoginService loginService;

    @ModelAttribute("currentUser")
    public AppUser getCurrentUser() {
        return loginService.getLoggedInUser().get();
    }
//TODO zrobić wyszukiwanie poprzez  @RequestParam

//    @GetMapping(path = "/show/all")
//    public List<Animal> getAllAnimals() {
//        return animalService.getAllAnimmals();
//    }
//
//    @GetMapping(path = "/get/all/by/type")
//    public List<Animal> getAllByType(String type) {
//        return animalService.getAllByType(type);
//    }
//
//    @GetMapping(path = "/get/all/by/race")
//    public List<Animal> getAllByRace(String race) {
//        return animalService.getAllByRace(race);
//    }
//
//    @GetMapping(path = "/get/all/by/age")
//    public List<Animal> getAllByAge(int age) {
//        return animalService.getAllByAge(age);
//    }
//
//    @GetMapping(path = "/get/all/by/name")
//    public List<Animal> getAllByName(String name) {
//        return animalService.getAllByName(name);
//    }
//
//    @GetMapping(path = "/get/all/by/gender")
//    public List<Animal> getAllByGender(String gender) {
//        return animalService.getAllByGender(gender);
//    }

    @GetMapping(path = "/add")
    public String addAnimal(Model model,
                            @RequestParam(name = "error_message", required = false) String error_message,
                            @RequestParam(name = "message", required = false) String message) {
        model.addAttribute("addAnimal", new AddAnimalDTO());
        model.addAttribute("animalCategory", categoryService.findAll());
        model.addAttribute("message", message);
        model.addAttribute("error_message", error_message);
        return "animal/addAnimalForm";
    }

    @PostMapping(path = "/add")
    public String addAnimal(Model model, AddAnimalDTO animalDTO, @RequestParam("photo") MultipartFile photo) {
        model.addAttribute("newAnimal", new AddAnimalDTO());
        if (animalDTO.getName().isEmpty()) {
            return "redirect:/animal/add?error_message=" + "Animal name is empty";
        }
        if (animalDTO.getGender().isEmpty()) {
            return "redirect:/animal/add?error_message=" + "Animal gender is empty";
        }
        if (animalDTO.getRace().isEmpty()) {
            return "redirect:/animal/add?error_message=" + "Animal name is empty";
        }
        if (animalDTO.getType().isEmpty()) {
            return "redirect:/animal/add?error_message=" + "Animal type is empty";
        }
        if (animalDTO.getImage().length == 0) {
            return "redirect:/animal/add?error_message=" + "Animal type is empty";
        }
        animalService.saveAnimal(animalDTO, photo);
        return "list";
    }

    @DeleteMapping(path = "/remove/{id}")
    public String removeAnimal(@PathVariable(value = "id") Long id) {
        //TODO dodać template

        if (animalService.removeAnimal(id, getCurrentUser())) {
            return "redirect:/.........?message=Animal has been removed.";
        } else {
            return "redirect:/.........?error_message=Animal has not been removed.";

        }
    }

    @GetMapping("/edit/{id}")
    public String editAnimal(Model model, @PathVariable(name = "id") Long id) {
        Optional<Animal> optionalAnimal = animalService.findById(id);
        Animal animal = optionalAnimal.get();
        AppUser appUser = getCurrentUser();
        if (optionalAnimal.isPresent()) {
            if (loginService.isAdmin() || animal.getUser().getId().equals(appUser.getId())) {
                model.addAttribute("editAnimal", EditAnimalDTO.createForm(optionalAnimal.get()));
            } else {
                return "redirect:/..............?error_message=You cannot edit animal.";
            }
        } else {
            return "redirect:/...............?error_message=Animal does not exist.";
        }
        return "animal/editAnimalForm";
    }

    @PostMapping("/edit/{id}")
    public String setAnimalEdit(EditAnimalDTO animalDTO, @PathVariable(name = "id") Long id) {
        Optional<Animal> animalOptional = animalService.findById(id);
        Animal animal = animalOptional.get();
        AppUser getLoggedUser = getCurrentUser();
        if (loginService.isAdmin() || animal.getUser().getId().equals(getLoggedUser.getId())) {
            if (animalService.isEpty(animalDTO)) {
                return "redirect:/..............?error_message=You cannot edit animal.";
            }
            animalService.modifiedAnimal(id, animalDTO, getCurrentUser());
        }
        return ".....................?message=Animal edited";
    }

    @GetMapping("/details/{id}")
    public String animalDetails(Model model, @PathVariable(name = "id") Long id,
                                @RequestParam(name = "error_message", required = false) String error_message) {
        Optional<Animal> optionalAnimal = animalService.findById(id);
        model.addAttribute("erroe_message", error_message);
        if (optionalAnimal.isPresent()) {
            Animal animal = optionalAnimal.get();
            model.addAttribute("animalForm", EditAnimalDTO.createForm(optionalAnimal.get()));
            model.addAttribute("animal", animal);
            return "animal/animalDetails";
        }
        return "redirect:/animal............?error_message=Given animal not exist.";
    }
}