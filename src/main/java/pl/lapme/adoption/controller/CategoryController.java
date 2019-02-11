package pl.lapme.adoption.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.lapme.adoption.model.Category;
import pl.lapme.adoption.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(path = "/categoryList")
    public String list(Model model){
        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList", categoryList);
        return "categoryList";
    }
    @GetMapping("/adoption/cats")
    public String adoptionCats(){
        return "adoptionCats";
    }
}
