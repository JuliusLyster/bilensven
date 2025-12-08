package dk.bilensven.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// JPA Entity for admin login
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "password")  // SECURITY: Exclude password from logs
public class User extends BaseEntity {  // Arver createdAt + updatedAt

    // Primary key (auto-increment)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // Adgangskode (BCrypt hashed, 60 characters)
    @JsonIgnore  // SECURITY: Never serialize password in JSON responses
    @Column(nullable = false, length = 60)
    private String password;

    // Brugerrolle (ADMIN eller USER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.ADMIN;
}