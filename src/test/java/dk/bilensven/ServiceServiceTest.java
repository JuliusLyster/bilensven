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

        // ✅ FIXED: Mock findByActiveTrue() instead of findAll()
        when(serviceRepository.findByActiveTrue())
                .thenReturn(Arrays.asList(active));

        // When
        List<ServiceDTO> result = serviceService.getAllActive();

        // Then
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
        verify(serviceRepository).findByActiveTrue();  // ✅ Verify correct method
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