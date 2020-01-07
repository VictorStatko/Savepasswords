package com.statkovit.userservice.feign;

import com.statkovit.userservice.feign.payload.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service")
public interface AuthServiceFeignClient {

    @PostMapping(value = "/internal-api/v1/accounts")
    AccountDto createUser(@RequestBody AccountDto accountDto);
}
