package leonil.sulude.api.gateway.filter;

import leonil.sulude.api.gateway.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = TestController.class)
@Import(CorrelationIdFilter.class)
class CorrelationFilterTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void shouldGenerateCorrelationIdWhenNotProvided() {

        webTestClient.get()
                .uri("/test")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().exists("X-Correlation-Id");
    }

    @Test
    void shouldPropagateExistingCorrelationId() {

        webTestClient.get()
                .uri("/test")
                .header("X-Correlation-Id", "test-correlation-123")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("X-Correlation-Id", "test-correlation-123");
    }
}
