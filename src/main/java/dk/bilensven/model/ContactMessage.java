package dk.bilensven.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Navn er påkrævet")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Email er påkrævet")
    @Email(message = "Ugyldigt e-mailformat")
    private String email;

    @Size(max = 20)
    private String phone;

    @NotBlank(message = "Besked er påkrævet")
    @Size(max = 1000, message = "Besked skal være mindre end 1000 tegn")
    private String message;

    @Column(nullable = false)
    private Boolean read = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
