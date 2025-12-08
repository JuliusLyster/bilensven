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

// Service layer for medarbejder business logic
@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Hent alle aktive medarbejdere (sorteret alfabetisk)
    // FUNCTIONAL PROGRAMMING: Stream operations with database filter
    public List<EmployeeDTO> getAllActive() {
        log.info("Fetching all active employees");

        return employeeRepository.findByActiveTrue().stream()
                .sorted(Comparator.comparing(Employee::getName))
                .map(this::toDTO)  // Entity → DTO transformation
                .collect(Collectors.toList());
    }

    // Hent specifik medarbejder ved ID
    // FUNCTIONAL PROGRAMMING: Optional for null safety
    public EmployeeDTO getById(Long id) {
        log.info("Fetching employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        return toDTO(employee);
    }

    // Opret ny medarbejder
    // Business rule: Email skal være unique
    public EmployeeDTO create(EmployeeDTO dto) {
        log.info("Creating new employee: {}", dto.getName());

        Optional<Employee> existing = employeeRepository.findByEmail(dto.getEmail());
        if (existing.isPresent()) {
            throw new BusinessException("Employee with email " + dto.getEmail() + " already exists");
        }

        Employee employee = toEntity(dto);
        employee.setActive(true);  // Nye medarbejdere starter som aktive

        Employee saved = employeeRepository.save(employee);
        return toDTO(saved);
    }

    // Opdater eksisterende medarbejder
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        log.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        // Check email uniqueness hvis email ændres
        if (!employee.getEmail().equals(dto.getEmail())) {
            Optional<Employee> existing = employeeRepository.findByEmail(dto.getEmail());
            if (existing.isPresent()) {
                throw new BusinessException("Email " + dto.getEmail() + " is already in use");
            }
        }

        // Update fields
        employee.setName(dto.getName());
        employee.setPosition(dto.getPosition());
        employee.setEmail(dto.getEmail());
        employee.setPhone(dto.getPhone());
        employee.setImageUrl(dto.getImageUrl());

        Employee updated = employeeRepository.save(employee);
        return toDTO(updated);
    }

    // Slet medarbejder (soft delete - mark as inactive)
    // Data bevares for historik og audit trail
    public void delete(Long id) {
        log.info("Deleting (soft) employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

        employee.setActive(false);
        employeeRepository.save(employee);
    }

    // Konverter Entity → DTO (for API responses)
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

    // Konverter DTO → Entity (for database persistence)
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