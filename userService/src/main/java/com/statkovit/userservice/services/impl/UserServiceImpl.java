package com.statkovit.userservice.services.impl;

import com.statkovit.userservice.domain.User;
import com.statkovit.userservice.dto.UserDto;
import com.statkovit.userservice.events.UserCreatedEvent;
import com.statkovit.userservice.repository.UserRepository;
import com.statkovit.userservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Override
    public User create(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user = userRepository.save(user);

        applicationEventPublisher.publishEvent(
                new UserCreatedEvent(userDto, user.getUuid())
        );

        return user;
    }
}
