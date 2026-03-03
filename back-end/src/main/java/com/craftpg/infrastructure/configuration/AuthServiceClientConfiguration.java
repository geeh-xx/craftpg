package com.craftpg.infrastructure.configuration;

import com.craftpg.infrastructure.exception.AuthorizationServiceException;
import com.craftpg.infrastructure.web.client.AuthServiceClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AuthServiceClientConfiguration {

    @Value("${app.keycloak.base-url}")
    private String authServiceBaseUrl;

    @Bean
    AuthServiceClient authServiceClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(authServiceBaseUrl)
                .defaultStatusHandler(
                        HttpStatusCode::isError,
                        (_, response) -> {
                            String body = response.getBody().toString();
                            throw new AuthorizationServiceException(
                                    response.getStatusCode(),
                                    body
                            );
                        }
                )
                .build();

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
                        .build();

        return factory.createClient(AuthServiceClient.class);
    }

}
