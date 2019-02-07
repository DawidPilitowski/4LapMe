package pl.lapme.adoption.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.lapme.adoption.model.ApplicationEvent;

public interface ApplicationEventRepository extends JpaRepository<ApplicationEvent, Long> {
}
