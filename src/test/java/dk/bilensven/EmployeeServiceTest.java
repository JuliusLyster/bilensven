package dk.bilensven;

import dk.bilensven.dto.EmployeeDTO;
import dk.bilensven.exception.BusinessException;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.model.Employee;
import dk.bilensven.repository.EmployeeRepository;
import dk.bilensven.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getAllActive_ShouldReturnOnlyActiveEmployees() {
        // Given
        Employee active1 = new Employee();
        active1.setId(1L);
        active1.setName("John Doe");
        active1.setActive(true);

        Employee active2 = new Employee();
        active2.setId(3L);
        active2.setName("Alice Active");
        active2.setActive(true);

        // ✅ FIXED: Mock findByActiveTrue() instead of findAll()
        when(employeeRepository.findByActiveTrue())
                .thenReturn(Arrays.asList(active1, active2));

        // When
        List<EmployeeDTO> result = employeeService.getAllActive();

        // Then
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(EmployeeDTO::getActive));
        verify(employeeRepository).findByActiveTrue();  // ✅ Verify correct method
    }

    @Test
    void getById_WhenEmployeeExists_ShouldReturnEmployee() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("John Doe");

        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(employee));

        // When
        EmployeeDTO result = employeeService.getById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John Doe", result.getName());
        verify(employeeRepository).findById(id);
    }

    @Test
    void getById_WhenEmployeeDoesNotExist_ShouldThrowException() {
        // Given
        Long id = 999L;
        when(employeeRepository.findById(id))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> employeeService.getById(id));
        verify(employeeRepository).findById(id);
    }

    @Test
    void create_WhenEmailIsUnique_ShouldCreateEmployee() {
        // Given
        EmployeeDTO dto = new EmployeeDTO();
        dto.setName("New Employee");
        dto.setEmail("new@test.com");
        dto.setPosition("Developer");

        when(employeeRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setName(dto.getName());
        savedEmployee.setEmail(dto.getEmail());
        savedEmployee.setActive(true);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        // When
        EmployeeDTO result = employeeService.create(dto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.getActive());
        verify(employeeRepository).findByEmail(dto.getEmail());
        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void create_WhenEmailExists_ShouldThrowBusinessException() {
        // Given
        EmployeeDTO dto = new EmployeeDTO();
        dto.setEmail("existing@test.com");

        Employee existing = new Employee();
        existing.setEmail(dto.getEmail());

        when(employeeRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(existing));

        // When & Then
        assertThrows(BusinessException.class,
                () -> employeeService.create(dto));
        verify(employeeRepository).findByEmail(dto.getEmail());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void delete_ShouldMarkEmployeeAsInactive() {
        // Given
        Long id = 1L;
        Employee employee = new Employee();
        employee.setId(id);
        employee.setActive(true);

        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        // When
        employeeService.delete(id);

        // Then
        assertFalse(employee.isActive());  // ✅ Correct: isActive() for entity
        verify(employeeRepository).findById(id);
        verify(employeeRepository).save(employee);
    }
}