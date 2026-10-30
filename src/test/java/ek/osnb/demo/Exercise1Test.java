package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class Exercise1Test {

    @Autowired
    private MockMvcTester mvc;

    @Test
    void anyoneCanSayHello() {
        assertThat(mvc.get().uri("/api/hello").exchange()).hasStatusOk();
    }

    @Test
    void notesRedirectToTheLoginPage() {
        assertThat(mvc.get().uri("/api/notes").accept("text/html").exchange())
                .hasStatus(HttpStatus.FOUND)
                .headers().containsHeader(HttpHeaders.LOCATION);
    }

    @Test
    void user1AndAdminCanLogIn() {
        assertThat(login("user1", "pw")).hasRedirectedUrl("/");
        assertThat(login("admin", "pw")).hasRedirectedUrl("/");
    }

    @Test
    void wrongPasswordIsRejected() {
        assertThat(login("user1", "wrong")).hasRedirectedUrl("/login?error");
    }

    private MvcTestResult login(String username, String password) {
        return mvc.post().uri("/login")
                .param("username", username)
                .param("password", password)
                .with(csrf())
                .exchange();
    }
}
