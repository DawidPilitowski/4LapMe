package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lapme.adoption.model.Subcategory;

import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    Optional<Subcategory> findByName(String name);
}
