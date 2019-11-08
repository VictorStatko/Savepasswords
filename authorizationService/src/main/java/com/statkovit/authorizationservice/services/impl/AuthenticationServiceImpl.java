package com.statkovit.authorizationservice.services.impl;

import com.statkolibraries.exceptions.exceptions.UnauthorizedException;
import com.statkolibraries.jwtprocessing.JwsCreator;
import com.statkolibraries.jwtprocessing.payload.TokenData;
import com.statkolibraries.utils.DateTimeUtils;
import com.statkolibraries.utils.SecuredRandomStringGenerator;
import com.statkovit.authorizationservice.domain.AccessToken;
import com.statkovit.authorizationservice.domain.RefreshToken;
import com.statkovit.authorizationservice.feign.UserServiceRestClient;
import com.statkovit.authorizationservice.payloads.AccountDTO;
import com.statkovit.authorizationservice.payloads.SignInDTO;
import com.statkovit.authorizationservice.properties.JwtProperties;
import com.statkovit.authorizationservice.repositories.AccessTokenRepository;
import com.statkovit.authorizationservice.repositories.RefreshTokenRepository;
import com.statkovit.authorizationservice.services.AuthenticationService;
import com.statkovit.authorizationservice.services.transfer.CurrentAccountTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final int OPAQUE_TOKENS_LENGTH = 21;

    private final UserServiceRestClient userServiceRestClient;
    private final JwtProperties jwtProperties;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public CurrentAccountTokens signIn(SignInDTO signInDTO) {
        AccountDTO accountDTO = userServiceRestClient.requestLogin(signInDTO);
        UUID accountUuid = accountDTO.getUuid();
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration());

        AccessToken accessToken = createAccessToken(accountDTO, accessTokenExpiration);
        RefreshToken refreshToken = createRefreshToken(accountDTO);

        accessTokenRepository.findByAccountUuid(accountUuid).ifPresent(accessTokenRepository::delete);
        refreshTokenRepository.findByAccountUuid(accountUuid).ifPresent(refreshTokenRepository::delete);

        accessTokenRepository.save(accessToken);
        refreshTokenRepository.save(refreshToken);

        return new CurrentAccountTokens(
                accessToken.getOpaqueToken(), refreshToken.getOpaqueToken(), DateTimeUtils.dateToHttpFormat(accessTokenExpiration)
        );
    }

    @Override
    public CurrentAccountTokens refresh(String refreshToken) {
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByOpaqueToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Refresh token " + refreshToken + " was not found."));

        UUID accountUuid = refreshTokenEntity.getAccountUuid();
        AccountDTO accountDTO = userServiceRestClient.getAccountData(accountUuid);
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration());

        AccessToken accessToken = createAccessToken(accountDTO, accessTokenExpiration);

        accessTokenRepository.findByAccountUuid(accountUuid).ifPresent(accessTokenRepository::delete);
        accessTokenRepository.save(accessToken);

        return new CurrentAccountTokens(
                accessToken.getOpaqueToken(), refreshTokenEntity.getOpaqueToken(), DateTimeUtils.dateToHttpFormat(accessTokenExpiration)
        );
    }

    private AccessToken createAccessToken(AccountDTO accountDTO, Date expirationDate) {
        SecuredRandomStringGenerator opaqueTokenGenerator = new SecuredRandomStringGenerator(OPAQUE_TOKENS_LENGTH);

        JwsCreator jwsCreator = new JwsCreator(
                jwtProperties.getPrivateKey(),
                expirationDate
        );

        TokenData tokenData = new TokenData(
                accountDTO.getId(), accountDTO.getUuid(), accountDTO.getRoles(), accountDTO.getPermissions()
        );

        String jwtAccessToken = jwsCreator.generateToken(tokenData);
        String opaqueAccessToken = opaqueTokenGenerator.generate();

        return new AccessToken(
                accountDTO.getUuid(), jwtAccessToken, opaqueAccessToken, jwtProperties.getAccessTokenExpiration()
        );
    }

    private RefreshToken createRefreshToken(AccountDTO accountDTO) {
        SecuredRandomStringGenerator opaqueTokenGenerator = new SecuredRandomStringGenerator(OPAQUE_TOKENS_LENGTH);

        String opaqueRefreshToken = opaqueTokenGenerator.generate();

        return new RefreshToken(
                accountDTO.getUuid(), opaqueRefreshToken, jwtProperties.getRefreshTokenExpiration()
        );

    }
}
