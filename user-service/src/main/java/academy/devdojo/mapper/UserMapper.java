package academy.devdojo.mapper;

import academy.devdojo.domain.User;
import academy.devdojo.request.UserPostRequest;
import academy.devdojo.request.UserPutRequest;
import academy.devdojo.response.UserGetResponse;
import academy.devdojo.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    List<UserGetResponse> toUserGetResponseList(List<User> userList);

    UserGetResponse toUserGetResponse(User user);

    //    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 1000))")
    User toUser(UserPostRequest userPostRequest);

    UserPostResponse toUserPostResponse(User user);

    User toUser(UserPutRequest userPutRequest);
}