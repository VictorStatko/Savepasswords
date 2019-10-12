package com.statkovit.authorizationservice.feign;

import com.statkovit.authorizationservice.payloads.AccountDTO;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserServiceRestClient {

    @RequestMapping(method = RequestMethod.POST, value = "/internal-api/v1/accounts/request-login")
    AccountDTO requestLogin(@RequestBody SignInDTO body);

    @RequestMapping(method = RequestMethod.GET, value = "/internal-api/v1/accounts/{uuid}")
    AccountDTO getAccountData(@PathVariable("uuid") UUID uuid);
}
