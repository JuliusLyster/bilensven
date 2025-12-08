package dk.bilensven.repository;

import dk.bilensven.model.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Arver standard CRUD operations fra JpaRepository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {

    List<ContactMessage> findByReadFalse();

    List<ContactMessage> findAllByOrderByCreatedAtDesc();
}