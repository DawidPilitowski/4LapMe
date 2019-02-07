package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lapme.adoption.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findUserByLogin(String login);
    Optional<User> findByPassword(String code);
    User findByLogin(String login);
    User findOneById(Long id);


    User findOneByLogin(String login);
}
