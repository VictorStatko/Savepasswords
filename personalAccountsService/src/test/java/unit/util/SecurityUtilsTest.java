package unit.util;

import com.statkovit.personalAccountsService.utils.SecurityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Random;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityUtilsTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private OAuth2Authentication oAuth2Authentication;

    @Mock
    private Authentication userAuthentication;

    private SecurityUtils securityUtils = new SecurityUtils();

    @Test
    void getCurrentAccountEntityShouldReturnIdFromUserAuthentication() {
        final Long userId = getRandomLong();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(oAuth2Authentication);
        when(oAuth2Authentication.getUserAuthentication()).thenReturn(userAuthentication);
        when(userAuthentication.getPrincipal()).thenReturn(userId);

        Long accountEntityId = securityUtils.getCurrentAccountEntityId();

        Assertions.assertEquals(userId, accountEntityId);

    }

    private Long getRandomLong() {
        Random random = new Random();
        return random.nextLong();
    }
}