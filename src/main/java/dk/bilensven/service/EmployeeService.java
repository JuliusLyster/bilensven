package dk.bilensven.service;

import dk.bilensven.dto.EmployeeDTO;
import dk.bilensven.exception.BusinessException;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.model.Employee;
import dk.bilensven.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * FUNCTIONAL PROGRAMMING: Stream operations with filter, map, collect
     */
    public List<EmployeeDTO> getAllActive() {
        log.info("Fetching all active employees");

        return employeeRepository.findAll().stream()
                .filter(Employee::isActive)  // Filter only active
                .sorted(Comparator.comparing(Employee::getName))  // Sort by name
                .map(this::toDTO)  // Transform to DTO
                .collect(Collectors.toList());  // Collect to list
    }

    public EmployeeDTO getById(Long id) {
        log.info("Fetching employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        return toDTO(employee);
    }

    /**
     * FUNCTIONAL PROGRAMMING: Optional for null safety
     */
    public EmployeeDTO create(EmployeeDTO dto) {
        log.info("Creating new employee: {}", dto.getName());

        // Check if email already exists
        Optional<Employee> existing = employeeRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new BusinessException("Employee with email " + dto.getEmail() + " already exists");
        }

        Employee employee = toEntity(dto);
        employee.setActive(true);

        Employee saved = employeeRepository.save(employee);
        return toDTO(saved);
    }

    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        log.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        // Update fields
        employee.setName(dto.getName());
        employee.setPosition(dto.getPosition());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setImageUrl(dto.getImageUrl());

        Employee updated = employeeRepository.save(employee);
        return toDTO(updated);
    }

    /**
     * Soft delete - mark as inactive
     */
    public void delete(Long id) {
        log.info("Deleting (soft) employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    /**
     * FUNCTIONAL PROGRAMMING: Method reference in mapping
     */
    private EmployeeDTO toDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setPosition(employee.getPosition());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setImageUrl(employee.getImageUrl());
        dto.setActive(employee.isActive());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());
        return dto;
    }

    private Employee toEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setPosition(dto.getPosition());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setImageUrl(dto.getImageUrl());
        return employee;
    }
}
