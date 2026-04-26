package academy.devdojo.service;

import academy.devdojo.domain.User;
import academy.devdojo.exception.EmailAlreadyExistsException;
import academy.devdojo.exception.NotFoundException;
import academy.devdojo.repository.UserProfileRepository;
import academy.devdojo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserProfileRepository userProfileRepository;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not Found"));
    }

    public User save(User userToSave) {
        assertEmailDoesNotExist(userToSave.getEmail());
        return repository.save(userToSave);
    }

    @Transactional
    public void delete(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        userProfileRepository.deleteByUserId(userToDelete.getId());
        repository.delete(userToDelete);
    }

    public void update(User userToUpdate) {
        assertEmailDoesNotExist(userToUpdate.getEmail(), userToUpdate.getId());
        var userSaved = findByIdOrThrowNotFound(userToUpdate.getId());
        userToUpdate.setRoles(userSaved.getRoles());
        if (userToUpdate.getPassword() == null) {
            userToUpdate.setPassword(userSaved.getPassword());
        }
        repository.save(userToUpdate);
    }

    private void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(user -> throwEmailExistsException());
    }

    private void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(user -> throwEmailExistsException());
    }

    private static void throwEmailExistsException() {
        throw new EmailAlreadyExistsException("Email already exists");
    }
}