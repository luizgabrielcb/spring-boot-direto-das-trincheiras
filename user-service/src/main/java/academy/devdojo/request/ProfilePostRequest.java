package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfilePostRequest {
    @NotBlank(message = "This field 'name' is required")
    private String name;
    @NotBlank(message = "This field 'description' is required")
    private String description;
}
