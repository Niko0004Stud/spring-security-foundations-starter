package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class Exercise6Test {

    @Autowired
    private MockMvcTester mvc;

    @ParameterizedTest
    @ValueSource(strings = {"/api/notes", "/api/me", "/api/admin/notes"})
    void theApiAnswers401InsteadOfRedirecting(String path) {
        assertThat(mvc.get().uri(path).accept("text/html").exchange())
                .hasStatus(HttpStatus.UNAUTHORIZED)
                .headers().doesNotContainHeader(HttpHeaders.LOCATION);
    }

    @Test
    void loginWithoutACsrfTokenIsRejected() {
        assertThat(mvc.post().uri("/api/login")
                .param("username", "user1")
                .param("password", "pw")
                .exchange())
                .hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    void wrongPasswordGives401() {
        assertThat(login("user1", "wrong")).hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void theSessionKeepsYouLoggedInUntilYouLogOut() {
        MockHttpSession session = session(login("user1", "pw"));

        assertThat(mvc.get().uri("/api/me").session(session).exchange()).hasStatusOk();
        assertThat(mvc.post().uri("/api/logout").session(session).with(csrf()).exchange())
                .hasStatus(HttpStatus.NO_CONTENT);
        assertThat(mvc.get().uri("/api/me").session(session).exchange())
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void aRegularUserGets403OnTheAdminEndpoint() {
        MockHttpSession session = session(login("user1", "pw"));

        assertThat(mvc.get().uri("/api/admin/notes").session(session).exchange())
                .hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    void adminSeesEveryonesNotes() {
        MockHttpSession session = session(login("admin", "pw"));

        assertThat(mvc.get().uri("/api/admin/notes").session(session).exchange())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].owner")
                .asArray()
                .contains("user1", "user2");
    }

    private MvcTestResult login(String username, String password) {
        return mvc.post().uri("/api/login")
                .param("username", username)
                .param("password", password)
                .with(csrf())
                .exchange();
    }

    private MockHttpSession session(MvcTestResult login) {
        return (MockHttpSession) login.getRequest().getSession();
    }
}
