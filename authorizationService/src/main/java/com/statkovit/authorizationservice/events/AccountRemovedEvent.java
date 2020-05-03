package com.statkovit.authorizationservice.events;

import com.statkovit.authorizationservice.payload.kafka.AccountKafkaDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AccountRemovedEvent {
    public AccountKafkaDto account;
}
