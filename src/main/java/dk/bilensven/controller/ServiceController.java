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
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllActive(
            @RequestParam(required = false, defaultValue = "name") String sortBy) {
        log.info("GET /api/services?sortBy={}", sortBy);

        List<ServiceDTO> services = sortBy.equals("price")
                ? serviceService.getAllActiveSortedByPrice()
                : serviceService.getAllActive();

        return ResponseEntity.ok(services);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getById(@PathVariable Long id) {
        log.info("GET /api/services/{}", id);
        ServiceDTO service = serviceService.getById(id);
        return ResponseEntity.ok(service);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ServiceDTO>> getByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        log.info("GET /api/services/price-range?min={}&max={}", min, max);
        List<ServiceDTO> services = serviceService.findByPriceRange(min, max);
        return ResponseEntity.ok(services);
    }

    @GetMapping("/stats/total-revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        log.info("GET /api/services/stats/total-revenue");
        Double total = serviceService.calculateTotalRevenue();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/stats/average-price")
    public ResponseEntity<Double> getAveragePrice() {
        log.info("GET /api/services/stats/average-price");
        Double average = serviceService.calculateAveragePrice();
        return ResponseEntity.ok(average);
    }

    @PostMapping
    public ResponseEntity<ServiceDTO> create(@Valid @RequestBody ServiceDTO dto) {
        log.info("POST /api/services - Create new service");
        ServiceDTO created = serviceService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ServiceDTO dto) {
        log.info("PUT /api/services/{}", id);
        ServiceDTO updated = serviceService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/services/{}", id);
        serviceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}