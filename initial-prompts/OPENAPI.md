# OpenAPI (Source of Truth)

Location:
- back-end/docs/api/openapi.yml

Generation:
- Maven openapi-generator-maven-plugin
- Generates Spring server interfaces (interfaceOnly=true)
- Generated sources go to:
  - back-end/target/generated-sources/openapi

Implementation strategy:
- Implement generated interfaces in `infrastructure.web.controller`
- Controllers delegate to `application.usecase`
- Map DTOs via `application.mapper`