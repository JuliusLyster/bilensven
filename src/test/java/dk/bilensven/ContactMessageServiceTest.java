package dk.bilensven;

import dk.bilensven.dto.ContactMessageDTO;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.model.ContactMessage;
import dk.bilensven.repository.ContactMessageRepository;
import dk.bilensven.service.ContactMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ContactMessageService
 *
 * Test scenarios:
 * - Save new contact message
 * - Get all messages sorted by date
 * - Get only unread messages
 * - Mark message as read
 * - Delete message
 * - Handle not found exceptions
 */
@ExtendWith(MockitoExtension.class)
class ContactMessageServiceTest {

    @Mock
    private ContactMessageRepository contactMessageRepository;

    @InjectMocks
    private ContactMessageService contactMessageService;

    @Test
    void save_ShouldCreateNewMessage() {
        // Given
        ContactMessageDTO dto = new ContactMessageDTO();
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setPhone("+45 12345678");
        dto.setMessage("Test message");

        ContactMessage savedMessage = new ContactMessage();
        savedMessage.setId(1L);
        savedMessage.setName(dto.getName());
        savedMessage.setEmail(dto.getEmail());
        savedMessage.setPhone(dto.getPhone());
        savedMessage.setMessage(dto.getMessage());
        savedMessage.setRead(false);

        when(contactMessageRepository.save(any(ContactMessage.class)))
                .thenReturn(savedMessage);

        // When
        ContactMessageDTO result = contactMessageService.save(dto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test User", result.getName());
        assertEquals("test@example.com", result.getEmail());
        assertFalse(result.getRead());
        verify(contactMessageRepository).save(any(ContactMessage.class));
    }

    @Test
    void getAllMessages_ShouldReturnSortedByDateDesc() {
        // Given
        ContactMessage msg1 = createMessage(1L, "User 1", false, LocalDateTime.now().minusDays(2));
        ContactMessage msg2 = createMessage(2L, "User 2", true, LocalDateTime.now().minusDays(1));
        ContactMessage msg3 = createMessage(3L, "User 3", false, LocalDateTime.now());

        when(contactMessageRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(Arrays.asList(msg3, msg2, msg1));

        // When
        List<ContactMessageDTO> result = contactMessageService.getAllMessages();

        // Then
        assertEquals(3, result.size());
        assertEquals(3L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(1L, result.get(2).getId());
        verify(contactMessageRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getUnreadMessages_ShouldReturnOnlyUnreadMessages() {
        // Given
        ContactMessage unread1 = createMessage(1L, "User 1", false, LocalDateTime.now().minusDays(1));
        ContactMessage unread2 = createMessage(2L, "User 2", false, LocalDateTime.now());

        when(contactMessageRepository.findByReadFalse())
                .thenReturn(Arrays.asList(unread1, unread2));

        // When
        List<ContactMessageDTO> result = contactMessageService.getUnreadMessages();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(ContactMessageDTO::getRead));
        verify(contactMessageRepository).findByReadFalse();
    }

    @Test
    void markAsRead_WhenMessageExists_ShouldUpdateReadStatus() {
        // Given
        Long messageId = 1L;
        ContactMessage message = createMessage(messageId, "User", false, LocalDateTime.now());

        when(contactMessageRepository.findById(messageId))
                .thenReturn(Optional.of(message));
        when(contactMessageRepository.save(any(ContactMessage.class)))
                .thenReturn(message);

        // When
        contactMessageService.markAsRead(messageId);

        // Then
        assertTrue(message.isRead());
        verify(contactMessageRepository).findById(messageId);
        verify(contactMessageRepository).save(message);
    }

    @Test
    void markAsRead_WhenMessageDoesNotExist_ShouldThrowException() {
        // Given
        Long messageId = 999L;
        when(contactMessageRepository.findById(messageId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> contactMessageService.markAsRead(messageId));
        verify(contactMessageRepository).findById(messageId);
        verify(contactMessageRepository, never()).save(any());
    }

    @Test
    void deleteMessage_WhenMessageExists_ShouldDeleteMessage() {
        // Given
        Long messageId = 1L;
        ContactMessage message = createMessage(messageId, "User", false, LocalDateTime.now());

        when(contactMessageRepository.findById(messageId))
                .thenReturn(Optional.of(message));

        // When
        contactMessageService.deleteMessage(messageId);

        // Then
        verify(contactMessageRepository).findById(messageId);
        verify(contactMessageRepository).delete(message);
    }

    @Test
    void deleteMessage_WhenMessageDoesNotExist_ShouldThrowException() {
        // Given
        Long messageId = 999L;
        when(contactMessageRepository.findById(messageId))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> contactMessageService.deleteMessage(messageId));
        verify(contactMessageRepository).findById(messageId);
        verify(contactMessageRepository, never()).delete(any());
    }

    @Test
    void save_ShouldSetReadToFalse() {
        // Given
        ContactMessageDTO dto = new ContactMessageDTO();
        dto.setName("Test");
        dto.setEmail("test@test.com");
        dto.setMessage("Test message");

        ContactMessage savedMessage = new ContactMessage();
        savedMessage.setId(1L);
        savedMessage.setRead(false);

        when(contactMessageRepository.save(any(ContactMessage.class)))
                .thenReturn(savedMessage);

        // When
        ContactMessageDTO result = contactMessageService.save(dto);

        // Then
        assertFalse(result.getRead());
    }

    // Helper method
    private ContactMessage createMessage(Long id, String name, boolean read, LocalDateTime createdAt) {
        ContactMessage message = new ContactMessage();
        message.setId(id);
        message.setName(name);
        message.setEmail(name.toLowerCase().replace(" ", "") + "@test.com");
        message.setMessage("Test message from " + name);
        message.setRead(read);
        return message;
    }
}
