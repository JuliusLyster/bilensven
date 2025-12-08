package dk.bilensven.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA Entity for kontaktbeskeder fra website kontaktformular
@Entity
@Table(name = "contact_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage extends BaseEntity {  // Arver createdAt + updatedAt

    // Primary key (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, length = 1000)
    private String message;

    // Status: Er beskeden læst af admin?
    @Column(name = "is_read", nullable = false)
    private boolean read = false;
}