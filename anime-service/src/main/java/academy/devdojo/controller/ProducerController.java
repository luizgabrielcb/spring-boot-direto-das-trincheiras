package academy.devdojo.controller;

import academy.devdojo.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

    @GetMapping()
    public List<Producer> listAllProducers() {
        return Producer.getProducers();
    }

    @GetMapping("filterParam")
    public List<Producer> listAllProducersParam(@RequestParam String name) {
        return Producer.getProducers().stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();
    }

    @GetMapping("{id}")
    public Producer listAllProducersPathVariable(@PathVariable Long id) {
        return Producer.getProducers().stream().filter(producer -> producer.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
    headers = "x-api-key")
    public ResponseEntity<Producer> save(@RequestBody Producer producer, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);
        producer.setId(ThreadLocalRandom.current().nextLong(1000));
        Producer.getProducers().add(producer);
        var responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization", "My key");
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(producer);
    }
}
