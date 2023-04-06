package ru.aasmc.edgeservice.web;

import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class SecurityControllerAdvice {

    @ModelAttribute(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME)
    public Mono<CsrfToken> getCsrfToken(final ServerWebExchange exchange) {

        return exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
    }
}
