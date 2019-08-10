package com.statkovit.configurationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EncryptionTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_STRING = "Lorem Ipsum";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void shouldEncryptAndDecrypt() throws Exception {
        MvcResult result = this.mockMvc.perform(
                post("/encrypt")
                        .content(DEFAULT_STRING)
                        .contentType(MediaType.TEXT_PLAIN)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(EMPTY_STRING)))
                .andExpect(content().string(not(DEFAULT_STRING)))
                .andReturn();

        final String encryptedString = result.getResponse().getContentAsString();

        this.mockMvc.perform(
                post("/decrypt")
                        .content(encryptedString)
                        .contentType(MediaType.TEXT_PLAIN)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(not(EMPTY_STRING)))
                .andExpect(content().string(not(encryptedString)))
                .andExpect(content().string(DEFAULT_STRING));
    }

    @Test
    void shouldNotDecryptInvalidString() throws Exception {
        this.mockMvc.perform(
                post("/decrypt")
                        .content(DEFAULT_STRING)
                        .contentType(MediaType.TEXT_PLAIN)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
