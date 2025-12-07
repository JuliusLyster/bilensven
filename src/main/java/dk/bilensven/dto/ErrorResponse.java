package dk.bilensven.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Error response DTO til standardiserede API fejlbeskeder
@Data
@NoArgsConstructor
public class ErrorResponse {

    // Timestamp i ISO-8601 format (2025-12-06T23:30:15)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    // HTTP status code (400, 404, 500, etc.)
    private int status;

    // HTTP status beskrivelse
    private String error;

    // Detaljeret fejlbesked
    private String message;

    // API endpoint hvor fejlen opstod
    private String path;

    // Constructor med auto-genereret timestamp
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}