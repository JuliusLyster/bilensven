package dk.bilensven.controller;

import dk.bilensven.dto.EmployeeDTO;
import dk.bilensven.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
// REST API Controller for medarbejder-håndtering (CRUD)
@RequestMapping("/api/employees")
// Base URL: /api/employees
@RequiredArgsConstructor
// Lombok: Auto-generate constructor for final fields
@Slf4j
// Lombok: Tilføj logger
public class EmployeeController {

    private final EmployeeService employeeService;

    // GET: Hent alle aktive medarbejdere
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllActive() {
        log.info("GET /api/employees - Fetch all active employees");
        List<EmployeeDTO> employees = employeeService.getAllActive();
        return ResponseEntity.ok(employees);
    }

    // GET: Hent specifik medarbejder via ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        log.info("GET /api/employees/{} - Fetch employee by id", id);
        // Hvis ikke fundet → 404 Not Found exception
        EmployeeDTO employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }

    // POST: Opret ny medarbejder
    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO dto) {
        log.info("POST /api/employees - Create new employee");
        // @Valid validerer DTO (NotBlank, Email, Size, etc.)
        EmployeeDTO created = employeeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT: Opdater eksisterende medarbejder
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {
        log.info("PUT /api/employees/{} - Update employee", id);
        // Hvis ID ikke findes → 404 exception
        EmployeeDTO updated = employeeService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE: Slet medarbejder
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/employees/{} - Delete employee", id);
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}