package dk.bilensven.controller;

import dk.bilensven.dto.ContactMessageDTO;
import dk.bilensven.service.ContactMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
// REST API Controller for kontaktformular beskeder
@RequestMapping("/api/contact")
// Base URL
@RequiredArgsConstructor
// Lombok: Auto-generate constructor for final fields
@Slf4j
// Lombok: Tilføj logger (log.info, log.error, etc.)

public class ContactMessageController {

    // Service layer dependency
    private final ContactMessageService contactMessageService;

    // POST: Modtag og gem kontaktformular
    @PostMapping
    public ResponseEntity<?> submitContactForm(@Valid @RequestBody ContactMessageDTO dto) {
        log.info("POST /api/contact - New contact message received");

        ContactMessageDTO saved = contactMessageService.save(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Din besked er blevet sendt. Vi kontakter dig snart.",
                "id", saved.getId()
        ));
    }

    // GET: Hent alle beskeder (admin panel)
    @GetMapping("/messages")
    public ResponseEntity<List<ContactMessageDTO>> getAllMessages() {
        log.info("GET /api/contact/messages");
        List<ContactMessageDTO> messages = contactMessageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    // GET: Hent ulæste beskeder
    @GetMapping("/messages/unread")
    public ResponseEntity<List<ContactMessageDTO>> getUnreadMessages() {
        log.info("GET /api/contact/messages/unread");
        List<ContactMessageDTO> messages = contactMessageService.getUnreadMessages();
        return ResponseEntity.ok(messages);
    }

    // PATCH: Marker besked som læst
    @PatchMapping("/messages/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("PATCH /api/contact/messages/{}/read", id);
        contactMessageService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE: Slet besked
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.info("DELETE /api/contact/messages/{}", id);
        contactMessageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}