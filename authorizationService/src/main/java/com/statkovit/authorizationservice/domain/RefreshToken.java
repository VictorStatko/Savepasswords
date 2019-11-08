package com.statkovit.authorizationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RedisHash("RefreshToken")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    private String id;

    @Indexed
    private UUID accountUuid;

    @Indexed
    private String opaqueToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;

    public RefreshToken(UUID accountUuid, String opaqueToken, Long ttl) {
        this.accountUuid = accountUuid;
        this.opaqueToken = opaqueToken;
        this.ttl = ttl;
    }
}
