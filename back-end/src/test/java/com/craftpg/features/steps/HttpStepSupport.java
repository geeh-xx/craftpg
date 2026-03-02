package com.craftpg.features.steps;

import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

abstract class HttpStepSupport {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String method;
    private String path;
    private String payloadTemplate;
    private boolean authenticated;
    private MvcResult result;
    private Exception requestException;

    protected void setRoute(final String method, final String path) {
        this.method = method;
        this.path = path;
    }

    protected void setPayloadTemplate(final String payloadTemplate) {
        this.payloadTemplate = payloadTemplate;
    }

    protected void setAuthenticated(final boolean authenticated) {
        this.authenticated = authenticated;
    }

    protected void sendRequest() {
        requestException = null;
        result = null;

        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        }

        final String body;
        if (payloadTemplate == null || "none".equalsIgnoreCase(payloadTemplate)) {
            body = null;
        } else {
            final String payloadFile = "data/" + payloadTemplate + ".json";
            try {
                body = StreamUtils.copyToString(new ClassPathResource(payloadFile).getInputStream(), StandardCharsets.UTF_8);
            } catch (Exception ex) {
                throw new IllegalStateException("Unable to read payload file: " + payloadFile, ex);
            }
        }

        final HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase());

        try {
            var requestBuilder = MockMvcRequestBuilders
                .request(httpMethod, path)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

            if (authenticated) {
                requestBuilder = requestBuilder
                    .header("Authorization", "Bearer cucumber-token")
                    .with(jwt().jwt(token -> token
                        .subject(UUID.fromString("00000000-0000-0000-0000-000000000001").toString())
                        .claim("email", "cucumber@craftpg.test")
                        .claim("preferred_username", "cucumber-user")
                    ));
            }

            if (body != null) {
                requestBuilder = requestBuilder.content(body);
            }

            result = mockMvc.perform(requestBuilder)
                .andDo(print())
                .andReturn();
        } catch (Exception ex) {
            requestException = ex;
        }
    }

    protected int getResponseStatus() {
        if (requestException != null) {
            return 500;
        }
        if (result == null) {
            return 500;
        }
        return result.getResponse().getStatus();
    }

    protected Exception getRequestException() {
        return requestException;
    }

    protected String getResponseBody() {
        if (result == null) {
            return "";
        }
        return new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }
}