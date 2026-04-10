package academy.devdojo.user_service.mapper;

import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.request.UserPostRequest;
import academy.devdojo.user_service.request.UserPutRequest;
import academy.devdojo.user_service.response.UserGetResponse;
import academy.devdojo.user_service.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    List<UserGetResponse> toUserGetResponseList(List<User> userList);

    UserGetResponse toUserGetResponse(User user);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 1000))")
    User toUser(UserPostRequest userPostRequest);

    UserPostResponse toUserPostResponse(User user);

    User toUser(UserPutRequest userPutRequest);
}
