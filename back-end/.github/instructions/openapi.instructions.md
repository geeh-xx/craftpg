---
applyTo: "docs/**/*.yaml,docs/**/*.yml"
---

# OpenAPI Specification Instructions

## API Design

- Follow OpenAPI 3.0.3 specification
- Use API-First approach: design the API before implementation
- Define all endpoints, request/response schemas, and error responses
- Use consistent naming conventions

## Specification Structure

- One OpenAPI file per API domain (e.g., `auth.yaml`, `users.yaml`)
- Use `$ref` to reference shared components
- Define all schemas in the `components/schemas` section
- Define all responses in the `components/responses` section
- Define all parameters in the `components/parameters` section

## Versioning

- Use version prefix in paths: `/v1/`, `/v2/`, etc.
- Version the API at the path level, not in the OpenAPI version

## Schema Definitions

- Use descriptive schema names (PascalCase)
- Include `description` for all schemas and properties
- Use `required` array for mandatory fields
- Use appropriate types: `string`, `number`, `integer`, `boolean`, `array`, `object`
- Use `format` for specific types: `date-time`, `email`, `uuid`, `uri`
- Use `enum` for fixed value sets
- Define validation constraints: `minLength`, `maxLength`, `minimum`, `maximum`, `pattern`

## Response Definitions

- Define success and error responses for each endpoint
- Use standard HTTP status codes
- Include examples in responses
- Document all possible error scenarios

## Security

- Define security schemes in `components/securitySchemes`
- Apply security requirements at operation or global level
- Document authentication and authorization requirements

## Examples

- Provide example requests and responses
- Use realistic data in examples
- Include both success and error examples

## Best Practices

- Keep API design RESTful
- Use plural nouns for resource names
- Use HTTP verbs correctly: GET, POST, PUT, PATCH, DELETE
- Use query parameters for filtering, sorting, and pagination
- Use path parameters for resource identifiers
- Return appropriate status codes
- Include pagination for list endpoints
