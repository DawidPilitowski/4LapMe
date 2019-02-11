package pl.lapme.adoption.service;

import org.springframework.stereotype.Service;
import pl.lapme.adoption.model.Category;
import pl.lapme.adoption.model.MainCategory;
import pl.lapme.adoption.model.Subcategory;
import pl.lapme.adoption.repository.CategoryRepository;
import pl.lapme.adoption.repository.MainCategoryRepository;
import pl.lapme.adoption.repository.SubcategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private MainCategoryRepository mainCategoryRepository;
    private SubcategoryRepository subcategoryRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           MainCategoryRepository mainCategoryRepository,
                           SubcategoryRepository subcategoryRepository) {

        this.categoryRepository = categoryRepository;
        this.mainCategoryRepository = mainCategoryRepository;
        this.subcategoryRepository = subcategoryRepository;
    }

    public List<Category> getAllList() {
        return categoryRepository.findAll();
    }

    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    public Category checkAndSaveIfNotExists(Category category) {
        boolean categoryExisting = true;

        Optional<Subcategory> subcategory = subcategoryRepository.findByName(category.getSubcategory().getName());

        Subcategory subcategoryInDatabase;
        if (!subcategory.isPresent()) {
            subcategoryInDatabase = subcategoryRepository.save(category.getSubcategory());
            categoryExisting = false;
        } else {
            subcategoryInDatabase = subcategory.get();
        }

        Optional<MainCategory> mainCategory = mainCategoryRepository.findByNameCategory(category.getMainCategory().getNameCategory());
        MainCategory mainCategoryInDatabase;
        if (!mainCategory.isPresent()) {
            mainCategoryInDatabase = mainCategoryRepository.save(category.getMainCategory());
            categoryExisting = false;
        } else {
            mainCategoryInDatabase = mainCategory.get();
        }

        if (!categoryExisting) {
            category.setMainCategory(mainCategoryInDatabase);
            category.setSubcategory(subcategoryInDatabase);

            return categoryRepository.save(category);
        }

        Category categoryFromDatabase;
        Optional<Category> categoryInDatabase = categoryRepository
                .findBySubcategoryAndMainCategory(subcategoryInDatabase, mainCategoryInDatabase);
        if (categoryInDatabase.isPresent()) {
            categoryFromDatabase = categoryInDatabase.get();
        } else {
            category.setMainCategory(mainCategoryInDatabase);
            category.setSubcategory(subcategoryInDatabase);
            categoryFromDatabase = categoryRepository.save(category);
        }

        return categoryFromDatabase;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
