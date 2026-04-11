package academy.devdojo.repository;

import academy.devdojo.domain.User;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserData {
    @Getter
    private final List<User> users = new ArrayList<>();

    {
        var luiz = User.builder().id(1L).firstName("Luiz").lastName("Britto").email("luizgcb@gmail.com").build();
        var thaemy = User.builder().id(2L).firstName("Thaemy").lastName("Melo").email("thaemypmelo@gmail.com").build();
        var gisele = User.builder().id(3L).firstName("Gisele").lastName("Britto").email("giselevbritto@gmail.com").build();
        var luizCosta = User.builder().id(4L).firstName("Luiz").lastName("Costa").email("luizcosta@gmail.com").build();
        users.addAll(List.of(luiz, thaemy, gisele, luizCosta));
    }
}