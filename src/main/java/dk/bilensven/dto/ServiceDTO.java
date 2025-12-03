package dk.bilensven.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {

    private Long id;

    @NotBlank(message = "Navn er påkrævet")
    @Size(max = 100, message = "Navnet skal være mindre end 100 tegn")
    private String name;

    @Size(max = 500, message = "Beskrivelsen skal være mindre end 500 tegn")
    private String description;

    @NotNull(message = "Pris er påkrævet")
    @Positive(message = "Prisen skal være positiv")
    private Double price;

    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}