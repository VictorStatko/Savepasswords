package com.statkovit.authorizationservice.services.impl;

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
import com.statkovit.authorizationservice.services.transfer.TokensCreationResult;
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
    public TokensCreationResult signIn(SignInDTO signInDTO) {
        AccountDTO accountDTO = userServiceRestClient.requestLogin(signInDTO);
        UUID accountUuid = accountDTO.getUuid();

        SecuredRandomStringGenerator opaqueTokenGenerator = new SecuredRandomStringGenerator(OPAQUE_TOKENS_LENGTH);
        Date accessTokenExpiration = new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration());

        String jwtAccessToken = createJwtAccessToken(accountDTO, accessTokenExpiration);
        String opaqueAccessToken = opaqueTokenGenerator.generate();
        String opaqueRefreshToken = opaqueTokenGenerator.generate();

        AccessToken accessToken = new AccessToken(
                accountUuid, jwtAccessToken, opaqueAccessToken, jwtProperties.getAccessTokenExpiration()
        );

        RefreshToken refreshToken = new RefreshToken(
                accountUuid, opaqueRefreshToken, jwtProperties.getRefreshTokenExpiration()
        );

        accessTokenRepository.findByAccountUuid(accountUuid).forEach(accessTokenRepository::delete);
        refreshTokenRepository.findByAccountUuid(accountUuid).forEach(refreshTokenRepository::delete);

        accessTokenRepository.save(accessToken);
        refreshTokenRepository.save(refreshToken);

        return new TokensCreationResult(opaqueAccessToken, opaqueRefreshToken, DateTimeUtils.dateToHttpFormat(accessTokenExpiration));
    }

   /* @Override
    public void refresh(String refreshToken, HttpServletResponse httpServletResponse) {

        JwsProcessor jwsProcessor = new JwsProcessor(jwtProperties.getPublicKey());
        TokenData tokenData = jwsProcessor.readToken(refreshToken);
        //TODO get data from redis by refresh token
        UUID uuid = UUID.randomUUID();

        AccountDTO accountDTO = userServiceRestClient.getAccountData(uuid);

        String jwtAccessToken = createJwtAccessToken(accountDTO, new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()));

    }*/

    private String createJwtAccessToken(AccountDTO accountDTO, Date expirationDate) {
        JwsCreator jwsCreator = new JwsCreator(
                jwtProperties.getPrivateKey(),
                expirationDate
        );

        TokenData tokenData = new TokenData(
                accountDTO.getId(), accountDTO.getUuid(), accountDTO.getRoles(), accountDTO.getPermissions()
        );

        return jwsCreator.generateToken(tokenData);
    }
}
