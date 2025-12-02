package dk.bilensven.repository;

import dk.bilensven.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByActiveTrue();
    Optional<Service> findByName(String name);
}
