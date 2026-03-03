package com.craftpg;

import org.jspecify.annotations.NonNull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CraftpgApplication {

    static void main(@NonNull final String[] args) {
        SpringApplication.run(CraftpgApplication.class, args);
    }
}
