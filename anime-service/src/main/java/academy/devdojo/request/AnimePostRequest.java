package academy.devdojo.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AnimePostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}