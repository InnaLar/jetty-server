package ru.larina.service;

import lombok.AllArgsConstructor;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
import ru.larina.mapper.UserMapper;
import ru.larina.model.dto.userClearDTO.UserDeleteTasksResponse;
import ru.larina.model.dto.userDTO.UserPutRequest;
import ru.larina.model.dto.userDTO.UserPutResponse;
import ru.larina.model.dto.userDTO.UserRegistrationRequest;
import ru.larina.model.dto.userDTO.UserRegistrationResponse;
import ru.larina.model.entity.User;
import ru.larina.repository.UserRepository;

@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRegistrationResponse getById(final Long id) {
        final User user = userRepository.findById(id)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, id));
        return userMapper.userToUserRegistrationResponse(user);
    }

    public UserRegistrationResponse create(final UserRegistrationRequest request) {
        final User user = userMapper.userRegistrationRequestToUser(request);
        final User userSaved = userRepository.save(user);
        return userMapper.userToUserRegistrationResponse(userSaved);
    }

    public UserPutResponse update(final UserPutRequest request) {
        final User user = userMapper.userPutRequestToUser(request);
        final User userUpdated = userRepository.save(user);
        return userMapper.userToUserPutResponse(userUpdated);
    }

    public UserDeleteTasksResponse deleteTasksByUser(final Long id) {
        userRepository.deleteTasksByUser(id);
        final User user = User.builder()
            .id(id)
            .build();
        return userMapper.userToUserDeleteTasksResponse(user);
    }
}
