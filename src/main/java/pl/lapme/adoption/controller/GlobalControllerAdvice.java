package pl.lapme.adoption.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.lapme.adoption.components.CategoryComponent;
import pl.lapme.adoption.model.MainCategory;
import pl.lapme.adoption.model.Subcategory;

import java.util.List;
import java.util.Map;

@ControllerAdvice(basePackages = {"pl.lapme.adoption.controller"})
public class GlobalControllerAdvice {

    @Autowired
    private CategoryComponent categoryComponent;

    @ModelAttribute("categories")
    public Map<MainCategory, List<Subcategory>> getCategories() {
        return categoryComponent.getCategoryMap();
    }
}
