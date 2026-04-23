package academy.devdojo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserPostResponse {
    @Schema(description = "User's id", example = "1")
    private Long id;
    @Schema(description = "User's first name", example = "Satoru")
    private String firstName;
    @Schema(description = "User's last name", example = "Gojo")
    private String lastName;
    @Schema(description = "User's email", example = "gojo.satoru@jujutsu.com")
    private String email;
}