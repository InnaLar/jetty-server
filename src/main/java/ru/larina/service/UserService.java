package ru.larina.service;

import lombok.AllArgsConstructor;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.model.dto.userReportDTO.UserTaskEffortResponse;
import ru.larina.model.entity.User;
import ru.larina.repository.UserRepository;

@AllArgsConstructor
public class UserService {
    final private UserRepository userRepository;

    public UserRegistrationResponse create(UserRegistrationRequest request) {
        //todo методы маппера должны быть нестатическими
        User user = UserMapper.UserRegistrationRequestToUser(request);
        User userSaved = userRepository.save(user);
        return UserMapper.userToUserRegistrationResponse(userSaved);
    }

    public UserPutResponse update(UserPutRequest request) {
        User user = UserMapper.userPutRequestToUser(request);
        User userUpdated = userRepository.save(user);
        return UserMapper.userToUserPutResponse(userUpdated);
    }

    public UserTaskEffortResponse getUserTaskEffort(Long userId) {
        User user = userRepository.findById(userId).get();
        return UserMapper.userToUserTaskEffortResponse(user);
    }
}
