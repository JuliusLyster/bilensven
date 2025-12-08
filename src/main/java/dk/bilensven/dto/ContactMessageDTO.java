package dk.bilensven.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
// Lombok: Auto-generate getters, setters, toString, equals, hashCode
@NoArgsConstructor
// Lombok: Default constructor (nødvendig for JSON deserialization)
@AllArgsConstructor
// Lombok: Constructor med alle felter (nyttigt til Entity → DTO conversion)
public class ContactMessageDTO {

    // ID (auto-genereret af database)
    private Long id;

    @NotBlank(message = "Navn er påkrævet")
    @Size(max = 100, message = "Navnet skal være mindre end 100 tegn")
    private String name;

    @NotBlank(message = "E-mailadresse er påkrævet")
    @Email(message = "Ugyldigt e-mailformat")
    private String email;

    @Size(max = 20, message = "Telefonnummeret skal være på under 20 tegn")
    private String phone;

    @NotBlank(message = "Besked er påkrævet")
    @Size(min = 10, max = 1000, message = "Beskeden skal være mellem 10 og 1000 tegn lang")
    private String message;

    private Boolean read;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}