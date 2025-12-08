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

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    /**
     * FUNCTIONAL PROGRAMMING: Stream with filter, sorted, map, collect
     */
    public List<ServiceDTO> getAllActive() {
        log.info("Fetching all active services");

        return serviceRepository.findAll().stream()
                .filter(Service::isActive)
                .sorted(Comparator.comparing(Service::getName))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * FUNCTIONAL PROGRAMMING: Sorting by price (Lambda expression)
     */
    public List<ServiceDTO> getAllActiveSortedByPrice() {
        log.info("Fetching all active services sorted by price");

        return serviceRepository.findAll().stream()
                .filter(Service::isActive)
                .sorted((s1, s2) -> Double.compare(s1.getPrice(), s2.getPrice()))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * FUNCTIONAL PROGRAMMING: Calculate total revenue using mapToDouble and sum
     */
    public Double calculateTotalRevenue() {
        log.info("Calculating total revenue from all active services");

        return serviceRepository.findAll().stream()
                .filter(Service::isActive)
                .mapToDouble(Service::getPrice)
                .sum();
    }

    /**
     * FUNCTIONAL PROGRAMMING: Calculate average price
     */
    public Double calculateAveragePrice() {
        log.info("Calculating average price of active services");

        return serviceRepository.findAll().stream()
                .filter(Service::isActive)
                .mapToDouble(Service::getPrice)
                .average()
                .orElse(0.0);
    }

    /**
     * FUNCTIONAL PROGRAMMING: Find services in price range
     */
    public List<ServiceDTO> findByPriceRange(Double minPrice, Double maxPrice) {
        log.info("Finding services in price range {} - {}", minPrice, maxPrice);

        return serviceRepository.findAll().stream()
                .filter(Service::isActive)
                .filter(service -> service.getPrice() >= minPrice && service.getPrice() <= maxPrice)
                .sorted(Comparator.comparing(Service::getPrice))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * FUNCTIONAL PROGRAMMING: Optional for null safety
     */
    public ServiceDTO getById(Long id) {
        log.info("Fetching service with id: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        return toDTO(service);
    }

    public ServiceDTO create(ServiceDTO dto) {
        log.info("Creating new service: {}", dto.getName());

        // Check if service name already exists
        Optional<Service> existing = serviceRepository.findByName(dto.getName());
        if (existing.isPresent()) {
            throw new BusinessException("Service with name '" + dto.getName() + "' already exists");
        }

        // Validate price has max 2 decimal places
        if (!isValidPrice(dto.getPrice())) {
            throw new ValidationException("Price must have maximum 2 decimal places");
        }

        Service service = toEntity(dto);
        service.setActive(true);

        Service saved = serviceRepository.save(service);
        return toDTO(saved);
    }

    public ServiceDTO update(Long id, ServiceDTO dto) {
        log.info("Updating service with id: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        // Validate price
        if (!isValidPrice(dto.getPrice())) {
            throw new ValidationException("Price must have maximum 2 decimal places");
        }

        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());

        Service updated = serviceRepository.save(service);
        return toDTO(updated);
    }

    public void delete(Long id) {
        log.info("Deleting (soft) service with id: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service", id));

        service.setActive(false);
        serviceRepository.save(service);
    }

    private boolean isValidPrice(Double price) {
        if (price == null) return false;
        String priceStr = String.valueOf(price);
        int decimalIndex = priceStr.indexOf('.');
        if (decimalIndex == -1) return true;
        int decimals = priceStr.length() - decimalIndex - 1;
        return decimals <= 2;
    }

    private ServiceDTO toDTO(Service service) {
        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setPrice(service.getPrice());
        dto.setActive(service.isActive());
        dto.setCreatedAt(service.getCreatedAt());
        dto.setUpdatedAt(service.getUpdatedAt());
        return dto;
    }

    private Service toEntity(ServiceDTO dto) {
        Service service = new Service();
        service.setName(dto.getName());
        service.setDescription(dto.getDescription());
        service.setPrice(dto.getPrice());
        return service;
    }
}