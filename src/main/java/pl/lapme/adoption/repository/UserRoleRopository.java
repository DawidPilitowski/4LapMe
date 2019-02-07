package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lapme.adoption.model.AppUserRole;

import java.util.Optional;

public interface UserRoleRopository extends JpaRepository<AppUserRole,Long> {
    Optional<AppUserRole> findByName(String name);
}
