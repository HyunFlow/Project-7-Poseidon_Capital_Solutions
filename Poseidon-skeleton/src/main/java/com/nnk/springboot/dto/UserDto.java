package com.nnk.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private Integer id;

    @NotBlank(message = "FullName is mandatory")
    @Size(max = 125)
    private String fullName;

    @NotBlank(message = "Username is mandatory")
    @Size(max = 125)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 125, message = "Password must be between 8 and 125 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$", message = "Password must contain at least one uppercase letter, one number, and one special character")
    private String password;

    @NotBlank(message = "Role is mandatory")
    private String role;
}
