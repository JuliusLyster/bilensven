package dk.bilensven.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Navn er påkrævet")
    @Size(max = 100, message = "Navnet skal være mindre end 100 tegn")
    private String name;

    @NotBlank(message = "Stilling er påkrævet")
    @Size(max = 100, message = "Stilling skal være mindre end 100 tegn")
    private String position;

    @Email(message = "Ugyldigt e-mailformat")
    @Column(unique = true)
    private String email;

    @Size(max = 20, message = "Telefonnummeret skal være på under 20 tegn")
    private String phone;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean active = true;
}
