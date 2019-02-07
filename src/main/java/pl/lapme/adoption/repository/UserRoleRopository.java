package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lapme.adoption.model.UserRole;

import java.util.Optional;

public interface UserRoleRopository extends JpaRepository<UserRole,Long> {
    Optional<UserRole> findByName(String name);
}
