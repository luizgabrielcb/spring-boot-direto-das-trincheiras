package academy.devdojo.commons;

import academy.devdojo.domain.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserProfileUtils {
    private final UserUtils userUtils;
    private final ProfileUtils profileUtils;

    public List<UserProfile> newUserProfileList() {
        var userProfileAdmin = newUserProfileSaved();

        return List.of(userProfileAdmin);
    }

    public UserProfile newUserProfileToSave() {
        return UserProfile.builder().user(userUtils.newUserSaved()).profile(profileUtils.newProfileSaved()).build();
    }

    public UserProfile newUserProfileSaved() {
        return UserProfile.builder().id(99L).user(userUtils.newUserSaved()).profile(profileUtils.newProfileSaved()).build();
    }
}
