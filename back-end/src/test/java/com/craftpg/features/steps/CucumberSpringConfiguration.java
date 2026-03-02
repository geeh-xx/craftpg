package com.craftpg.features.steps;

import com.craftpg.configuration.ContainerTestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@CucumberContextConfiguration
public class CucumberSpringConfiguration extends ContainerTestConfig {

    @MockitoBean
    JwtDecoder jwtDecoder;
}
