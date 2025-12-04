package dk.bilensven.config;

import dk.bilensven.model.Employee;
import dk.bilensven.model.Service;
import dk.bilensven.repository.EmployeeRepository;
import dk.bilensven.repository.ServiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    @Value("${app.data.initialize-on-startup:true}")
    private boolean shouldInitialize;

    @PostConstruct
    public void init() {
        if (!shouldInitialize) {
            log.info("Data initialization disabled");
            return;
        }

        // Only initialize if database is empty
        if (employeeRepository.count() == 0 && serviceRepository.count() == 0) {
            log.info("Initializing test data...");
            initializeEmployees();
            initializeServices();
            log.info("Test data initialized successfully!");
        } else {
            log.info("Database already contains data. Skipping initialization.");
        }
    }

    private void initializeEmployees() {
        // Michael Hansen - Ejer
        Employee employee1 = new Employee();
        employee1.setName("Michael Hansen");
        employee1.setPosition("Ejer & Hovedmekaniker");
        employee1.setEmail("michael@bilensven.dk");
        employee1.setPhone("+45 66 12 32 64");
        employee1.setActive(true);
        employeeRepository.save(employee1);

        // Lars Nielsen - Mekaniker
        Employee employee2 = new Employee();
        employee2.setName("Lars Nielsen");
        employee2.setPosition("Mekaniker");
        employee2.setEmail("lars@bilensven.dk");
        employee2.setPhone("+45 54 12 63 11");
        employee2.setActive(true);
        employeeRepository.save(employee2);

        // Peter Jensen - Lærling
        Employee employee3 = new Employee();
        employee3.setName("Peter Jensen");
        employee3.setPosition("Lærling");
        employee3.setEmail("peter@bilensven.dk");
        employee3.setPhone("+45 54 23 12 56");
        employee3.setActive(true);
        employeeRepository.save(employee3);

        log.info("Created {} employees", employeeRepository.count());
    }

    private void initializeServices() {
        createService(
                "Olieskift",
                "Komplet olieskift inkl. oliefilter og ny motorolie",
                499.00,
                "https://images.unsplash.com/photo-1486262715619-67b85e0b08d3?w=400"
        );

        createService(
                "Bremseservice",
                "Kontrol og udskiftning af bremseklodser, inkl. arbejdsløn",
                1299.00,
                "https://images.unsplash.com/photo-1625047509168-a7026f36de04?w=400"
        );

        createService(
                "Dækskift",
                "Sæsonmæssigt dækskift inkl. afbalancering",
                299.00,
                "https://images.unsplash.com/photo-1619642751034-765dfdf7c58e?w=400"
        );

        createService(
                "Aircondition service",
                "Kontrol, rensning og genopfyldning af aircondition anlæg",
                799.00,
                "https://images.unsplash.com/photo-1621905251918-48416bd8575a?w=400"
        );

        createService(
                "Fejlfinding",
                "Computerdiagnostik og fejlfinding pr. time",
                650.00,
                "https://images.unsplash.com/photo-1613214149929-b3a2e9b66a2e?w=400"
        );

        createService(
                "Kobling udskiftning",
                "Udskiftning af kobling inkl. arbejdsløn",
                4500.00,
                "https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=400"
        );

        createService(
                "Periodisk syn",
                "Forberedelse og gennemførelse af periodisk syn",
                899.00,
                "https://images.unsplash.com/photo-1449965408869-eaa3f722e40d?w=400"
        );

        createService(
                "Motorservice",
                "Stor motorservice med udskiftning af alle væsker og filtre",
                1999.00,
                "https://images.unsplash.com/photo-1487754180451-c456f719a1fc?w=400"
        );

        createService(
                "Rustbehandling",
                "Professionel rustbehandling og undersvognsbehandling",
                2499.00,
                "https://images.unsplash.com/photo-1616422285623-13ff0162193c?w=400"
        );

        createService(
                "Støddæmpere",
                "Udskiftning af støddæmpere for og bag, inkl. arbejdsløn",
                3200.00,
                "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?w=400"
        );

        log.info("Created {} services", serviceRepository.count());
    }

    private void createService(String name, String description, double price, String imageUrl) {
        Service service = new Service();
        service.setName(name);
        service.setDescription(description);
        service.setPrice(price);
        service.setImageUrl(imageUrl);
        service.setActive(true);
        serviceRepository.save(service);
    }
}