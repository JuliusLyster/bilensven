package dk.bilensven.controller;

import dk.bilensven.exception.BusinessException;
import dk.bilensven.exception.ResourceNotFoundException;
import dk.bilensven.exception.ValidationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestExceptionController {

    @GetMapping("/resource-not-found")
    public String testResourceNotFound() {
        throw new ResourceNotFoundException("Employee", 999L);
    }

    @GetMapping("/validation-error")
    public String testValidationError() {
        throw new ValidationException("E-mailformatet er ugyldigt");
    }

    @GetMapping("/business-error")
    public String testBusinessError() {
        throw new BusinessException("Kan ikke slette medarbejder med aktive opgaver");
    }

    @GetMapping("/generic-error")
    public String testGenericError() {
        throw new RuntimeException("Noget gik frygteligt galt!");
    }

    @GetMapping("/success")
    public String testSuccess() {
        return "Alt virker!";
    }
}