package com.statkovit.personalAccountsService.integration.controllers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.helpers.rest.RestHelper;
import com.statkovit.personalAccountsService.helpers.rest.RestHelper.HttpResponse;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.statkovit.personalAccountsService.constants.MappingConstants.FoldersExternalController.CREATE_ROUTE;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.prePopulatedValidFolderDtoBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-test")
class FoldersControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PersonalAccountFolderRepository folderRepository;

    private final WireMockServer wireMockServer = new WireMockServer(9999);

    // @formatter:off
    private static final String CURRENT_ACCOUNT_RESPONSE =
            "{" +
                    "    \"uuid\": \"00000000-0000-0000-0000-000000000001\"," +
                    "    \"email\": \"test@gmail.com\"," +
                    "    \"id\": 1" +
                    "}";

    private static final String CHECK_TOKEN_RESPONSE =
            "{" +
                    "    \"active\": true," +
                    "    \"exp\": 1585997084," +
                    "    \"user_name\": \"test@gmail.com\"," +
                    "    \"authorities\": [" +
                    "        \"ACCOUNT_OWNER\"" +
                    "    ]," +
                    "    \"client_id\": \"webclient\"," +
                    "    \"scope\": [" +
                    "        \"ui\"" +
                    "    ]" +
                    "}";
    // @formatter:on

    private static final Map<String, String> VALID_AUTH_HEADERS = Collections.singletonMap("Authorization", "Bearer myTestToken");
    private static final Map<String, String> INVALID_AUTH_HEADERS = Collections.singletonMap("Authorization", "Bearer myInvalidTestToken");

    private static final UUID UUID_1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID UUID_2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @BeforeEach
    public void setUp() {
        wireMockServer.stubFor(
                post(urlPathMatching("/oauth/check_token"))
                        .withQueryParam("token", equalTo("myTestToken")).willReturn(
                        okJson(CHECK_TOKEN_RESPONSE)
                )
        );

        wireMockServer.stubFor(
                get(urlPathMatching("/api/v1/auth/accounts/current"))
                        .withHeader("Authorization", equalTo("Bearer myTestToken")).willReturn(
                        okJson(CURRENT_ACCOUNT_RESPONSE)
                )
        );
        wireMockServer.start();
    }


    @AfterEach
    public void stop() {
        wireMockServer.stop();
    }

    @Test
    public void createFolder_requireValidAuthToken() {
        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder().build();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, INVALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void createFolder_DtoShouldContainName() {
        PersonalAccountFolderDto dto = new PersonalAccountFolderDto();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = PersonalAccountFolderDto.builder().name("name").build();

        response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void createFolder_shouldReturnDtoOfCreatedEntity() {
        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder().name("name").build();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getResponseEntity().getBody());

        PersonalAccountFolderDto responseDto = response.getConvertedResponse();

        Assertions.assertNotNull(responseDto.getUuid());
        Assertions.assertEquals(responseDto.getName(), "name");
    }

    @Test
    public void createFolder_foldersShouldHaveUniqueNameForUserAccount() {
        PersonalAccountFolder folder = prePopulatedValidFolderBuilder().accountEntityId(1L).name("name").build();
        folderRepository.save(folder);

        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder().name("name").build();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = prePopulatedValidFolderDtoBuilder().name("name1").build();

        response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

}