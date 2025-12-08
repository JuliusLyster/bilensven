package dk.bilensven.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// JPA Entity for medarbejdere
@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends BaseEntity {  // Arver createdAt + updatedAt

    // Primary key (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String position;

    @Column(unique = true, length = 255)
    private String email;

    @Column(length = 20)
    private String phone;

    // Billede URL (optional)
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;
}