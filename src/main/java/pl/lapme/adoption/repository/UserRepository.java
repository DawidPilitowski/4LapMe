package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lapme.adoption.model.AppUser;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser,Long> {
    Optional<AppUser> findUserByLogin(String login);
    Optional<AppUser> findByPassword(String code);
    AppUser findByLogin(String login);
    AppUser findOneById(Long id);


    AppUser findOneByLogin(String login);

    Optional<AppUser> findByUsername(String username);
}
