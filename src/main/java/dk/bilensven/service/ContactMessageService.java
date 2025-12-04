package dk.bilensven.service;

import dk.bilensven.dto.ContactMessageDTO;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.model.ContactMessage;
import dk.bilensven.repository.ContactMessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;

    public ContactMessageDTO save(ContactMessageDTO dto) {
        log.info("Saving contact message from: {}", dto.getEmail());

        ContactMessage message = toEntity(dto);
        message.setRead(false);
        message.setCreatedAt(LocalDateTime.now());

        ContactMessage saved = contactMessageRepository.save(message);
        return toDTO(saved);
    }

    /**
     * FUNCTIONAL PROGRAMMING: Stream with map and collect
     */
    public List<ContactMessageDTO> getAllMessages() {
        log.info("Fetching all contact messages");

        return contactMessageRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * FUNCTIONAL PROGRAMMING: Filter unread messages
     */
    public List<ContactMessageDTO> getUnreadMessages() {
        log.info("Fetching unread contact messages");

        return contactMessageRepository.findAll().stream()
                .filter(msg -> !msg.isRead())  // ✅ Changed from getRead() to isRead()
                .sorted(Comparator.comparing(ContactMessage::getCreatedAt).reversed())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long id) {
        log.info("Marking message {} as read", id);

        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage", id));

        message.setRead(true);
        contactMessageRepository.save(message);
    }

    private ContactMessageDTO toDTO(ContactMessage message) {
        ContactMessageDTO dto = new ContactMessageDTO();
        dto.setId(message.getId());
        dto.setName(message.getName());
        dto.setEmail(message.getEmail());
        dto.setPhone(message.getPhone());
        dto.setMessage(message.getMessage());
        dto.setRead(message.isRead());  // ✅ Changed from getRead() to isRead()
        dto.setCreatedAt(message.getCreatedAt());
        return dto;
    }

    private ContactMessage toEntity(ContactMessageDTO dto) {
        ContactMessage message = new ContactMessage();
        message.setName(dto.getName());
        message.setEmail(dto.getEmail());
        message.setPhone(dto.getPhone());
        message.setMessage(dto.getMessage());
        return message;
    }
    @Transactional
    public void deleteMessage(Long id) {
        log.info("Deleting contact message {}", id);

        ContactMessage message = contactMessageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        contactMessageRepository.delete(message);
        log.info("Message {} deleted successfully", id);
    }
}