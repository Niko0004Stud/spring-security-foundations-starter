package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
class Exercise2Test {

    @Autowired
    private MockMvcTester mvc;

    @Test
    void allThreeUsersFromTheDatabaseCanLogIn() {
        assertThat(login("user1", "pw")).hasRedirectedUrl("/");
        assertThat(login("user2", "pw")).hasRedirectedUrl("/");
        assertThat(login("admin", "pw")).hasRedirectedUrl("/");
    }

    @Test
    void unknownUserIsRejected() {
        assertThat(login("nobody", "pw")).hasRedirectedUrl("/login?error");
    }

    @Test
    @WithMockUser(username = "user1", roles = "USER")
    void user1IsNotAnAdmin() {
        assertThat(mvc.get().uri("/api/admin/notes").exchange()).hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCanSeeEveryonesNotes() {
        assertThat(mvc.get().uri("/api/admin/notes").exchange())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].owner")
                .asArray()
                .contains("user1", "user2");
    }

    private MvcTestResult login(String username, String password) {
        return mvc.post().uri("/login")
                .param("username", username)
                .param("password", password)
                .with(csrf())
                .exchange();
    }
}
