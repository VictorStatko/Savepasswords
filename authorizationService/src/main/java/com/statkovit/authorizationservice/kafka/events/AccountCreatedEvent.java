package com.statkovit.authorizationservice.kafka.events;

import com.statkovit.authorizationservice.kafka.dto.AccountKafkaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccountCreatedEvent {
    public AccountKafkaDto account;
}
