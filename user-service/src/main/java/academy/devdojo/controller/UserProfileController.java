package academy.devdojo.controller;

import academy.devdojo.mapper.UserProfileMapper;
import academy.devdojo.response.UserGetUsersResponse;
import academy.devdojo.response.UserProfileGetResponse;
import academy.devdojo.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/user-profiles")
@Slf4j
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService service;
    private final UserProfileMapper mapper;

    @GetMapping
    public ResponseEntity<List<UserProfileGetResponse>> findAll() {
        var userProfiles = service.findAll();

        var userProfileGetResponse = mapper.toUserProfileGetResponse(userProfiles);

        return ResponseEntity.ok(userProfileGetResponse);
    }

    @GetMapping("profiles/{id}/users")
    public ResponseEntity<List<UserGetUsersResponse>> findAll(@PathVariable Long id) {
        var users = service.findUserByProfileId(id);

        var userGetUsersResponseList = mapper.toUserGetUsersResponseList(users);

        return ResponseEntity.ok(userGetUsersResponseList);
    }
}
