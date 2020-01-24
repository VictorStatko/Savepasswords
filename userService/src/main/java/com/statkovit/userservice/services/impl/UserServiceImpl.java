package com.statkovit.userservice.services.impl;

import com.statkolibraries.kafkaUtils.enums.KafkaTopics;
import com.statkovit.userservice.domain.OutboxEvent;
import com.statkovit.userservice.domain.User;
import com.statkovit.userservice.dto.UserDto;
import com.statkovit.userservice.feign.AuthServiceFeignClient;
import com.statkovit.userservice.feign.payload.AccountDto;
import com.statkovit.userservice.repository.OutboxEventRepository;
import com.statkovit.userservice.repository.UserRepository;
import com.statkovit.userservice.services.UserService;
import com.statkovit.userservice.util.TransactionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthServiceFeignClient authServiceFeignClient;
    private final TransactionUtils transactionUtils;
    private final UserRepository userRepository;
    private final OutboxEventRepository outboxEventRepository;

    @Override
    public Pair<User, AccountDto> create(UserDto userDto) {
        AccountDto accountDto = new AccountDto();
        accountDto.setPassword(userDto.getPassword());
        accountDto.setEmail(userDto.getEmail());
        //accountDto = authServiceFeignClient.createUser(accountDto);

        User user = saveNewUser(userDto.getName(), accountDto.getId());
        return ImmutablePair.of(user, accountDto);
    }

    private User saveNewUser(String name, Long accountId) {
        return transactionUtils.executeInTransaction(() -> {
            User user = new User();
            user.setName(name);
            user.setAccountId(accountId);

            OutboxEvent outboxEvent = new OutboxEvent(KafkaTopics.USERS.getTopicName(), "{userCreated: true}");
            outboxEventRepository.save(outboxEvent);

            return userRepository.save(user);
        });
    }
}
