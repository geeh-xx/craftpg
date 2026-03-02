package com.craftpg;

import lombok.NonNull;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CraftpgApplication {

    public static void main(@NonNull final String[] args) {
        SpringApplication.run(CraftpgApplication.class, args);
    }
}
