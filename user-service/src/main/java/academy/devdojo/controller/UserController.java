package academy.devdojo.controller;

import academy.devdojo.exception.DefaultErrorMessage;
import academy.devdojo.mapper.UserMapper;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import academy.devdojo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User API", description = "User related endpoints")
public class UserController {
    private final UserService service;
    private final UserMapper userMapper;

    @GetMapping()
    @Operation(summary = "Get all users", description = "Get all users available in the system",
            responses = {
                    @ApiResponse(description = "List all users",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserGetResponse.class)))
                    )
            })
    public ResponseEntity<List<UserGetResponse>> findAll(@RequestParam(required = false) String firstName) {
        var userList = service.findAll(firstName);

        var userGetResponseList = userMapper.toUserGetResponseList(userList);

        return ResponseEntity.ok(userGetResponseList);
    }

    @GetMapping("{id}")
    @Operation(summary = "Get user by id",
            responses = {
                    @ApiResponse(description = "Get user by its id",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = UserGetResponse.class)))
                    ),
                    @ApiResponse(description = "User Not Found",
                            responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = DefaultErrorMessage.class)))
            })
    public ResponseEntity<UserGetResponse> findById(@PathVariable Long id) {
        var user = service.findByIdOrThrowNotFound(id);

        var userGetResponse = userMapper.toUserGetResponse(user);

        return ResponseEntity.ok(userGetResponse);
    }

    @PostMapping
    public ResponseEntity<UserPostResponse> save(@RequestBody @Valid UserPostRequest postRequest) {
        var userToSave = userMapper.toUser(postRequest);

        var userSaved = service.save(userToSave);

        var userPostResponse = userMapper.toUserPostResponse(userSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(userPostResponse);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid UserPutRequest putRequest) {
        var userToUpdate = userMapper.toUser(putRequest);

        service.update(userToUpdate);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}