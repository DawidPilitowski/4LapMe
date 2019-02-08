package pl.lapme.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.basket.AdoptionBasket;
import pl.lapme.adoption.repository.AnimalRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BasketController {

    @Autowired
    private AdoptionBasket basket;
    @Autowired
    private AnimalRepository animalRepository;
    private List<Animal> allAnimals = new ArrayList<>();

    @PostConstruct
    public void createDefaultAnimals() {
        Animal e = new Animal("Kot Mirek");
        Animal e1 = new Animal("Pies Anrzej");
        Animal e2 = new Animal("Żaba Monika");
        animalRepository.save(e);
        animalRepository.save(e1);
        animalRepository.save(e2);
    }

    @GetMapping(path = "/allAnimals")
    public String showAllAnimals(Model model){

        allAnimals = animalRepository.findAll();
        model.addAttribute("animals", allAnimals);
        return "allAnimals";
    }


    @RequestMapping({ "/adoptAnimal" })
    public String listAnimalHandler(HttpServletRequest request, Model model,
                                     @RequestParam(value = "id", defaultValue = "") Long id) {

        Animal animal = null;
        if (id != null && id > 0) {
            animal = animalRepository.findById(id).get();
        }
        if (animal != null) {
            basket.addAnimal(animal, 1);
        }
        model.addAttribute("basket", basket);
        return "animalBasket";
    }


    @RequestMapping({ "/removeAnimal" })
    public String removeAnimalHaandler(HttpServletRequest request, Model model, //
                                       @RequestParam(value = "id", defaultValue = "") Long id) {
        Animal animal = null;
        if (id != null && id > 0) {
            animal =  animalRepository.findById(id).get();
        }
        if (animal != null) {
            basket.removeAnimal(animal);
        }

        model.addAttribute("basket", basket);
        return "animalBasket";
    }

    @RequestMapping({ "/pay" })
    public String payAdoption(HttpServletRequest request, Model model,
                              @RequestParam(value = "id", defaultValue = "") Long id){

        model.addAttribute("basket", basket);
        return "pay";
    }

    // będzie wybór na stronie 4adopt zwierząt i kliknięcie na jedno = dodano do koszyka

   // @PostMapping()
}
