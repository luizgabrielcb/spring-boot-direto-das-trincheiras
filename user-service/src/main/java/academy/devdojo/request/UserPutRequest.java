package academy.devdojo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserPutRequest {
    @NotNull(message = "The field 'id' cannot be null")
    @Schema(description = "User's id", example = "1")
    private Long id;
    @NotBlank(message = "This field 'firstName' is required")
    @Schema(description = "User's first name", example = "Satoru")
    private String firstName;
    @NotBlank(message = "This field 'lastName' is required")
    @Schema(description = "User's last name", example = "Gojo")
    private String lastName;
    @Email(regexp = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$", message = "The e-mail is not valid")
    @NotBlank(message = "This field 'email' is required")
    @Schema(description = "User's email", example = "gojo.satoru@jujutsu.com")
    private String email;
    private String password;
}