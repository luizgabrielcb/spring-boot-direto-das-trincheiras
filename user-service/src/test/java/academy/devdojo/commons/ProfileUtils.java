package academy.devdojo.commons;

import academy.devdojo.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileUtils {
    public List<Profile> newProfileList() {
        var admin = Profile.builder().id(1L).name("Administrator").description("Admins everything").build();
        var manager = Profile.builder().id(2L).name("Manager").description("Manages users").build();
        return List.of(admin, manager);
    }
}
