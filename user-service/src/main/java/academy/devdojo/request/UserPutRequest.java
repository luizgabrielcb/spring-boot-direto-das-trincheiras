package academy.devdojo.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPutRequest {
    @NotNull(message = "The field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "This field 'firstName' is required")
    private String firstName;
    @NotBlank(message = "This field 'lastName' is required")
    private String lastName;
    @Email(regexp = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$", message = "The e-mail is not valid")
    @NotBlank(message = "This field 'email' is required")
    private String email;
}