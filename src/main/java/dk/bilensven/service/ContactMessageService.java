package dk.bilensven.service;

import dk.bilensven.dto.ContactMessageDTO;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.model.ContactMessage;
import dk.bilensven.repository.ContactMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Service layer for kontaktbesked business logic
@Service
@Slf4j
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    // Gem ny kontaktbesked fra website formular
    public ContactMessageDTO save(ContactMessageDTO dto) {
        log.info("Saving contact message from: {}", dto.getEmail());

        ContactMessage message = toEntity(dto);
        message.setRead(false);  // Nye beskeder starter som ulæste

        ContactMessage saved = contactMessageRepository.save(message);
        return toDTO(saved);
    }

    // Hent alle beskeder sorteret efter dato (nyeste først)
    // FUNCTIONAL PROGRAMMING: Stream with map and collect
    public List<ContactMessageDTO> getAllMessages() {
        log.info("Fetching all contact messages");

        return contactMessageRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDTO)  // Entity → DTO transformation
                .collect(Collectors.toList());
    }

    // Hent kun ulæste beskeder (admin notification)
    // FUNCTIONAL PROGRAMMING: Database filter + sorting + mapping
    public List<ContactMessageDTO> getUnreadMessages() {
        log.info("Fetching unread contact messages");

        return contactMessageRepository.findByReadFalse().stream()
                .sorted(Comparator.comparing(ContactMessage::getCreatedAt,
                        Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Marker besked som læst
    public void markAsRead(Long id) {
        log.info("Marking message {} as read", id);

        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage", id));

        message.setRead(true);
        contactMessageRepository.save(message);
    }

    // Slet besked permanent
    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting contact message {}", id);

        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage", id));

        contactMessageRepository.delete(message);
        log.info("Message {} deleted successfully", id);
    }

    // Konverter Entity → DTO (for API responses)
    private ContactMessageDTO toDTO(ContactMessage message) {
        ContactMessageDTO dto = new ContactMessageDTO();
        dto.setId(message.getId());
        dto.setName(message.getName());
        dto.setEmail(message.getEmail());
        dto.setPhone(message.getPhone());
        dto.setMessage(message.getMessage());
        dto.setRead(message.isRead());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());
        return dto;
    }

    // Konverter DTO → Entity (for database persistence)
    private ContactMessage toEntity(ContactMessageDTO dto) {
        ContactMessage message = new ContactMessage();
        message.setName(dto.getName());
        message.setEmail(dto.getEmail());
        message.setPhone(dto.getPhone());
        message.setMessage(dto.getMessage());
        return message;
    }
}