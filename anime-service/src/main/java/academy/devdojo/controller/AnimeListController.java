package academy.devdojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
public class AnimeListController {

    @GetMapping
    public List<String> listAll() {
        return List.of("Drogon Ball Z", "Kimetsu no Yaiba", "Shingeki no Kyojin");
    }
}
