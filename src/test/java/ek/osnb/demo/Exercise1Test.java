package ek.osnb.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class Exercise1Test {

    @Autowired
    private MockMvcTester mvc;

    @Test
    void anyoneCanSayHello() {
        assertThat(mvc.get().uri("/api/hello").exchange()).hasStatusOk();
    }
}
