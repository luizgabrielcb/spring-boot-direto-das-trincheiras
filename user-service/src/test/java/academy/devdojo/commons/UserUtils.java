package academy.devdojo.commons;

import academy.devdojo.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserUtils {
    public List<User> newUserList() {
        var gojo = User.builder()
                .id(1L)
                .firstName("Satoro")
                .lastName("Gojo")
                .email("satoru@jujutsu.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$aTnFCSeFhSdlHonVvpGn8Oq/ku.//.VGLm8cTwO7ZxjOxFi03pqjO")
                .build();
        var itadori = User.builder()
                .id(2L)
                .firstName("Yudi")
                .lastName("Itadori")
                .email("yuditadori@jjts.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$aTnFCSeFhSdlHonVvpGn8Oq/ku.//.VGLm8cTwO7ZxjOxFi03pqjO")
                .build();
        var naruto = User.builder()
                .id(3L)
                .firstName("Naruto")
                .lastName("Uzumaki")
                .email("narutinho@sasuke.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$aTnFCSeFhSdlHonVvpGn8Oq/ku.//.VGLm8cTwO7ZxjOxFi03pqjO")
                .build();
        var konoha = User.builder()
                .id(4L)
                .firstName("Konoha")
                .lastName("Folha")
                .email("folhakk@charuto.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$aTnFCSeFhSdlHonVvpGn8Oq/ku.//.VGLm8cTwO7ZxjOxFi03pqjO")
                .build();

        return List.of(gojo, itadori, naruto, konoha);
    }

    public User newUserToSave() {
        return User.builder()
                .firstName("Itachi")
                .lastName("Uchiha")
                .email("itachi@folha.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$aTnFCSeFhSdlHonVvpGn8Oq/ku.//.VGLm8cTwO7ZxjOxFi03pqjO")
                .build();
    }

    public User newUserSaved() {
        return User.builder()
                .id(99L)
                .firstName("Itachi")
                .lastName("Uchiha")
                .email("itachi@folha.com")
                .roles("USER")
                .password("{bcrypt}$2a$10$aTnFCSeFhSdlHonVvpGn8Oq/ku.//.VGLm8cTwO7ZxjOxFi03pqjO")
                .build();
    }
}
