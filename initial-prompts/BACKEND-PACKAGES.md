# Backend Package Structure (Java)

Base package: com.craftpg

com.craftpg
 ├── domain
 │    ├── model
 │    ├── event
 │
 ├── application
 │    ├── usecase
 │    ├── dto
 │    ├── mapper
 │
 ├── infrastructure
 │    ├── exception
 │    ├── configuration
 │    ├── persistence
 │    │     ├── repository
 │    ├── messaging
 │    │     ├── producer
 │    │     ├── consumer
 │    ├── web
 │    │     ├── controller
 │    │     ├── request
 │    │     ├── response
 │
 ├── shared
 │    ├── util
 │    ├── constants

Responsibilities:
- domain: domain entities(jpa entities), interfaces, rules, events
- application: use cases + DTOs + mappers
- infrastructure: Spring configs, web controllers, persistence repos, integrations, exceptions
- shared: utils and constants