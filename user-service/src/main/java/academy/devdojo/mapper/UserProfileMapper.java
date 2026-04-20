package academy.devdojo.mapper;

import academy.devdojo.domain.User;
import academy.devdojo.domain.UserProfile;
import academy.devdojo.response.UserGetUsersResponse;
import academy.devdojo.response.UserProfileGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfiles);

    List<UserGetUsersResponse> toUserGetUsersResponseList(List<User> users);
}
