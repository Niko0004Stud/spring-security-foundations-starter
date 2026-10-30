package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class Exercise3Test {

    @Autowired
    private MockMvcTester mvc;

    @Test
    @WithMockUser(username = "user1")
    void user1OnlySeesTheirOwnNotes() {
        assertThat(mvc.get().uri("/api/notes").exchange())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].owner")
                .asArray()
                .isNotEmpty()
                .containsOnly("user1");
    }

    @Test
    @WithMockUser(username = "user2")
    void user2OnlySeesTheirOwnNotes() {
        assertThat(mvc.get().uri("/api/notes").exchange())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].owner")
                .asArray()
                .isNotEmpty()
                .containsOnly("user2");
    }

    @Test
    @WithMockUser(username = "user2")
    void theServerDecidesTheOwner() {
        assertThat(mvc.post().uri("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"text": "Study Spring Security", "owner": "user1"}
                        """)
                .with(csrf())
                .exchange())
                .hasStatus(HttpStatus.CREATED)
                .bodyJson()
                .extractingPath("$.owner")
                .isEqualTo("user2");
    }

    @Test
    void meGivesTheUsernameAndOnlyTheRoles() {
        assertThat(mvc.get().uri("/api/me").session(loginAs("user1")).exchange())
                .hasStatusOk()
                .bodyJson()
                .isLenientlyEqualTo("""
                        {"username": "user1", "roles": ["ROLE_USER"]}
                        """);
        assertThat(mvc.get().uri("/api/me").session(loginAs("admin")).exchange())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.roles")
                .isEqualTo(List.of("ROLE_ADMIN"));
    }

    // A real login, because a mock user doesn't get FACTOR_PASSWORD
    private MockHttpSession loginAs(String username) {
        MvcTestResult result = mvc.post().uri("/login")
                .param("username", username)
                .param("password", "pw")
                .with(csrf())
                .exchange();
        return (MockHttpSession) result.getRequest().getSession();
    }
}
