package ru.aasmc.edgeservice.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("user")
    public Mono<User> getUser(@AuthenticationPrincipal OidcUser oidcUser) {
        log.info("Fetching information about the currently authenticated user");
        var user = new User(
                oidcUser.getPreferredUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                oidcUser.getClaimAsStringList("roles")
        );
        return Mono.just(user);
    }

    // One way to retrieve User from SecurityContext
    /*
    @GetMapping("user")
    public Mono<User> getUser() {
        // Gets SecurityContext for the currently authenticated user from
        // ReactiveSecurityContextHolder
        return ReactiveSecurityContextHolder.getContext()
                // Gets Authentication from SecurityContext
                .map(SecurityContext::getAuthentication)
                // Gets the principal from Authentication
                // For OIDC, it's of type OidcUser
                .map(authentication ->
                        (OidcUser) authentication.getPrincipal())
                .map(oidcUser ->
                        new User(
                                oidcUser.getPreferredUsername(),
                                oidcUser.getGivenName(),
                                oidcUser.getFamilyName(),
                                List.of("employee", "customer")
                        ));
    }
    */
}
