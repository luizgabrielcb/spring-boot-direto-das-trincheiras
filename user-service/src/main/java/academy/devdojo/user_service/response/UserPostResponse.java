package academy.devdojo.user_service.response;

import lombok.Data;

@Data
public class UserPostResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
