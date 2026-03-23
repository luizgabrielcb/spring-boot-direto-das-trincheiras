package academy.devdojo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class AnimeServiceApplication {

	public static void main(String[] args) {
        var applicationContext = SpringApplication.run(AnimeServiceApplication.class, args);
		Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
	}

}
