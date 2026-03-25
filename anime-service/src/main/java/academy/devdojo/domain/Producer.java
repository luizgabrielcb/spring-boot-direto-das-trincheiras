package academy.devdojo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class Producer {
    private Long id;
    @JsonProperty("name")
    private String name;
    @Getter
    private static List<Producer> producers = new ArrayList<>();

    static {
        producers.add(Producer.builder().id(1L).name("Mappa").build());
        producers.add(Producer.builder().id(2L).name("Kyoto Animation").build());
        producers.add(Producer.builder().id(3L).name("Madhouse").build());
    }

}
