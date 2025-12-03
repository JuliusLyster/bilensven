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
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllActive() {
        log.info("GET /api/employees - Fetch all active employees");
        List<EmployeeDTO> employees = employeeService.getAllActive();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        log.info("GET /api/employees/{} - Fetch employee by id", id);
        EmployeeDTO employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO dto) {
        log.info("POST /api/employees - Create new employee");
        EmployeeDTO created = employeeService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO dto) {
        log.info("PUT /api/employees/{} - Update employee", id);
        EmployeeDTO updated = employeeService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/employees/{} - Delete employee", id);
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
