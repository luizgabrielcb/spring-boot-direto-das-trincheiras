package academy.devdojo.controller;

import academy.devdojo.mapper.ProfileMapper;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import academy.devdojo.service.ProfileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/profiles")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class ProfileController {
    private final ProfileService service;
    private final ProfileMapper mapper;

    @PostMapping
    public ResponseEntity<ProfilePostResponse> save(@RequestBody @Valid ProfilePostRequest profilePostRequest) {
        log.info("Saving profile {}", profilePostRequest);

        var profileToSave = mapper.toProfile(profilePostRequest);

        var profileSavedReturn = service.save(profileToSave);

        var profilePostResponse = mapper.toProfilePostResponse(profileSavedReturn);

        return ResponseEntity.status(HttpStatus.CREATED).body(profilePostResponse);
    }

    @GetMapping
    public ResponseEntity<List<ProfileGetResponse>> findAll() {
        var profileList = service.findAll();

        var profileGetResponseList = mapper.toProfileGetResponseList(profileList);

        return ResponseEntity.ok(profileGetResponseList);
    }
}
