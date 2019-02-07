package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lapme.adoption.model.MainCategory;

import java.util.Optional;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {

    Optional<MainCategory> findByNameCategory(String name);
}
