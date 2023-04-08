package ru.aasmc.edgeservice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.aasmc.edgeservice.config.SecurityConfig;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebFluxTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webClient;

    /**
     * Mock bean needed to skip the interaction with Keycloak when retrieving information
     * about the Client registration.
     */
    @MockBean
    private ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void whenNotAuthenticatedThen401() {
        webClient
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedThenReturnUser() {
        var expectedUser = new User("jon.snow", "Jon", "Snow",
                List.of("employee", "customer"));
        webClient
                .mutateWith(configureMockOidcLogin(expectedUser))
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(User.class)
                .value(user -> assertThat(user).isEqualTo(expectedUser));
    }

    /**
     * Mocks an OIDC login and builds a mock ID token.
     */
    private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(User expected) {
        return SecurityMockServerConfigurers.mockOidcLogin().idToken(
                builder -> {
                    builder.claim(StandardClaimNames.PREFERRED_USERNAME,
                            expected.username());
                    builder.claim(StandardClaimNames.GIVEN_NAME,
                            expected.firstName());
                    builder.claim(StandardClaimNames.FAMILY_NAME,
                            expected.lastName());
                    builder.claim("roles", expected.roles());
                }
        );
    }

}