package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lapme.adoption.model.Category;
import pl.lapme.adoption.model.MainCategory;
import pl.lapme.adoption.model.Subcategory;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findBySubcategoryAndMainCategory(Subcategory subcategory, MainCategory mainCategory);
}
