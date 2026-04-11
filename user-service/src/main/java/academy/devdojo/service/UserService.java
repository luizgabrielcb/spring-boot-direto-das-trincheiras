package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.repository.UserHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserHardCodedRepository repository;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findByFirstName(firstName);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
    }

    public User save(User userToSave) {
        return repository.save(userToSave);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        findByIdOrThrowNotFound(userToUpdate.getId());
        repository.update(userToUpdate);
    }
}