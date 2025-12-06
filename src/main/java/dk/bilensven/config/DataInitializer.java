package dk.bilensven.config;

import dk.bilensven.model.Employee;
import dk.bilensven.model.Service;
import dk.bilensven.repository.EmployeeRepository;
import dk.bilensven.repository.ServiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Fylder database med test data ved startup
@Slf4j
@Component
@RequiredArgsConstructor
// Kun aktiv hvis app.data.initialize-on-startup=true
@ConditionalOnProperty(
        name = "app.data.initialize-on-startup",
        havingValue = "true",
        matchIfMissing = true
)
public class DataInitializer {

    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    // Record for service data (immutable)
    private record ServiceData(String name, String description, double price, String imageUrl) {}

    // Alle services defineret som konstant liste
    private static final List<ServiceData> SERVICES = List.of(
            new ServiceData("Olieskift", "Komplet olieskift inkl. oliefilter og ny motorolie",
                    499.00, "https://images.unsplash.com/photo-1486262715619-67b85e0b08d3?w=400"),
            new ServiceData("Bremseservice", "Kontrol og udskiftning af bremseklodser, inkl. arbejdsløn",
                    1299.00, "https://images.unsplash.com/photo-1625047509168-a7026f36de04?w=400"),
            new ServiceData("Dækskift", "Sæsonmæssigt dækskift inkl. afbalancering",
                    299.00, "https://images.unsplash.com/photo-1619642751034-765dfdf7c58e?w=400"),
            new ServiceData("Aircondition service", "Kontrol, rensning og genopfyldning af aircondition anlæg",
                    799.00, "https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=400"),
            new ServiceData("Fejlfinding", "Computerdiagnostik og fejlfinding pr. time",
                    650.00, "https://images.unsplash.com/photo-1613214149929-b3a2e9b66a2e?w=400"),
            new ServiceData("Kobling udskiftning", "Udskiftning af kobling inkl. arbejdsløn",
                    4500.00, "https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=400"),
            new ServiceData("Periodisk syn", "Forberedelse og gennemførelse af periodisk syn",
                    899.00, "https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?w=400"),
            new ServiceData("Motorservice", "Stor motorservice med udskiftning af alle væsker og filtre",
                    1999.00, "https://images.unsplash.com/photo-1487754180451-c456f719a1fc?w=400"),
            new ServiceData("Rustbehandling", "Professionel rustbehandling og undersvognsbehandling",
                    2499.00, "https://images.unsplash.com/photo-1616422285623-13ff0162193c?w=400"),
            new ServiceData("Støddæmpere", "Udskiftning af støddæmpere for og bag, inkl. arbejdsløn",
                    3200.00, "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400")
    );

    // Kører automatisk ved startup (efter dependency injection)
    @PostConstruct
    // Wrapper alt i én database transaction
    @Transactional
    public void init() {
        // Skip hvis data allerede eksisterer
        if (employeeRepository.count() > 0 || serviceRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }

        log.info("Initializing test data...");
        initializeEmployees();
        initializeServices();
        log.info("Test data initialized: {} employees, {} services",
                employeeRepository.count(), serviceRepository.count());
    }

    // Opret 3 medarbejdere med batch save
    private void initializeEmployees() {
        List<Employee> employees = List.of(
                createEmployee("Michael Hansen", "Ejer & Hovedmekaniker",
                        "michael@bilensven.dk", "+45 66 12 32 64"),
                createEmployee("Lars Nielsen", "Mekaniker",
                        "lars@bilensven.dk", "+45 54 12 63 11"),
                createEmployee("Peter Jensen", "Lærling",
                        "peter@bilensven.dk", "+45 54 23 12 56")
        );

        // Batch save (1 query i stedet for 3)
        employeeRepository.saveAll(employees);
    }

    // Opret 10 services med stream og batch save
    private void initializeServices() {
        // Transform ServiceData → Service entities
        List<Service> services = SERVICES.stream()
                .map(data -> createService(data.name(), data.description(), data.price(), data.imageUrl()))
                .toList();

        // Batch save (1 query i stedet for 10)
        serviceRepository.saveAll(services);
    }

    // Helper: Opret employee entity
    private Employee createEmployee(String name, String position, String email, String phone) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setPosition(position);
        employee.setEmail(email);
        employee.setPhone(phone);
        employee.setActive(true);
        return employee;
    }

    // Helper: Opret service entity
    private Service createService(String name, String description, double price, String imageUrl) {
        Service service = new Service();
        service.setName(name);
        service.setDescription(description);
        service.setPrice(price);
        service.setImageUrl(imageUrl);
        service.setActive(true);
        return service;
    }
}