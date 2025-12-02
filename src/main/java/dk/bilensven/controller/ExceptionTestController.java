package dk.bilensven.controller;

import dk.bilensven.exception.BusinessException;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.exception.ValidationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test-exception")
public class ExceptionTestController {

    @GetMapping("/notfound")
    public String throwNotFound() {
        throw new ResourceNotFoundException("Test resource not found");
    }

    @GetMapping("/validation")
    public String throwValidation() {
        throw new ValidationException("Test validation failed");
    }

    @GetMapping("/business")
    public String throwBusiness() {
        throw new BusinessException("Business rule violation");
    }

    @GetMapping("/generic")
    public String throwGeneric() {
        throw new RuntimeException("Unexpected test error");
    }
}

