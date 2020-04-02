package com.statkovit.personalAccountsService.unit.util;

import com.statkovit.personalAccountsService.properties.CustomProperties;
import com.statkovit.personalAccountsService.properties.CustomProperties.Aes;
import com.statkovit.personalAccountsService.utils.AesUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.codec.Hex;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AesUtilsUTest {

    @Mock
    CustomProperties customProperties;

    @InjectMocks
    AesUtils aesUtils;

    private static final String DECRYPTED_WITH_VALID_KEY_SALT_TEST_STRING = "test string";
    private static final String ENCRYPTED_WITH_VALID_KEY_SALT_TEST_STRING = "0150480a5aab586c8eeeb66cbe4f6a68b6d9b5bd53102d01a8a4cf1992fed458";

    private static final String VALID_SALT = "5662674c7a59493269426b4a41494168";
    private static final String INVALID_SALT = "58465a58656834644e476d676f4e3874";

    private static final String VALID_KEY = "R1FqQm5RTVhFUG9iVGQwOTltYXcwM1o1UUJEemx5VXRBbDdHa2pQUlVmYmVwQnlkb201WFVCTmg5djdFa25Nbw==";
    private static final String INVALID_KEY = "R1FqQm5RTVhFUG9iVGQwYTltYXcwM1o1UUJEemx5VXRBbDdka2pQUlVmYmVwQnlkb201WFVCTmg5djdFa25Nbw==";

    @Test
    void encryptShouldNotReturnDecryptedString() {
        mockEncryptionKey(VALID_KEY);

        String result = aesUtils.encrypt(DECRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, VALID_SALT);

        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(DECRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, result);
    }

    @Test
    void decryptShouldNotReturnEncryptedString() {
        mockEncryptionKey(VALID_KEY);

        String result = aesUtils.decrypt(ENCRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, VALID_SALT);

        Assertions.assertNotNull(result);
        Assertions.assertNotEquals(ENCRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, result);
    }

    @Test
    void decryptWithCorrectSaltAndKeyShouldReturnDecryptedString() {
        mockEncryptionKey(VALID_KEY);

        String result = aesUtils.decrypt(ENCRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, VALID_SALT);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(DECRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, result);
    }

    @Test
    void decryptWithIncorrectSaltShouldThrowException() {
        mockEncryptionKey(VALID_KEY);

        Assertions.assertThrows(IllegalStateException.class, () ->
                aesUtils.decrypt(ENCRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, INVALID_SALT)
        );
    }

    @Test
    void decryptWithIncorrectKeyShouldThrowException() {
        mockEncryptionKey(INVALID_KEY);

        Assertions.assertThrows(IllegalStateException.class, () ->
                aesUtils.decrypt(ENCRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, VALID_SALT)
        );
    }


    @Test
    void encryptedDataCanBeReversedToDecrypted() {
        mockEncryptionKey(VALID_KEY);

        String encrypted = aesUtils.encrypt(DECRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, VALID_SALT);

        Assertions.assertNotNull(encrypted);

        String decrypted = aesUtils.decrypt(encrypted, VALID_SALT);

        Assertions.assertNotNull(decrypted);

        Assertions.assertEquals(DECRYPTED_WITH_VALID_KEY_SALT_TEST_STRING, decrypted);
    }

    @Test
    void generateSaltShouldReturn32byteLengthHex() {
        String salt = aesUtils.generateSalt();

        Assertions.assertNotNull(salt);
        Assertions.assertEquals(32, salt.length());

        Assertions.assertDoesNotThrow(() -> {
            Hex.decode(salt);
        });
    }

    private void mockEncryptionKey(String key) {
        Aes aes = mock(Aes.class);
        when(customProperties.getAes()).thenReturn(aes);
        when(aes.getKey()).thenReturn(key);
    }
}