package academy.devdojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPostRequest {
    @NotBlank(message = "This field 'firstName' is required")
    private String firstName;
    @NotBlank(message = "This field 'lastName' is required")
    private String lastName;
    @Email(regexp = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$", message = "e-mail is not valid")
    @NotBlank(message = "This field 'email' is required")
    private String email;
}