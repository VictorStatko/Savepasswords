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

    String ACCOUNTS_URL = "/internal-api/v1/accounts/";

    @RequestMapping(method = RequestMethod.POST, value = ACCOUNTS_URL + "request-login")
    AccountDTO requestLogin(@RequestBody SignInDTO body);

    @RequestMapping(method = RequestMethod.GET, value = ACCOUNTS_URL + "{uuid}")
    AccountDTO getAccountData(@PathVariable("uuid") UUID uuid);
}
