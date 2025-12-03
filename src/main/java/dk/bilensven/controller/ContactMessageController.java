package dk.bilensven.controller;

import dk.bilensven.dto.ContactMessageDTO;
import dk.bilensven.service.ContactMessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@Slf4j
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<?> submitContactForm(
            @Valid @RequestBody ContactMessageDTO dto,
            HttpServletRequest request) {

        String ipAddress = getClientIP(request);
        log.info("POST /api/contact from IP: {}", ipAddress);

        ContactMessageDTO saved = contactMessageService.save(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Din besked er blevet sendt. Vi kontakter dig snart.",
                "id", saved.getId()
        ));
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ContactMessageDTO>> getAllMessages() {
        log.info("GET /api/contact/messages - Fetch all messages");
        List<ContactMessageDTO> messages = contactMessageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/unread")
    public ResponseEntity<List<ContactMessageDTO>> getUnreadMessages() {
        log.info("GET /api/contact/messages/unread");
        List<ContactMessageDTO> messages = contactMessageService.getUnreadMessages();
        return ResponseEntity.ok(messages);
    }

    @PatchMapping("/messages/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        log.info("PATCH /api/contact/messages/{}/read", id);
        contactMessageService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}