package com.statkovit.personalAccountsService.integration.controllers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.statkovit.personalAccountsService.constants.MappingConstants;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper;
import com.statkovit.personalAccountsService.helpers.rest.RestHelper;
import com.statkovit.personalAccountsService.helpers.rest.RestHelper.HttpResponse;
import com.statkovit.personalAccountsService.payload.LongDto;
import com.statkovit.personalAccountsService.payload.PersonalAccountDto;
import com.statkovit.personalAccountsService.repository.PersonalAccountFolderRepository;
import com.statkovit.personalAccountsService.repository.PersonalAccountRepository;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.statkovit.personalAccountsService.constants.MappingConstants.PersonalAccountsExternalController.*;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.prePopulatedValidAccountBuilder;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.prePopulatedValidAccountDtoBuilder;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("integration-test")
class PersonalAccountsControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PersonalAccountRepository personalAccountRepository;

    @Autowired
    private PersonalAccountFolderRepository personalAccountFolderRepository;

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
    private static final UUID UUID_3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private static final UUID UUID_4 = UUID.fromString("00000000-0000-0000-0000-000000000004");
    private static final UUID UUID_5 = UUID.fromString("00000000-0000-0000-0000-000000000005");

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
    public void createPersonalAccount_requireValidAuthToken() {
        PersonalAccountDto dto = prePopulatedValidAccountDtoBuilder().build();

        HttpResponse<PersonalAccountDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, INVALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void createPersonalAccount_DtoShouldContainAtLeastOneOfUrlName() {
        PersonalAccountDto dto = new PersonalAccountDto();

        HttpResponse<PersonalAccountDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = PersonalAccountDto.builder().name("name").build();

        response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());

        dto = PersonalAccountDto.builder().url("url").build();

        response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());

        dto = PersonalAccountDto.builder().url("url").name("name").build();

        response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void createPersonalAccount_ShouldReturnCreatedEntityDto() {
        PersonalAccountDto dto = PersonalAccountDto.builder().url("url").build();

        HttpResponse<PersonalAccountDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getResponseEntity().getBody());

        PersonalAccountDto responseDto = response.getConvertedResponse();

        Assertions.assertNotNull(responseDto.getUuid());
        Assertions.assertEquals(responseDto.getUrl(), "url");
    }

    @Test
    public void updatePersonalAccount_requireValidAuthToken() {
        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccountDto dto = prePopulatedValidAccountDtoBuilder().build();

        HttpResponse<PersonalAccountDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, INVALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void updatePersonalAccount_DtoShouldContainAtLeastOneOfUrlName() {
        PersonalAccount account = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        personalAccountRepository.save(account);

        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccountDto dto = PersonalAccountDto.builder().uuid(UUID_1).build();

        HttpResponse<PersonalAccountDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = PersonalAccountDto.builder().uuid(UUID_1).name("name").build();
        response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());

        dto = PersonalAccountDto.builder().uuid(UUID_1).url("url").build();

        response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());

        dto = PersonalAccountDto.builder().uuid(UUID_1).url("url").name("name").build();

        response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void updatePersonalAccount_CanNotUpdateIfEntityNotExists() {
        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccountDto dto = prePopulatedValidAccountDtoBuilder().uuid(UUID_1).build();

        HttpResponse<PersonalAccountDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void updatePersonalAccount_ShouldReturnUpdatedDto() {
        PersonalAccount account = prePopulatedValidAccountBuilder().accountEntityId(1L)
                .uuid(UUID_1).url("oldUrl").build();

        personalAccountRepository.save(account);

        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccountDto dto = prePopulatedValidAccountDtoBuilder().uuid(UUID_1).url("newUrl").build();

        HttpResponse<PersonalAccountDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());
        Assertions.assertEquals(UUID_1, response.getConvertedResponse().getUuid());
        Assertions.assertEquals("newUrl", response.getConvertedResponse().getUrl());
    }

    @Test
    public void deletePersonalAccount_requireValidAuthToken() {
        final String route = DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, INVALID_AUTH_HEADERS
        );

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void deletePersonalAccount_CanNotDeleteIfEntityNotExists() {
        final String route = DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, VALID_AUTH_HEADERS
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void deletePersonalAccount_CanDeleteIfEntityExists() {
        final String route = DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccount account = prePopulatedValidAccountBuilder().accountEntityId(1L)
                .uuid(UUID_1).build();

        personalAccountRepository.save(account);

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, VALID_AUTH_HEADERS
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void getPersonalAccounts_requireValidAuthToken() {
        HttpResponse<PersonalAccountDto[]> response = RestHelper.sendRequest(
                restTemplate, GET_LIST_ROUTE, HttpMethod.GET, INVALID_AUTH_HEADERS, PersonalAccountDto[].class
        );

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void getPersonalAccounts_ShouldReturnListOfCurrentUserAccounts() {
        PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(2L).uuid(UUID_2).build();
        PersonalAccount account3 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_3).build();
        personalAccountRepository.saveAll(List.of(account1, account2, account3));

        HttpResponse<PersonalAccountDto[]> response = RestHelper.sendRequest(
                restTemplate, GET_LIST_ROUTE, HttpMethod.GET, VALID_AUTH_HEADERS, PersonalAccountDto[].class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        List<PersonalAccountDto> accountDtos = Arrays.asList(response.getConvertedResponse());

        Assertions.assertEquals(2, accountDtos.size());
        Assertions.assertTrue(
                accountDtos.stream().map(PersonalAccountDto::getUuid).collect(Collectors.toList()).containsAll(List.of(UUID_1, UUID_3))
        );
    }

    @Test
    public void getPersonalAccounts_ShouldReturnListAccordingToUnfolderedFilter() {
        PersonalAccountFolder folder = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().build();
        folder = personalAccountFolderRepository.save(folder);

        final PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        final PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_2).folder(folder).build();

        personalAccountRepository.saveAll(List.of(account1, account2));

        final String route = UriComponentsBuilder.fromUriString(GET_LIST_ROUTE)
                .queryParam("unfolderedOnly", true)
                .toUriString();

        final HttpResponse<PersonalAccountDto[]> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, PersonalAccountDto[].class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        List<PersonalAccountDto> accountDtos = Arrays.asList(response.getConvertedResponse());

        Assertions.assertEquals(1, accountDtos.size());
        Assertions.assertEquals(account1.getUuid(), accountDtos.get(0).getUuid());
    }

    @Test
    public void getPersonalAccounts_ShouldReturnListAndUnfolderedFilterHasHigherPriorityThenFolderUuidFilter() {
        PersonalAccountFolder folder = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().uuid(UUID_3).build();
        folder = personalAccountFolderRepository.save(folder);

        final PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        final PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_2).folder(folder).build();

        personalAccountRepository.saveAll(List.of(account1, account2));

        final String route = UriComponentsBuilder.fromUriString(GET_LIST_ROUTE)
                .queryParam("unfolderedOnly", true)
                .queryParam("folderUuid", UUID_3)
                .toUriString();

        final HttpResponse<PersonalAccountDto[]> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, PersonalAccountDto[].class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        List<PersonalAccountDto> accountDtos = Arrays.asList(response.getConvertedResponse());

        Assertions.assertEquals(1, accountDtos.size());
        Assertions.assertEquals(account1.getUuid(), accountDtos.get(0).getUuid());
    }

    @Test
    public void getPersonalAccounts_ShouldReturnListAccordingToFolderUuidFilter() {
        PersonalAccountFolder folder1 = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().uuid(UUID_4).build();
        folder1 = personalAccountFolderRepository.save(folder1);

        PersonalAccountFolder folder2 = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().uuid(UUID_5).build();
        folder2 = personalAccountFolderRepository.save(folder2);

        final PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        final PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_2).folder(folder1).build();
        final PersonalAccount account3 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_3).folder(folder2).build();

        personalAccountRepository.saveAll(List.of(account1, account2, account3));

        final String route = UriComponentsBuilder.fromUriString(GET_LIST_ROUTE)
                .queryParam("folderUuid", UUID_4)
                .toUriString();

        final HttpResponse<PersonalAccountDto[]> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, PersonalAccountDto[].class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        List<PersonalAccountDto> accountDtos = Arrays.asList(response.getConvertedResponse());

        Assertions.assertEquals(1, accountDtos.size());
        Assertions.assertEquals(account2.getUuid(), accountDtos.get(0).getUuid());
    }

    @Test
    public void getPersonalAccounts_ShouldReturnBadRequestIfFolderNotExists() {
        final String route = UriComponentsBuilder.fromUriString(GET_LIST_ROUTE)
                .queryParam("folderUuid", UUID_1)
                .toUriString();

        final HttpResponse<PersonalAccountDto[]> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, PersonalAccountDto[].class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void getPersonalAccountsCount_requireValidAuthToken() {
        HttpResponse<LongDto> response = RestHelper.sendRequest(
                restTemplate, GET_LIST_COUNT_ROUTE, HttpMethod.GET, INVALID_AUTH_HEADERS, LongDto.class
        );

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void getPersonalAccountsCount_ShouldReturnCountOfCurrentUserAccounts() {
        PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(2L).uuid(UUID_2).build();
        PersonalAccount account3 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_3).build();
        personalAccountRepository.saveAll(List.of(account1, account2, account3));

        HttpResponse<LongDto> response = RestHelper.sendRequest(
                restTemplate, GET_LIST_COUNT_ROUTE, HttpMethod.GET, VALID_AUTH_HEADERS, LongDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        Assertions.assertEquals(2, response.getConvertedResponse().getValue());
    }

    @Test
    public void getPersonalAccountsCount_ShouldReturnCountAccordingToUnfolderedFilter() {
        PersonalAccountFolder folder = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().build();
        folder = personalAccountFolderRepository.save(folder);

        final PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        final PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_2).folder(folder).build();

        personalAccountRepository.saveAll(List.of(account1, account2));

        final String route = UriComponentsBuilder.fromUriString(GET_LIST_COUNT_ROUTE)
                .queryParam("unfolderedOnly", true)
                .toUriString();

        final HttpResponse<LongDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, LongDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        Assertions.assertEquals(1, response.getConvertedResponse().getValue());
    }

    @Test
    public void getPersonalAccountsCount_ShouldReturnCountAndUnfolderedFilterHasHigherPriorityThenFolderUuidFilter() {
        PersonalAccountFolder folder = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().uuid(UUID_3).build();
        folder = personalAccountFolderRepository.save(folder);

        final PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        final PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_2).folder(folder).build();

        personalAccountRepository.saveAll(List.of(account1, account2));

        final String route = UriComponentsBuilder.fromUriString(GET_LIST_COUNT_ROUTE)
                .queryParam("unfolderedOnly", true)
                .queryParam("folderUuid", UUID_3)
                .toUriString();

        final HttpResponse<LongDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, LongDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        Assertions.assertEquals(1, response.getConvertedResponse().getValue());
    }

    @Test
    public void getPersonalAccountsCount_ShouldReturnCountAccordingToFolderUuidFilter() {
        PersonalAccountFolder folder1 = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().uuid(UUID_4).build();
        folder1 = personalAccountFolderRepository.save(folder1);

        PersonalAccountFolder folder2 = PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder().uuid(UUID_5).build();
        folder2 = personalAccountFolderRepository.save(folder2);

        final PersonalAccount account1 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_1).build();
        final PersonalAccount account2 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_2).folder(folder1).build();
        final PersonalAccount account3 = prePopulatedValidAccountBuilder().accountEntityId(1L).uuid(UUID_3).folder(folder2).build();

        personalAccountRepository.saveAll(List.of(account1, account2, account3));

        final String route = UriComponentsBuilder.fromUriString(GET_LIST_COUNT_ROUTE)
                .queryParam("folderUuid", UUID_4)
                .toUriString();

        final HttpResponse<LongDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, LongDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        Assertions.assertEquals(1, response.getConvertedResponse().getValue());
    }

    @Test
    public void getPersonalAccountsCount_ShouldReturnBadRequestIfFolderNotExists() {
        final String route = UriComponentsBuilder.fromUriString(GET_LIST_COUNT_ROUTE)
                .queryParam("folderUuid", UUID_1)
                .toUriString();

        final HttpResponse<LongDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.GET, VALID_AUTH_HEADERS, LongDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());
    }
}