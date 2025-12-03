package dk.bilensven.config;

import dk.bilensven.model.Employee;
import dk.bilensven.model.Service;
import dk.bilensven.repository.EmployeeRepository;
import dk.bilensven.repository.ServiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    @PostConstruct
    public void init() {
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

        Employee employee1 = new Employee();
        employee1.setName("Michael Hansen");
        employee1.setPosition("Ejer & Hovedmekaniker");
        employee1.setEmail("michael@bilensven.dk");
        employee1.setPhone("+45 XX XX XX XX");  // TODO: Rigtig telefonnummer
        employee1.setActive(true);
        employeeRepository.save(employee1);

        Employee employee2 = new Employee();
        employee2.setName("Lars Nielsen");
        employee2.setPosition("Mekaniker");
        employee2.setEmail("lars@bilensven.dk");
        employee2.setPhone("+45 XX XX XX XX");  // TODO: Rigtig telefonnummer
        employee2.setActive(true);
        employeeRepository.save(employee2);

        Employee employee3 = new Employee();
        employee3.setName("Peter Jensen");
        employee3.setPosition("Lærling");
        employee3.setEmail("peter@bilensven.dk");
        employee3.setPhone("+45 XX XX XX XX");  // TODO: Rigtig telefonnummer
        employee3.setActive(true);
        employeeRepository.save(employee3);

        log.info("Created {} employees", employeeRepository.count());
    }

    private void initializeServices() {
        // TODO: Få rigtige service priser fra Michael (kunden)
        // Dette er typiske bilværksted services

        createService(
                "Olieskift",
                "Komplet olieskift inkl. filter og kontrol af væskeniveauer",
                new BigDecimal("499.00")
        );

        createService(
                "Bremseservice",
                "Kontrol og udskiftning af bremseklodser, inkl. arbejdsløn",
                new BigDecimal("1299.00")
        );

        createService(
                "Dækskift",
                "Sæsonmæssigt dækskift inkl. afbalancering",
                new BigDecimal("299.00")
        );

        createService(
                "Aircondition service",
                "Kontrol, rensning og genopfyldning af aircondition anlæg",
                new BigDecimal("799.00")
        );

        createService(
                "Periodisk syn",
                "Forberedelse og gennemførelse af periodisk syn",
                new BigDecimal("899.00")
        );

        createService(
                "Rustbehandling",
                "Professionel rustbehandling og undervognsbehandling",
                new BigDecimal("2499.00")
        );

        createService(
                "Kobling udskiftning",
                "Udskiftning af kobling inkl. arbejdsløn",
                new BigDecimal("4500.00")
        );

        createService(
                "Motorservice",
                "Stor motorservice med udskiftning af alle væsker og filtre",
                new BigDecimal("1999.00")
        );

        createService(
                "Støddæmpere",
                "Udskiftning af støddæmpere for og bag, inkl. arbejdsløn",
                new BigDecimal("3200.00")
        );

        createService(
                "Fejlfinding",
                "Computerdiagnostik og fejlfinding pr. time",
                new BigDecimal("650.00")
        );

        log.info("Created {} services", serviceRepository.count());
    }

    private void createService(String name, String description, BigDecimal price) {
        Service service = new Service();
        service.setName(name);
        service.setDescription(description);
        service.setPrice(price.doubleValue());
        service.setActive(true);
        serviceRepository.save(service);
    }
}
