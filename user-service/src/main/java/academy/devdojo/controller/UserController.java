package academy.devdojo.controller;

import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        var userList = service.findAll(firstName);

        var userGetResponseList = userMapper.toUserGetResponseList(userList);

        return ResponseEntity.ok(userGetResponseList);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        var user = service.findByIdOrThrowNotFound(id);

        var userGetResponse = userMapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody UserPostRequest postRequest) {
        var userToSave = userMapper.toUser(postRequest);

        var userSaved = service.save(userToSave);

        var userPostResponse = userMapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UserPutRequest putRequest) {
        var userToUpdate = userMapper.toUser(putRequest);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }
}