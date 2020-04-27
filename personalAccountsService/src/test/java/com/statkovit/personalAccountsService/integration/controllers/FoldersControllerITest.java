package com.statkovit.personalAccountsService.integration.controllers;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.statkovit.personalAccountsService.constants.MappingConstants;
import com.statkovit.personalAccountsService.domain.PersonalAccount;
import com.statkovit.personalAccountsService.domain.PersonalAccountFolder;
import com.statkovit.personalAccountsService.enums.FolderRemovalOptions;
import com.statkovit.personalAccountsService.helpers.ClearDatabase;
import com.statkovit.personalAccountsService.helpers.rest.RestHelper;
import com.statkovit.personalAccountsService.helpers.rest.RestHelper.HttpResponse;
import com.statkovit.personalAccountsService.payload.PersonalAccountFolderDto;
import com.statkovit.personalAccountsService.repository.AccountDataRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.statkovit.personalAccountsService.constants.MappingConstants.FoldersExternalController.*;
import static com.statkovit.personalAccountsService.helpers.domain.AccountDataDomainHelper.prePopulatedValidAccountDataBuilder;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountDomainHelper.prePopulatedValidAccountBuilder;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.prePopulatedValidFolderBuilder;
import static com.statkovit.personalAccountsService.helpers.domain.PersonalAccountFolderDomainHelper.prePopulatedValidFolderDtoBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = NONE)
@ClearDatabase
@ActiveProfiles("integration-test")
class FoldersControllerITest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PersonalAccountFolderRepository folderRepository;

    @Autowired
    private PersonalAccountRepository personalAccountRepository;

    @Autowired
    private AccountDataRepository accountDataRepository;

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

        accountDataRepository.saveAndFlush(
                prePopulatedValidAccountDataBuilder()
                        .id(1L)
                        .email("email1@test.com")
                        .uuid(UUID_1)
                        .build()
        );

        accountDataRepository.saveAndFlush(
                prePopulatedValidAccountDataBuilder()
                        .id(2L)
                        .email("email2@test.com")
                        .uuid(UUID_2)
                        .build()
        );
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
    public void createFolder_DtoCanNotExceedMaxLength() {
        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder()
                .name("-".repeat(PersonalAccountFolder.MAX_LENGTH__NAME) + 1)
                .build();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, CREATE_ROUTE, HttpMethod.POST, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = prePopulatedValidFolderDtoBuilder()
                .name("-".repeat(PersonalAccountFolder.MAX_LENGTH__NAME))
                .build();

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

    @Test
    public void updateFolder_requireValidAuthToken() {
        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder().build();

        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, INVALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void updateFolder_DtoShouldContainName() {
        folderRepository.save(prePopulatedValidFolderBuilder().uuid(UUID_1).build());

        PersonalAccountFolderDto dto = new PersonalAccountFolderDto();

        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = prePopulatedValidFolderDtoBuilder().name("name").build();

        response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void updateFolder_DtoCanNotExceedMaxLength() {
        folderRepository.save(prePopulatedValidFolderBuilder().uuid(UUID_1).build());

        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder()
                .name("-".repeat(PersonalAccountFolder.MAX_LENGTH__NAME) + 1)
                .build();

        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = prePopulatedValidFolderDtoBuilder()
                .name("-".repeat(PersonalAccountFolder.MAX_LENGTH__NAME))
                .build();

        response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void updateFolder_CanNotUpdateIfEntityNotExists() {
        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder().uuid(UUID_1).build();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void updateFolder_shouldReturnDtoOfUpdatedEntity() {
        folderRepository.save(prePopulatedValidFolderBuilder().uuid(UUID_1).build());

        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder()
                .uuid(UUID_1).name("name").build();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getResponseEntity().getBody());

        PersonalAccountFolderDto responseDto = response.getConvertedResponse();

        Assertions.assertEquals(UUID_1, responseDto.getUuid());
        Assertions.assertEquals("name", responseDto.getName());
    }

    @Test
    public void updateFolder_foldersShouldHaveUniqueNameForUserAccount() {
        PersonalAccountFolder folder1 = prePopulatedValidFolderBuilder().accountEntityId(1L)
                .uuid(UUID_1)
                .name("name").build();

        PersonalAccountFolder folder2 = prePopulatedValidFolderBuilder().accountEntityId(1L)
                .uuid(UUID_2)
                .name("name1").build();

        folderRepository.saveAll(List.of(folder1, folder2));

        final String route = UPDATE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        PersonalAccountFolderDto dto = prePopulatedValidFolderDtoBuilder().name("name1").build();

        HttpResponse<PersonalAccountFolderDto> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());

        dto = prePopulatedValidFolderDtoBuilder().name("name").build();

        response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());

        dto = prePopulatedValidFolderDtoBuilder().name("name2").build();

        response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.PUT, dto, VALID_AUTH_HEADERS, PersonalAccountFolderDto.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void getFolderListOfCurrentAccountEntity_requireValidAuthToken() {
        HttpResponse<PersonalAccountFolderDto[]> response = RestHelper.sendRequest(
                restTemplate, GET_LIST_ROUTE, HttpMethod.GET, INVALID_AUTH_HEADERS, PersonalAccountFolderDto[].class
        );

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void getFolderListOfCurrentAccountEntity_ShouldReturnListOfCurrentUserFolders() {
        PersonalAccountFolder folder1 = prePopulatedValidFolderBuilder().accountEntityId(1L).uuid(UUID_1).build();

        PersonalAccountFolder folder2 = prePopulatedValidFolderBuilder().accountEntityId(2L).uuid(UUID_2).build();

        folderRepository.saveAll(List.of(folder1, folder2));

        HttpResponse<PersonalAccountFolderDto[]> response = RestHelper.sendRequest(
                restTemplate, GET_LIST_ROUTE, HttpMethod.GET, VALID_AUTH_HEADERS, PersonalAccountFolderDto[].class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertNotNull(response.getConvertedResponse());

        List<PersonalAccountFolderDto> folderDtos = Arrays.asList(response.getConvertedResponse());

        Assertions.assertEquals(1, folderDtos.size());
        Assertions.assertTrue(
                folderDtos.stream().allMatch(dto -> dto.getUuid().equals(UUID_1))
        );
    }

    @Test
    public void deleteFolder_requireValidAuthToken() {
        final String route = DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, INVALID_AUTH_HEADERS
        );

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void deleteFolder_requireFolderRemovalOption() {
        final String route = DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, VALID_AUTH_HEADERS
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void deleteFolder_CanNotDeleteIfEntityNotExists() {
        final String route = DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString());

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, VALID_AUTH_HEADERS, Void.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getResponseEntity().getStatusCode());
    }

    @Test
    public void deleteFolder_shouldDeleteAccordingToFoldersOnlyOption() {
        final PersonalAccountFolder folder = folderRepository.save(
                prePopulatedValidFolderBuilder().uuid(UUID_1).accountEntityId(1L).build()
        );
        final PersonalAccount account = personalAccountRepository.save(
                prePopulatedValidAccountBuilder().uuid(UUID_2).accountEntityId(1L).folder(folder).build()
        );

        final String route = UriComponentsBuilder.fromUriString(DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString()))
                .queryParam("removalOption", FolderRemovalOptions.FOLDER_ONLY)
                .toUriString();

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, VALID_AUTH_HEADERS, Void.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertFalse(folderRepository.existsByUuidAndAccountEntityId(UUID_1, 1L));

        Optional<PersonalAccount> accountOptional = personalAccountRepository.findById(account.getId());
        Assertions.assertTrue(accountOptional.isPresent());
        Assertions.assertNull(accountOptional.get().getFolder());
    }

    @Test
    public void deleteFolder_shouldDeleteAccordingToWithAccountsOption() {
        final PersonalAccountFolder folder = folderRepository.save(
                prePopulatedValidFolderBuilder().uuid(UUID_1).accountEntityId(1L).build()
        );
        final PersonalAccount account = personalAccountRepository.save(
                prePopulatedValidAccountBuilder().uuid(UUID_2).accountEntityId(1L).folder(folder).build()
        );

        final String route = UriComponentsBuilder.fromUriString(DELETE_ROUTE.replace(MappingConstants.UUID_PATH, UUID_1.toString()))
                .queryParam("removalOption", FolderRemovalOptions.WITH_ACCOUNTS)
                .toUriString();

        HttpResponse<Void> response = RestHelper.sendRequest(
                restTemplate, route, HttpMethod.DELETE, VALID_AUTH_HEADERS, Void.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getResponseEntity().getStatusCode());
        Assertions.assertFalse(folderRepository.existsByUuidAndAccountEntityId(UUID_1, 1L));

        Optional<PersonalAccount> accountOptional = personalAccountRepository.findById(account.getId());
        Assertions.assertFalse(accountOptional.isPresent());
    }
}