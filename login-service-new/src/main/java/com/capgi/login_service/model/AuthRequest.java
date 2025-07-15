package com.capgi.login_service.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//this will represent login request payload

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
	
//	@NotBlank(message = "Email must not be blank")
//    @Email(message = "Email format is invalid")
    private String email;

//    @NotBlank(message = "Password must not be blank")
    private String password;

}
