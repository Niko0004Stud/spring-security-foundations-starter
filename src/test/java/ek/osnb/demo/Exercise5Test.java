package ek.osnb.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

// A real server, because MockMvc doesn't store the CSRF token in a cookie
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Exercise5Test {

    private static final String LOGIN_URL = "/login";

    @LocalServerPort
    private int port;

    private final CookieManager cookies = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    private final HttpClient client = HttpClient.newBuilder().cookieHandler(cookies).build();

    @BeforeEach
    void login() throws Exception {
        assertThat(post(LOGIN_URL, "username=user1&password=pw", xsrfToken()).statusCode()).isLessThan(400);
    }

    @Test
    void postWithAValidTokenIsAccepted() throws Exception {
        assertThat(postNote(xsrfToken()).statusCode()).isEqualTo(201);
    }

    @Test
    void postWithoutATokenIsRejected() throws Exception {
        assertThat(postNote(null).statusCode()).isEqualTo(403);
    }

    @Test
    void postWithAnInvalidTokenIsRejected() throws Exception {
        xsrfToken();
        assertThat(postNote("not-the-token").statusCode()).isEqualTo(403);
    }

    @Test
    void aFormPostIsNoLongerAccepted() throws Exception {
        assertThat(post("/api/notes", "text=Hi", xsrfToken()).statusCode()).isEqualTo(415);
    }

    @Test
    void javaScriptCanReadTheTokenCookie() throws Exception {
        xsrfToken();
        assertThat(cookie("XSRF-TOKEN").isHttpOnly()).isFalse();
    }

    @Test
    void javaScriptCannotReadTheSessionCookie() {
        assertThat(cookie("JSESSIONID").isHttpOnly()).isTrue();
    }

    private String xsrfToken() throws Exception {
        client.send(HttpRequest.newBuilder(uri("/api/hello")).build(), HttpResponse.BodyHandlers.discarding());
        return cookie("XSRF-TOKEN").getValue();
    }

    private HttpCookie cookie(String name) {
        return cookies.getCookieStore().getCookies().stream()
                .filter(cookie -> cookie.getName().equals(name))
                .findFirst()
                .orElseThrow();
    }

    private HttpResponse<String> post(String path, String form, String token) throws Exception {
        return send(path, "application/x-www-form-urlencoded", form, token);
    }

    private HttpResponse<String> postNote(String token) throws Exception {
        return send("/api/notes", "application/json", """
                {"text": "Hi"}
                """, token);
    }

    private HttpResponse<String> send(String path, String contentType, String body, String token) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(uri(path))
                .header("Content-Type", contentType)
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (token != null) {
            request.header("X-XSRF-TOKEN", token);
        }
        return client.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String path) {
        return URI.create("http://localhost:" + port + path);
    }
}
