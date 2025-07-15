package com.capgi.registration_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {
	
	@NotNull(message = "Full name cannot be null")
    @Size(min = 2, message = "Full name must be at least 2 characters")
    private String fullName;
    
    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotNull(message = "Password cannot be null")
    @Size(min = 6, message = "Password should be at least 6 characters")
    private String password; // will be stored as hashed

}
