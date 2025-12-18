package dk.bilensven;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.bilensven.controller.ContactMessageController;
import dk.bilensven.dto.ContactMessageDTO;
import dk.bilensven.service.ContactMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ContactMessageController.class)
@AutoConfigureDataJpa
class ContactMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactMessageService contactMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void submitContactForm_WithValidData_ShouldReturn201() throws Exception {
        // Given
        ContactMessageDTO dto = new ContactMessageDTO();
        dto.setId(1L);
        dto.setName("Test User");
        dto.setEmail("test@example.com");
        dto.setMessage("This is a test message that is long enough");

        when(contactMessageService.save(any(ContactMessageDTO.class)))
                .thenReturn(dto);

        // When + Then
        mockMvc.perform(
                        post("/api/contact")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.id").value(1));
    }
}
