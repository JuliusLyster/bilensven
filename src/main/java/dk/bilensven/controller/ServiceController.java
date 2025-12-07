package dk.bilensven.controller;

import dk.bilensven.dto.ServiceDTO;
import dk.bilensven.service.ServiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
// REST API Controller for service-håndtering (CRUD + sorting)
@RequestMapping("/api/services")
// Base URL: /api/services
@RequiredArgsConstructor
// Lombok: Auto-generate constructor for final fields
@Slf4j
// Lombok: Tilføj logger
public class ServiceController {

    private final ServiceService serviceService;

    // GET: Hent alle aktive services med optional sorting
    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllActive(
            // Query param: ?sortBy=price eller ?sortBy=name (default: name)
            @RequestParam(required = false, defaultValue = "name") String sortBy) {
        log.info("GET /api/services?sortBy={}", sortBy);

        // Conditional sorting: price (billigste først) eller name (alfabetisk)
        List<ServiceDTO> services = sortBy.equals("price")
                ? serviceService.getAllActiveSortedByPrice()
                : serviceService.getAllActive();

        return ResponseEntity.ok(services);
    }

    // GET: Hent specifik service via ID
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getById(@PathVariable Long id) {
        log.info("GET /api/services/{}", id);
        // Hvis ikke fundet → 404 Not Found exception
        ServiceDTO service = serviceService.getById(id);
        return ResponseEntity.ok(service);
    }

    // POST: Opret ny service
    @PostMapping
    public ResponseEntity<ServiceDTO> create(@Valid @RequestBody ServiceDTO dto) {
        log.info("POST /api/services - Create new service");
        // @Valid validerer DTO (NotBlank, Positive, Size, etc.)
        ServiceDTO created = serviceService.create(dto);
        // Return HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT: Opdater eksisterende service
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ServiceDTO dto) {
        log.info("PUT /api/services/{}", id);
        // Hvis ID ikke findes → 404 exception
        ServiceDTO updated = serviceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE: Slet service
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/services/{}", id);
        serviceService.delete(id);
        // Return HTTP 204 No Content
        return ResponseEntity.noContent().build();
    }
}