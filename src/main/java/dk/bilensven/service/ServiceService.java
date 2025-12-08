package dk.bilensven.service;

import dk.bilensven.dto.ServiceDTO;
import dk.bilensven.exception.BusinessException;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.exception.ValidationException;
import dk.bilensven.model.Service;
import dk.bilensven.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Service layer for service/ydelse business logic
@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    // Hent alle aktive services (sorteret alfabetisk)
    // FUNCTIONAL PROGRAMMING: Stream with database filter, sorting, mapping
    public List<ServiceDTO> getAllActive() {
        log.info("Fetching all active services");

        return serviceRepository.findByActiveTrue().stream()  //
                .sorted(Comparator.comparing(Service::getName))
                .map(this::toDTO)  // Entity → DTO transformation
                .collect(Collectors.toList());
    }

    // Hent alle aktive services sorteret efter pris (billigste først)
    // FUNCTIONAL PROGRAMMING: Lambda expression for custom sorting
    public List<ServiceDTO> getAllActiveSortedByPrice() {
        log.info("Fetching all active services sorted by price");

        return serviceRepository.findByActiveTrue().stream()  //
                .sorted((s1, s2) -> Double.compare(s1.getPrice(), s2.getPrice()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Hent specifik service ved ID
    public ServiceDTO getById(Long id) {
        log.info("Fetching service with id: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        return toDTO(service);
    }

    // Opret ny service
    // Business rules: Unique name, max 2 decimals i pris
    public ServiceDTO create(ServiceDTO dto) {
        log.info("Creating new service: {}", dto.getName());

        // Check if service name already exists (business rule)
        Optional<Service> existing = serviceRepository.findByName(dto.getName());
        if (existing.isPresent()) {
            throw new BusinessException("Service with name '" + dto.getName() + "' already exists");
        }

        // Validate price has max 2 decimal places
        if (!isValidPrice(dto.getPrice())) {
            throw new ValidationException("Price must have maximum 2 decimal places");
        }

        Service service = toEntity(dto);
        service.setActive(true);  // Nye services starter som aktive

        Service saved = serviceRepository.save(service);
        return toDTO(saved);
    }

    // Opdater eksisterende service
    // Business rules: Unique name (hvis ændret), max 2 decimals i pris
    public ServiceDTO update(Long id, ServiceDTO dto) {
        log.info("Updating service with id: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        // Check name uniqueness hvis navn ændres
        if (!service.getName().equals(dto.getName())) {
            Optional<Service> existing = serviceRepository.findByName(dto.getName());
            if (existing.isPresent()) {
                throw new BusinessException("Service name '" + dto.getName() + "' already exists");
            }
        }

        // Validate price has max 2 decimal places
        if (!isValidPrice(dto.getPrice())) {
            throw new ValidationException("Price must have maximum 2 decimal places");
        }

        // Update fields
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());

        Service updated = serviceRepository.save(service);
        return toDTO(updated);
    }

    // Slet service
    // Data bevares for historik og fremtidige bookings
    public void delete(Long id) {
        log.info("Deleting (soft) service with id: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        service.setActive(false);
        serviceRepository.save(service);
    }

    // Valider at pris har max 2 decimaler
    private boolean isValidPrice(Double price) {
        if (price == null) return false;
        String priceStr = String.valueOf(price);
        int decimalIndex = priceStr.indexOf('.');
        if (decimalIndex == -1) return true;
        int decimals = priceStr.length() - decimalIndex - 1;
        return decimals <= 2;
    }

    // Konverter Entity → DTO (for API responses)
    private ServiceDTO toDTO(Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setActive(service.isActive());  // boolean → Boolean conversion
        dto.setCreatedAt(service.getCreatedAt());
        dto.setUpdatedAt(service.getUpdatedAt());
        return dto;
    }

    // Konverter DTO → Entity (for database persistence)
    private Service toEntity(ServiceDTO dto) {
        Service service = new Service();
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        // id, active, timestamps auto-handled
        return service;
    }
}