package dk.bilensven;

import dk.bilensven.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testResourceNotFoundException() {
        // Test implementation
    }

    @Test
    void testValidationException() {
        // Test implementation
    }

    @Test
    void testGenericException() {
        // Test implementation
    }
}