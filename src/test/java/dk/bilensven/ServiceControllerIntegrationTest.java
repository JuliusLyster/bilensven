package dk.bilensven;

import dk.bilensven.dto.ServiceDTO;
import dk.bilensven.model.Service;
import dk.bilensven.repository.ServiceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class ServiceControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeEach
    void setup() {
        serviceRepository.deleteAll();
    }
    @AfterEach
    void cleanup() {
        serviceRepository.deleteAll();
    }

    @Test
    void getAllActive_ShouldReturnServices() {
        // Given
        Service service = new Service();
        service.setName("Test Service");
        service.setDescription("Test");
        service.setPrice(299.99);
        service.setActive(true);
        serviceRepository.save(service);

        // When
        ResponseEntity<ServiceDTO[]> response = restTemplate.getForEntity(
                "/api/services",
                ServiceDTO[].class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void create_WithValidData_ShouldReturn201() {
        // Given
        ServiceDTO dto = new ServiceDTO();
        dto.setName("New Service");
        dto.setDescription("Test Description");
        dto.setPrice(199.99);

        // When
        ResponseEntity<ServiceDTO> response = restTemplate.postForEntity(
                "/api/services",
                dto,
                ServiceDTO.class
        );

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Service", response.getBody().getName());
    }

    @Test
    void getById_WhenExists_ShouldReturnService() {
        // Given
        Service service = new Service();
        service.setName("Existing Service");
        service.setDescription("Test");
        service.setPrice(299.99);
        service.setActive(true);
        Service saved = serviceRepository.save(service);

        // When
        ResponseEntity<ServiceDTO> response = restTemplate.getForEntity(
                "/api/services/" + saved.getId(),
                ServiceDTO.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Existing Service", response.getBody().getName());
    }

    @Test
    void getById_WhenNotExists_ShouldReturn404() {
        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/services/999",
                String.class
        );

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void delete_WhenExists_ShouldSoftDelete() {
        // Given
        Service service = new Service();
        service.setName("To Delete");
        service.setDescription("Test");
        service.setPrice(99.99);
        service.setActive(true);
        Service saved = serviceRepository.save(service);

        // When
        restTemplate.delete("/api/services/" + saved.getId());

        // Then
        serviceRepository.flush();
        Service deleted = serviceRepository.findById(saved.getId()).orElseThrow();
        assertFalse(deleted.isActive());
    }
}
