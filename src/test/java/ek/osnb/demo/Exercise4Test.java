package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class Exercise4Test {

    @Autowired
    private MockMvcTester mvc;

    @Test
    @WithMockUser(username = "user1")
    void postWithoutCsrfTokenIsRejected() {
        assertThat(mvc.post().uri("/api/notes").param("text", "Evil was here").exchange())
                .hasStatus(HttpStatus.FORBIDDEN);
    }
}
