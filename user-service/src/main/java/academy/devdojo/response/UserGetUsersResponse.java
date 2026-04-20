package academy.devdojo.response;

import lombok.Data;

@Data
public class UserGetUsersResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}