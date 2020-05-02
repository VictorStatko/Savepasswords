package com.statkovit.authorizationservice.feign;

import com.statkovit.authorizationservice.payload.IpStackApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "ipstack-client", url = "http://api.ipstack.com")
public interface IpStackRestClient {
    @GetMapping(value = "/{ip}?access_key=${custom.ipstack.accessKey}")
    IpStackApiResponseDto getIpInfo(@PathVariable("ip") String ip);

    @GetMapping(value = "/check?access_key=${custom.ipstack.accessKey}")
    IpStackApiResponseDto getLocalIpInfo();
}
