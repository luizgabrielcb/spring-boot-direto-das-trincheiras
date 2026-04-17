package academy.devdojo.producer;

import academy.devdojo.domain.Producer;
import academy.devdojo.exception.NameAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final ProducerRepository repository;

    public List<Producer> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByNameIgnoreCase(name);
    }

    public Producer findByIdOrElseThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producer not Found"));
    }

    public Producer save(Producer producer) {
        assertProducerExistsByName(producer.getName());
        return repository.save(producer);
    }

    public void delete(Long id) {
        var producerToDelete = findByIdOrElseThrowNotFound(id);
        repository.delete(producerToDelete);
    }

    public void update(Producer producerToUpdate) {
        assertProducerExistsById(producerToUpdate.getId());
        assertProducerExistsByName(producerToUpdate.getName());
        repository.save(producerToUpdate);
    }

    private void assertProducerExistsById(Long id) {
        findByIdOrElseThrowNotFound(id);
    }

    private void assertProducerExistsByName(String name) {
        var animeList = repository.findByNameIgnoreCase(name);
        if (!animeList.isEmpty()) {
            throw new NameAlreadyExistsException("Producer with name " + name + " already exists");
        }
    }
}
