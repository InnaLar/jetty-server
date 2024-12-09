package ru.larina.service;

import lombok.AllArgsConstructor;
import ru.larina.exception.ErrorCode;
import ru.larina.exception.ServiceException;
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
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserRegistrationResponse getById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, id));
        return userMapper.userToUserRegistrationResponse(user);
    }
    public UserRegistrationResponse create(UserRegistrationRequest request) {
        User user = userMapper.UserRegistrationRequestToUser(request);
        User userSaved = userRepository.save(user);
        return userMapper.userToUserRegistrationResponse(userSaved);
    }

    public UserPutResponse update(UserPutRequest request) {
        User user = userMapper.userPutRequestToUser(request);
        User userUpdated = userRepository.save(user);
        return userMapper.userToUserPutResponse(userUpdated);
    }

    public UserTaskEffortResponse getUserTaskEffort(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ServiceException(ErrorCode.ERR_CODE_001, userId));
        return userMapper.userToUserTaskEffortResponse(user);
    }
}
