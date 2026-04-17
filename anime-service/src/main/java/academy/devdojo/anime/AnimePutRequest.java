package academy.devdojo.anime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnimePutRequest {
    @NotNull(message = "The field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
