package dk.bilensven;

import dk.bilensven.dto.EmployeeDTO;
import dk.bilensven.model.Employee;
import dk.bilensven.repository.EmployeeRepository;
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

class EmployeeControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }
    @AfterEach
    void cleanup(){
        employeeRepository.deleteAll();
    }

    @Test
    void getAllActive_ShouldReturnEmployees() {
        // Given
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setPosition("Mekaniker");
        employee.setEmail("john@test.com");
        employee.setActive(true);
        employeeRepository.save(employee);

        // When
        ResponseEntity<EmployeeDTO[]> response = restTemplate.getForEntity(
                "/api/employees",
                EmployeeDTO[].class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length > 0);
    }

    @Test
    void create_WithValidData_ShouldReturn201() {
        // Given
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("Jane Smith");
        dto.setPosition("Leder");
        dto.setEmail("jane@test.com");

        // When
        ResponseEntity<EmployeeDTO> response = restTemplate.postForEntity(
                "/api/employees",
                dto,
                EmployeeDTO.class
        );

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane Smith", response.getBody().getName());
    }

    @Test
    void getById_WhenExists_ShouldReturnEmployee() {
        // Given
        Employee employee = new Employee();
        employee.setName("Test Employee");
        employee.setPosition("Test");
        employee.setEmail("test@test.com");
        employee.setActive(true);
        Employee saved = employeeRepository.save(employee);

        // When
        ResponseEntity<EmployeeDTO> response = restTemplate.getForEntity(
                "/api/employees/" + saved.getId(),
                EmployeeDTO.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Employee", response.getBody().getName());
    }

    @Test
    void delete_WhenExists_ShouldSoftDelete() {
        // Given
        Employee employee = new Employee();
        employee.setName("To Delete");
        employee.setPosition("Test");
        employee.setEmail("delete@test.com");
        employee.setActive(true);
        Employee saved = employeeRepository.save(employee);

        // When
        restTemplate.delete("/api/employees/" + saved.getId());

        // Then
        Employee deleted = employeeRepository.findById(saved.getId()).orElseThrow();
        assertFalse(deleted.isActive());
    }
}
