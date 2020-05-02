package com.statkovit.authorizationservice.controllers;

import com.statkovit.authorizationservice.constants.ServerConstants;
import com.statkovit.authorizationservice.domainServices.AuthSessionDomainService;
import com.statkovit.authorizationservice.payload.SessionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    public static final String AUTH_CONTROLLER_ROUTE = ServerConstants.AUTH_API_ROUTE;

    private final AuthSessionDomainService authSessionDomainService;

    @PostMapping(AUTH_CONTROLLER_ROUTE + "logout")
    public void logout(Principal principal) {
        authSessionDomainService.logout();
        log.debug("Successful logout for user: {}", principal.getName());
    }

    @GetMapping(AUTH_CONTROLLER_ROUTE + "/sessions")
    public ResponseEntity<List<SessionDto>> getActiveSessions() {
        List<SessionDto> sessionDtos = authSessionDomainService.getSessionsList();
        return ResponseEntity.ok(sessionDtos);
    }

    @RequestMapping(
            value = AUTH_CONTROLLER_ROUTE + "/sessions",
            method = RequestMethod.POST,
            params = "action=clear"
    )
    public ResponseEntity<?> clearSessions() {
        authSessionDomainService.clearSessionsExceptCurrent();
        return ResponseEntity.ok().build();
    }
}
