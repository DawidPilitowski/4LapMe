package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lapme.adoption.model.Animal;

import java.util.List;
import java.util.Optional;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByType(String type);

    List<Animal> findAllByRace(String race);

    Optional<Animal> findById(Long id);

    List<Animal> findAll();

    List<Animal> findAllByAge(int age);

    List<Animal> findAllByName(String name);

    List<Animal> findAllByGender(String gender);
}
