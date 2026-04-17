package academy.devdojo.producer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {
    private final ProducerMapper mapper;
    private final ProducerService service;

    @GetMapping()
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
        var producers = service.findAll(name);

        var producerGetResponse = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        var producer = service.findByIdOrElseThrowNotFound(id);

        var producerGetResponse = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "x-api-key")
    public ResponseEntity<ProducerPostResponse> save(@RequestBody @Valid ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("{}", headers);

        var producer = mapper.toProducer(producerPostRequest);

        var producerSaved = service.save(producer);

        var producerPostResponse = mapper.toProducerPostResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerPostResponse);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid ProducerPutRequest request) {
        log.debug("Request to update producer: {}", request);

        var producerToUpdate = mapper.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Request to delete producer by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}