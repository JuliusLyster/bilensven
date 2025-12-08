package dk.bilensven.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// Abstract base class for alle entities med fælles audit fields
@MappedSuperclass
// Spring Data JPA auditing listener (auto-set timestamps)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity {

    // Timestamp: auto-set ved persist
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Timestamp: Hauto-set ved update, null hvis aldrig opdateret
    @LastModifiedDate
    private LocalDateTime updatedAt;
}