package dk.bilensven;

import dk.bilensven.dto.ServiceDTO;
import dk.bilensven.exception.BusinessException;
import dk.bilensven.model.Service;
import dk.bilensven.repository.ServiceRepository;
import dk.bilensven.service.ServiceService;
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
class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    @Test
    void getAllActive_ShouldReturnOnlyActiveServices() {
        // Given
        Service active = new Service();
        active.setId(1L);
        active.setName("Oil Change");
        active.setPrice(299.99);
        active.setActive(true);

        Service inactive = new Service();
        inactive.setId(2L);
        inactive.setName("Old Service");
        inactive.setActive(false);

        when(serviceRepository.findAll())
                .thenReturn(Arrays.asList(active, inactive));

        // When
        List<ServiceDTO> result = serviceService.getAllActive();

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
        verify(serviceRepository).findAll();
    }

    @Test
    void calculateTotalRevenue_ShouldSumAllActivePrices() {
        // Given
        Service service1 = new Service();
        service1.setPrice(100.0);
        service1.setActive(true);

        Service service2 = new Service();
        service2.setPrice(200.0);
        service2.setActive(true);

        Service inactive = new Service();
        inactive.setPrice(50.0);
        inactive.setActive(false);

        when(serviceRepository.findAll())
                .thenReturn(Arrays.asList(service1, service2, inactive));

        // When
        Double total = serviceService.calculateTotalRevenue();

        // Then
        assertEquals(300.0, total, 0.01);
        verify(serviceRepository).findAll();
    }

    @Test
    void calculateAveragePrice_ShouldReturnCorrectAverage() {
        // Given
        Service service1 = new Service();
        service1.setPrice(100.0);
        service1.setActive(true);

        Service service2 = new Service();
        service2.setPrice(200.0);
        service2.setActive(true);

        when(serviceRepository.findAll())
                .thenReturn(Arrays.asList(service1, service2));

        // When
        Double average = serviceService.calculateAveragePrice();

        // Then
        assertEquals(150.0, average, 0.01);
    }

    @Test
    void findByPriceRange_ShouldFilterCorrectly() {
        // Given
        Service cheap = new Service();
        cheap.setId(1L);
        cheap.setName("Cheap");
        cheap.setPrice(50.0);
        cheap.setActive(true);

        Service medium = new Service();
        medium.setId(2L);
        medium.setName("Medium");
        medium.setPrice(150.0);
        medium.setActive(true);

        Service expensive = new Service();
        expensive.setId(3L);
        expensive.setName("Expensive");
        expensive.setPrice(500.0);
        expensive.setActive(true);

        when(serviceRepository.findAll())
                .thenReturn(Arrays.asList(cheap, medium, expensive));

        // When
        List<ServiceDTO> result = serviceService.findByPriceRange(100.0, 200.0);

        // Then
        assertEquals(1, result.size());
        assertEquals("Medium", result.get(0).getName());
    }

    @Test
    void create_WithValidPrice_ShouldCreateService() {
        // Given
        ServiceDTO dto = new ServiceDTO();
        dto.setName("New Service");
        dto.setPrice(199.99);
        dto.setDescription("Test service");

        when(serviceRepository.findByName(dto.getName()))
                .thenReturn(Optional.empty());

        Service saved = new Service();
        saved.setId(1L);
        saved.setName(dto.getName());
        saved.setPrice(dto.getPrice());
        saved.setActive(true);

        when(serviceRepository.save(any(Service.class)))
                .thenReturn(saved);

        // When
        ServiceDTO result = serviceService.create(dto);

        // Then
        assertNotNull(result);
        assertEquals(199.99, result.getPrice());
        assertTrue(result.getActive());
        verify(serviceRepository).save(any(Service.class));
    }

    @Test
    void create_WithDuplicateName_ShouldThrowException() {
        // Given
        ServiceDTO dto = new ServiceDTO();
        dto.setName("Existing Service");
        dto.setPrice(100.0);

        Service existing = new Service();
        existing.setName(dto.getName());

        when(serviceRepository.findByName(dto.getName()))
                .thenReturn(Optional.of(existing));

        // When & Then
        assertThrows(BusinessException.class,
                () -> serviceService.create(dto));
        verify(serviceRepository, never()).save(any());
    }
}