package academy.devdojo.repository;

import academy.devdojo.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHardCodedRepository {
    private final UserData userData;

    public List<User> findAll() {
        return userData.getUsers();
    }

    public List<User> findByFirstName(String firstName) {
        return findAll().stream().filter(user -> user.getFirstName().equalsIgnoreCase(firstName)).toList();
    }

    public Optional<User> findById(Long id) {
        return findAll().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public User save(User userToSave) {
        findAll().add(userToSave);
        return userToSave;
    }

    public void delete(User userToDelete) {
        findAll().remove(userToDelete);
    }

    public void update(User userToUpdate) {
        delete(userToUpdate);
        save(userToUpdate);
    }
}