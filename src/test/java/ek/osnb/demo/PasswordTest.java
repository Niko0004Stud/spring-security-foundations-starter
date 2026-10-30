package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordTest {

    @Test
    void samePasswordGivesDifferentHashes() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hash1 = encoder.encode("pw");
        String hash2 = encoder.encode("pw");

        assertThat(hash1).isNotEqualTo(hash2);
        assertThat(encoder.matches("pw", hash1)).isTrue();
        assertThat(encoder.matches("pw", hash2)).isTrue();
        assertThat(encoder.matches("wrong", hash1)).isFalse();
    }
}
